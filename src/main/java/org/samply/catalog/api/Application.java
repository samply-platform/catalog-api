package org.samply.catalog.api;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
    info = @Info(
        title = "catalog-api",
        version = "0.0"
    )
)
@SecuritySchemes({
        @SecurityScheme(
            name = "oauth2",
            type = SecuritySchemeType.OAUTH2,
            flows = @OAuthFlows(
                password = @OAuthFlow(
                    tokenUrl = "http://localhost:8090/auth/realms/samply/protocol/openid-connect/token"
                )
            )
        )
})
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

}
