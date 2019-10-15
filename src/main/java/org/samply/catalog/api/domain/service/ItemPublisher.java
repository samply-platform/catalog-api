package org.samply.catalog.api.domain.service;

import java.util.function.Function;
import org.samply.catalog.api.domain.model.Item;
import io.reactivex.Single;

/**
 * ItemPublisher
 */
interface ItemPublisher extends Function<Item, Single<Item>> {}