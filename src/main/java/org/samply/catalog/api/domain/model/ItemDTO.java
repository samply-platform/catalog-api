package org.samply.catalog.api.domain.model;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value(staticConstructor = "of")
@Schema(title = "ItemResponse")
public class ItemDTO {

    private final ItemId id;
    private final String title;
    private final String description;
    private final BigDecimal price;
    private final Category category;

}
