package org.samply.catalog.api.domain.model;

import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
@Schema(example = "1234", type = "string", required = true)
public class SellerId {

    @JsonValue
    @NotBlank
    private final String value;

}
