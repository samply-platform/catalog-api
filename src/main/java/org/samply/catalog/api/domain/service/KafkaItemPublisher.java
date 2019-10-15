package org.samply.catalog.api.domain.service;

import javax.inject.Singleton;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.samply.catalog.api.domain.model.Item;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.reactivex.Single;


@Singleton
class KafkaItemPublisher implements ItemPublisher {

    private final Producer<String, Item> itemProducer;

    KafkaItemPublisher(@KafkaClient("capi-item-producer") Producer<String, Item> itemProducer) {
        this.itemProducer = itemProducer;
    }

    @Override
    public Single<Item> apply(Item item) {
        var itemRecord = new ProducerRecord<String, Item>("item-created-log", item.getId(), item);

        return Single.fromFuture(itemProducer.send(itemRecord))
                     .map(ignored -> item);
    }

}
