package org.samply.catalog.api.web;

import java.math.BigDecimal;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.samply.catalog.api.domain.model.Category;
import org.samply.catalog.api.domain.model.Error;
import org.samply.catalog.api.domain.model.ItemCreationDTO;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;


@Validated
@Controller(
    value = "/items",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON
)
@SecurityRequirements({
        @SecurityRequirement(name = "oauth2")
})
public class ItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ItemResource.class);

    @Post
    @ApiResponses({
            @ApiResponse(
                responseCode = "201",
                content = @Content(schema = @Schema(implementation = ItemDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(
                        type = "array",
                        implementation = Error.class
                    )
                )
            )
    })
    public HttpResponse<ItemDTO> addItem(@Valid @NotNull @Header("X-User-Id") SellerId sellerId,
                                         @Valid @NotNull @Named("item") @Body ItemCreationDTO item) {
        LOG.info("POST Item for {}", sellerId);

        return HttpResponse.created(
                ItemDTO.of(
                        ItemId.of("234234"),
                        "34534",
                        "description",
                        BigDecimal.valueOf(9.99),
                        Category.A
                )
        );
    }

}
