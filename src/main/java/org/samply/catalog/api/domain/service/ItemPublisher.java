package org.samply.catalog.api.domain.service;

import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.reactivex.Single;

/**
 * ItemPublisher
 */
interface ItemPublisher {

    public Single<Item> publish(Item item);
    public Single<ItemId> delete(ItemId itemId, SellerId sellerId);

}