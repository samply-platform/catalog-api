package org.samply.catalog.api.domain.service;

import javax.inject.Inject;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.reactivex.Single;

class KafkaItemPublisher implements ItemPublisher {

    private static final String SELLER_ID_HEADER = "X-Seller-Id";

    private final Producer<String, Item> itemProducer;
    private final String topic;

    @Inject
    KafkaItemPublisher(@KafkaClient("capi-item-producer") Producer<String, Item> itemProducer,
                       KafkaConfig kafkaConfig) {
        this.itemProducer = itemProducer;
        this.topic = kafkaConfig.getTopic();
    }

    @Override
    public Single<Item> publish(Item item) {
        var itemRecord = new ProducerRecord<String, Item>(topic, item.getId(), item);
        itemRecord.headers().add(SELLER_ID_HEADER, item.getSellerId().getBytes());

        return Single.fromFuture(itemProducer.send(itemRecord))
                     .map(ignored -> item);
    }

    @Override
    public Single<ItemId> delete(ItemId itemId, SellerId sellerId) {
        var tombstoneRecord = new ProducerRecord<String, Item>(topic, itemId.getValue(), null);
        tombstoneRecord.headers().add(SELLER_ID_HEADER, sellerId.getValue().getBytes());

        return Single.fromFuture(itemProducer.send(tombstoneRecord))
                     .map(ignored -> itemId);
    }

}
