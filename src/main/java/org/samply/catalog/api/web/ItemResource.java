package org.samply.catalog.api.web;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.samply.catalog.api.domain.exception.ItemNotFoundException;
import org.samply.catalog.api.domain.model.Error;
import org.samply.catalog.api.domain.model.ItemDataDTO;
import org.samply.catalog.api.domain.model.ItemDTO;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import org.samply.catalog.api.domain.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
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

    private final ItemService itemService;

    public ItemResource(ItemService itemService) {
        this.itemService = itemService;
    }

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
    public Single<HttpResponse<ItemDTO>> add(@Valid @NotNull @Header("X-User-Id") SellerId sellerId,
                                             @Valid @NotNull @Body ItemDataDTO item) {
        LOG.info("POST Item for {}", sellerId);

        return itemService.add(item, sellerId)
                          .map(HttpResponse::created);
    }

    @Put("/{itemId}")
    @ApiResponses({
            @ApiResponse(responseCode = "202"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "403"),
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
    public Single<HttpResponse<?>> update(@Valid @NotNull @Header("X-User-Id") SellerId sellerId,
                                          @Valid @NotNull @PathVariable ItemId itemId,
                                          @Valid @NotNull @Body ItemDataDTO item) {
        LOG.info("PUT Item for id {} for seller {}", itemId, sellerId);

        return itemService.update(itemId, item, sellerId)
                          .<HttpResponse<?>>toSingleDefault(HttpResponse.accepted())
                          .onErrorReturn(this::handleError);
    }

    @Delete("/{itemId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "403")
    })
    public Single<HttpResponse<?>> delete(@Valid @NotNull @Header("X-User-Id") SellerId sellerId,
                                          @Valid @NotNull @PathVariable ItemId itemId) {
        LOG.info("DELETE Item for id {} for seller {}", itemId, sellerId);

        return itemService.delete(itemId, sellerId)
                          .<HttpResponse<?>>toSingleDefault(HttpResponse.noContent())
                          .onErrorReturn(this::handleError);
    }


    private HttpResponse<?> handleError(Throwable e) {
        if (e instanceof ItemNotFoundException) {
            return HttpResponse.notFound();
        } else if (e instanceof IllegalAccessException) {
            return HttpResponse.status(HttpStatus.FORBIDDEN);
        } else {
            return HttpResponse.serverError();
        }
    }


}
