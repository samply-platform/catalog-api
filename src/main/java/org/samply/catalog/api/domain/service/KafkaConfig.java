package org.samply.catalog.api.domain.service;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PROTECTED)
@ConfigurationProperties("capi.kafka")
class KafkaConfig {

    private String topic;
    private String registryUrl;

}