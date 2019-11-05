package org.samply.catalog.api.web;

import java.net.URI;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller("/")
public class RootResource {

    @Get
    @Hidden    HttpResponse<?> redirectToApiDocs() {
        return HttpResponse.redirect(URI.create("/swagger-ui"));
    }

}
