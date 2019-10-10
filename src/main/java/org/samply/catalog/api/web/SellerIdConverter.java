package org.samply.catalog.api.web;

import java.util.Optional;
import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.SellerId;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;

@Singleton
public class SellerIdConverter implements TypeConverter<String, SellerId> {

    @Override
    public Optional<SellerId> convert(String object,
                                      Class<SellerId> targetType,
                                      ConversionContext context) {
        return Optional.of(new SellerId(object));
    }

}
