package io.webmetric.advertisement.services;

import com.google.gson.Gson;
import io.webmetric.advertisement.config.DatabaseLoader;
import io.webmetric.advertisement.domain.Click;
import io.webmetric.advertisement.domain.Impression;
import io.webmetric.advertisement.domain.ImpressionRepository;
import io.webmetric.advertisement.dto.ReportDto;
import io.webmetric.advertisement.dto.TopAdviserDto;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class BusinessService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private final Gson gson;
    private final ImpressionRepository impressionRepository;
    private final DatabaseLoader databaseLoader;

    public BusinessService(Gson gson, ImpressionRepository impressionRepository, DatabaseLoader databaseLoader) {
        this.gson = gson;
        this.impressionRepository = impressionRepository;
        this.databaseLoader = databaseLoader;
    }

    @PostConstruct
    public void ready() throws IOException {
        databaseLoader.readImpressionsJsonFileAndSaveToDB(impressionRepository);
        databaseLoader.readClicksJsonFileAndSaveToDB(impressionRepository);
        exportJsonReportsByDimensions();
        exportJsonReportsTop5Adviser();
    }

    private void exportJsonReportsByDimensions() throws IOException {

        var map = impressionRepository.findAll().stream()
                .collect(groupingBy(Impression::getAppId, groupingBy(Impression::getCountryCode)));

        var reports = new ArrayList<ReportDto>();

        for (var app : map.entrySet()) {
            for (var country : app.getValue().entrySet()) {
                var listOfClicks = country.getValue().stream().filter(e -> e.getClick() != null).map(Impression::getClick).toList();
                double sum = listOfClicks.stream().mapToDouble(Click::revenue).sum();
                reports.add(new ReportDto(app.getKey(), country.getKey(), country.getValue().size(), listOfClicks.size(), sum));
            }
        }

        log.info("Report List by dimensions with size {} ", reports.size());

        try (FileWriter reportFile = new FileWriter(System.getProperty("user.dir") + File.separator + "report-dimension.json")) {
            reportFile.write(gson.toJson(reports));
        }
    }

    private void exportJsonReportsTop5Adviser() throws IOException {

        var topAdviserList = new ArrayList<TopAdviserDto>();

        var map = impressionRepository.findAll().stream()
                .collect(groupingBy(Impression::getAppId, groupingBy(Impression::getCountryCode)));

        for (var app : map.entrySet()) {

            for (var countryCode : app.getValue().entrySet()) {

                var mapByAdvertiserId = countryCode.getValue().stream().filter(e -> e.getClick() != null).collect(groupingBy(Impression::getAdvertiserId));

                var top5Ids = mapByAdvertiserId.entrySet().stream().map(entry -> {
                    var advertiserId = entry.getKey();
                    double revenue = entry.getValue().stream().mapToDouble(t -> t.getClick().revenue()).sum();
                    double rate = revenue / entry.getValue().size();
                    return Map.of(advertiserId, rate);
                }).flatMap(m -> m.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue)).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).toList();

                topAdviserList.add(new TopAdviserDto(app.getKey(), countryCode.getKey(), top5Ids));

            }
        }

        log.info("Report List Top 5 with size {} ", topAdviserList.size());

        try (FileWriter reportFile = new FileWriter(System.getProperty("user.dir") + File.separator + "report-top5.json")) {
            reportFile.write(gson.toJson(topAdviserList));
        }
    }
}
