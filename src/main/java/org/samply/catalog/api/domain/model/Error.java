package org.samply.catalog.api.domain.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {

    private final String message;

}
