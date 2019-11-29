package org.samply.catalog.api.web;

import javax.inject.Singleton;
import org.samply.catalog.api.domain.model.ItemId;
import org.samply.catalog.api.domain.model.SellerId;
import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.annotations.GraphQLArgument;
import io.aexp.nodes.graphql.annotations.GraphQLProperty;
import io.micronaut.context.annotation.Value;
import io.reactivex.Maybe;
import lombok.AllArgsConstructor;

@Singleton
public class CatalogQueryApiClient {

    private final GraphQLTemplate graphQLTemplate = new GraphQLTemplate();

    @Value("${cqapi.graphql.url:`http://localhost:8888/graphql`}")
    String graphqlUrl;

    public Maybe<CatalogQueryApiItemResponse> getItem(ItemId itemId) {
        return Maybe.<GraphQLResponseEntity<CatalogQueryApiItemResponse>>fromCallable(() -> {
            GraphQLRequestEntity request = GraphQLRequestEntity.Builder()
            .url(graphqlUrl)
            .arguments(new Arguments("item", new Argument<String>("id", itemId.getValue())))
            .request(CatalogQueryApiItemResponse.class)
            .build();

            return graphQLTemplate.query(request, CatalogQueryApiItemResponse.class);
        })
        .filter(r -> r.getResponse() != null)
        .map(GraphQLResponseEntity::getResponse);
    }

    @AllArgsConstructor
    @GraphQLProperty(name = "item", arguments = {
            @GraphQLArgument(name = "id")
    })
    public static class CatalogQueryApiItemResponse {

        private final String sellerId;

        public SellerId getSellerId() {
            return new SellerId(sellerId);
        }
    }

}