package org.samply.catalog.api.domain.service;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.samply.catalog.api.domain.model.Item;
import org.samply.catalog.api.domain.model.ItemId;
import io.micronaut.configuration.kafka.streams.InteractiveQueryService;
import io.vavr.Lazy;

@Singleton
public class ItemQueryService {

    private final InteractiveQueryService interactiveQueryService;
    private final Lazy<ReadOnlyKeyValueStore<String, Item>> itemLogTable = Lazy.of(
            this::getItemLogTableStore
    );

    @Inject
    public ItemQueryService(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    private ReadOnlyKeyValueStore<String, Item> getItemLogTableStore() {
        return interactiveQueryService.getQueryableStore(
                ItemLogStream.ITEM_LOG_TABLE,
                QueryableStoreTypes.<String, Item>keyValueStore()
        ).orElseThrow();
    }

    public Optional<Item> byId(ItemId id) {
        return Optional.ofNullable(itemLogTable.get().get(id.getValue()));
    }

}
