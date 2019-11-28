package org.samply.catalog.api.domain.model;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value(staticConstructor = "of")
@Schema(type = "string", example = "5678")
public class ItemId {
    @JsonValue
    @NotBlank
    private final String value;
}
