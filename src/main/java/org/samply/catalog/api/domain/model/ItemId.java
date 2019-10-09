package org.samply.catalog.api.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value(staticConstructor = "of")
@Schema(type = "string")
public class ItemId {
    @JsonValue
    private final String value;
}
