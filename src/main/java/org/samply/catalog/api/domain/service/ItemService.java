package org.samply.catalog.api.domain.service;

import java.util.UUID;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemDataDTO;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.reactivex.Single;

@Singleton
public class ItemService {

    private final ItemPublisher itemPublisher;

    public ItemService(ItemPublisher itemPublisher) {
        this.itemPublisher = itemPublisher;
    }

    public Single<ItemDTO> addItem(ItemDataDTO item, SellerId sellerId) {
        return publishItem(item, sellerId);
    }

    public Single<ItemDTO> updateItem(ItemId itemId, ItemDataDTO item, SellerId sellerId) {
        return publishItem(itemId, item, sellerId);
    }

    private Single<ItemDTO> publishItem(ItemDataDTO item, SellerId sellerId) {
        return publishItem(ItemId.of(UUID.randomUUID().toString()), item, sellerId);
    }

    private Single<ItemDTO> publishItem(ItemId itemId, ItemDataDTO item, SellerId sellerId) {
        Item itemEvent = createItemEvent(itemId, item, sellerId);

        return itemPublisher.apply(itemEvent)
                            .map(ItemService::toDTO);
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
