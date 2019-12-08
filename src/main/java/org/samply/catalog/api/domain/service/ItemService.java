package org.samply.catalog.api.domain.service;

import static io.reactivex.Single.fromCallable;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.exception.ItemNotFoundException;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemDataDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.reactivex.Completable;
import io.reactivex.Single;

@Singleton
public class ItemService {

    private final ItemPublisher itemPublisher;
    private final ItemQueryService itemQueryService;

    @Inject
    public ItemService(final ItemPublisher itemPublisher,
                       final ItemQueryService itemQueryService) {
        this.itemPublisher = itemPublisher;
        this.itemQueryService = itemQueryService;
    }

    public Single<ItemDTO> add(final ItemDataDTO item, final SellerId sellerId) {
        final ItemId itemId = ItemId.of(UUID.randomUUID().toString());
        final Item itemEvent = createItemEvent(itemId, item, sellerId);

        return itemPublisher.publish(itemEvent)
                            .map(ItemService::toDTO);
    }

    public Completable update(final ItemId itemId, final ItemDataDTO item, final SellerId sellerId) {
        return ensureItemExistsAndIsAccessible(itemId, sellerId)
                .map(i -> createItemEvent(itemId, item, sellerId))
                .flatMap(itemEvent -> itemPublisher.publish(itemEvent))
                .ignoreElement();
    }

    public Completable delete(final ItemId itemId, final SellerId sellerId) {
        return ensureItemExistsAndIsAccessible(itemId, sellerId)
                .flatMap(i -> itemPublisher.delete(itemId, sellerId))
                .ignoreElement();
    }

    private Single<Item> ensureItemExistsAndIsAccessible(final ItemId itemId, final SellerId sellerId) {
        return fromCallable(() -> itemQueryService.byId(itemId).orElseThrow(ItemNotFoundException::new))
              .filter(i -> i.getSellerId().equals(sellerId.getValue()))
              .switchIfEmpty(Single.error(new IllegalAccessException()));
    }

    private static ItemDTO toDTO(final Item item) {
        return ItemDTO.of(
                ItemId.of(item.getId()),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getCategory()
        );
    }

    private static Item createItemEvent(final ItemId itemId, final ItemDataDTO item, final SellerId sellerId) {
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
