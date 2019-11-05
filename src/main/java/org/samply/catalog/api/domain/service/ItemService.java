package org.samply.catalog.api.domain.service;

import java.util.UUID;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemCreationDTO;
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

    public Single<ItemDTO> addItem(ItemCreationDTO item, SellerId sellerId) {
        Item itemEvent = createItemEvent(item, sellerId);

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

    private static Item createItemEvent(ItemCreationDTO item, SellerId sellerId) {
        return Item.newBuilder()
                   .setId(UUID.randomUUID().toString())
                   .setSellerId(sellerId.getValue())
                   .setTitle(item.getTitle())
                   .setDescription(item.getDescription())
                   .setCategory(item.getCategory())
                   .setPrice(item.getPrice())
                   .build();
    }

}
