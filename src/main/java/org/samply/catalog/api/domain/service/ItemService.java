package org.samply.catalog.api.domain.service;

import static io.micronaut.http.HttpStatus.FORBIDDEN;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import java.util.UUID;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemDataDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import org.samply.catalog.api.web.CatalogQueryApiClient;
import io.micronaut.http.exceptions.HttpStatusException;
import io.reactivex.Single;

@Singleton
public class ItemService {

    private final ItemPublisher itemPublisher;
    private final CatalogQueryApiClient catalogQueryApiClient;

    public ItemService(ItemPublisher itemPublisher, CatalogQueryApiClient catalogQueryApiClient) {
        this.itemPublisher = itemPublisher;
        this.catalogQueryApiClient = catalogQueryApiClient;
    }

    public Single<ItemDTO> addItem(ItemDataDTO item, SellerId sellerId) {
        return publishItem(item, sellerId);
    }

    public Single<ItemDTO> updateItem(ItemId itemId, ItemDataDTO item, SellerId sellerId) {
        return catalogQueryApiClient.getItem(itemId)
                                    .switchIfEmpty(Single.error(new HttpStatusException(NOT_FOUND, "Not Found.")))
                                    .filter(response -> sellerId.equals(response.getSellerId()))
                                    .switchIfEmpty(Single.error(new HttpStatusException(FORBIDDEN, "Forbidden.")))
                                    .flatMap(i -> publishItem(itemId, item, sellerId));
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
