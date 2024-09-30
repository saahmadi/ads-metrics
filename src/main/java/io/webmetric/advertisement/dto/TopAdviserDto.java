package io.webmetric.advertisement.dto;

import java.util.List;

public record TopAdviserDto(int appId, String countryCode, List<Integer> recommendedAdvertiserIds) {
}