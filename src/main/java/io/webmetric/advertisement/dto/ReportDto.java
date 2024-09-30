package io.webmetric.advertisement.dto;

public record ReportDto(int appId, String countryCode, int impressions, int clicks, double revenue) {
}
