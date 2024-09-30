package io.webmetric.advertisement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("IMPRESSION_CLICKS")
public record Click(@Id Long id, Double revenue) {
}
