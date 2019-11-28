package org.samply.catalog.api.web;

import java.util.Optional;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.ItemId;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

@Singleton
public class ItemIdConverter implements TypeConverter<String, ItemId> {

    @Override
    public Optional<ItemId> convert(String object,
                                    Class<ItemId> targetType,
                                    ConversionContext context) {
        return Optional.of(ItemId.of(object));
    }

}
