package org.samply.catalog.api.domain.service;

import java.util.Map;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.samply.catalog.api.domain.model.Item;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;

@Factory
class ItemLogStream {

    static final String ITEM_LOG_TABLE = "item-log-table";

    @Singleton
    @Named("item-log-stream")
    KStream<String, Item> itemLogStream(ConfiguredStreamBuilder builder, KafkaConfig kafkaConfig) {
        Properties props = builder.getConfiguration();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var itemSerde = new SpecificAvroSerde<Item>();
        itemSerde.configure(Map.of("schema.registry.url", kafkaConfig.getRegistryUrl()), false);

        return builder.table(
                kafkaConfig.getTopic(),
                Consumed.with(Serdes.String(), itemSerde),
                Materialized.<String, Item, KeyValueStore<Bytes, byte[]>>as(ITEM_LOG_TABLE)
        ).toStream();
    }

}
