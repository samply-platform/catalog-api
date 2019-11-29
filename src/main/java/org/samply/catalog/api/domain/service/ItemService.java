package org.samply.catalog.api.domain.service;

import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemDataDTO;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.reactivex.Completable;
import io.reactivex.Single;

@Singleton
public class ItemService {

    private final ItemPublisher itemCreatedPublisher;
    private final ItemPublisher itemUpdatedPublisher;

    @Inject
    public ItemService(@Named("item-created-Publisher") ItemPublisher itemCreatedPublisher,
                       @Named("item-updated-Publisher") ItemPublisher itemUpdatedPublisher) {
        this.itemCreatedPublisher = itemCreatedPublisher;
        this.itemUpdatedPublisher = itemUpdatedPublisher;
    }

    public Single<ItemDTO> addItem(ItemDataDTO item, SellerId sellerId) {
        ItemId itemId = ItemId.of(UUID.randomUUID().toString());
        Item itemEvent = createItemEvent(itemId, item, sellerId);

        return itemCreatedPublisher.apply(itemEvent)
                                   .map(ItemService::toDTO);
    }

    public Completable updateItem(ItemId itemId, ItemDataDTO item, SellerId sellerId) {
        Item itemEvent = createItemEvent(itemId, item, sellerId);

        return itemUpdatedPublisher.apply(itemEvent)
                                   .ignoreElement();
    }

    private static ItemDTO toDTO(Item item) {
        return ItemDTO.of(
                ItemId.of(item.getId()),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory()
        );
    }

    private static Item createItemEvent(ItemId itemId, ItemDataDTO item, SellerId sellerId) {
        return Item.newBuilder()
                   .setId(itemId.getValue())
                   .setSellerId(sellerId.getValue())
                   .setTitle(item.getTitle())
                   .setDescription(item.getDescription())
                   .setCategory(item.getCategory())
                   .setPrice(item.getPrice())
                   .build();
    }

}
