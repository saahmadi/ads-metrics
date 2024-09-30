package io.webmetric.advertisement.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.webmetric.advertisement.domain.Click;
import io.webmetric.advertisement.domain.Impression;
import io.webmetric.advertisement.domain.ImpressionRepository;
import io.webmetric.advertisement.dto.ClickDto;
import io.webmetric.advertisement.dto.ImpressionDto;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Component
public class DatabaseLoader {

    private final Gson gson;

    public DatabaseLoader(Gson gson) {
        this.gson = gson;
    }

    public void readClicksJsonFileAndSaveToDB(ImpressionRepository impressionRepository) throws IOException {
        var uri = new File(System.getProperty("user.dir") + File.separator + "clicks.json").toURI();

        var clicks = new String(Files.readAllBytes(Path.of(uri)));

        var listType = new TypeToken<List<ClickDto>>() {
        }.getType();

        List<ClickDto> clicksList = gson.fromJson(clicks, listType);

        clicksList = clicksList.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(ClickDto::impressionId))), ArrayList::new));

        clicksList.forEach(item -> impressionRepository.findByImpressionId(item.impressionId().toString())
                .ifPresent(impression -> {
                    impression.setClick(new Click(null, item.revenue()));
                    impressionRepository.save(impression);
                }));
    }

    public void readImpressionsJsonFileAndSaveToDB(ImpressionRepository impressionRepository) throws IOException {
        var uri = new File(System.getProperty("user.dir") + File.separator + "impressions.json").toURI();

        var impressions = new String(Files.readAllBytes(Path.of(uri)));

        var listType = new TypeToken<List<ImpressionDto>>() {
        }.getType();

        List<ImpressionDto> impressionsList = gson.fromJson(impressions, listType);

        impressionsList = impressionsList.stream()
                .filter(e -> e.id() != null && e.countryCode() != null && !e.countryCode().isBlank())
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(ImpressionDto::id))), ArrayList::new));


        impressionsList.forEach(item -> impressionRepository.save(new Impression(item.id().toString(), item.appId(), item.countryCode(), item.advertiserId())));
    }


}
