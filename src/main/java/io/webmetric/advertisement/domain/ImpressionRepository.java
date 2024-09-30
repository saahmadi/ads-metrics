package io.webmetric.advertisement.domain;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ImpressionRepository extends ListCrudRepository<Impression, Long> {
    Optional<Impression> findByImpressionId(String impressionId);
}
