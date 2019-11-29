package org.samply.catalog.api.domain.service;

import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.samply.catalog.api.domain.model.Item;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.context.annotation.Factory;
import io.reactivex.Single;

@Factory
class KafkaItemPublisherFactory {

    @Singleton
    @Named("item-created-Publisher")
    KafkaItemPublisher itemCreatedPublisher(@KafkaClient("capi-item-producer") Producer<String, Item> itemProducer) {
        return new KafkaItemPublisher("item-created-log", itemProducer);
    }

    @Singleton
    @Named("item-updated-Publisher")
    KafkaItemPublisher itemUpdatedPublisher(@KafkaClient("capi-item-producer") Producer<String, Item> itemProducer) {
        return new KafkaItemPublisher("item-updated-log", itemProducer);
    }

    static class KafkaItemPublisher implements ItemPublisher {

        private final Producer<String, Item> itemProducer;
        private final String topic;

        KafkaItemPublisher(String topic, @KafkaClient("capi-item-producer") Producer<String, Item> itemProducer) {
            this.topic = topic;
            this.itemProducer = itemProducer;
        }

        @Override
        public Single<Item> apply(Item item) {
            var itemRecord = new ProducerRecord<String, Item>(topic, item.getId(), item);

            return Single.fromFuture(itemProducer.send(itemRecord))
                         .map(ignored -> item);
        }

    }

}
