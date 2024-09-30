package io.webmetric.advertisement.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record ImpressionDto(UUID id, int appId, @Nullable String countryCode, int advertiserId){}

