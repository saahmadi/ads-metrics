package io.webmetric.advertisement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AdvertisementTests {

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .create();

    //@Test
    public void generateUUID() {
        for (var i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID());
        }
    }

    @Test
    public void test() {
        var json = "{\"name\":\"John Doe\",\"age\":30,\"address\":\"123 Main St\"}";

        var person = new Gson().fromJson(json, Person.class);

        assertEquals("John Doe", person.name);
        assertEquals(30, person.age);
        assertEquals("123 Main St", person.address);

        System.out.println(UUID.randomUUID());

    }

    @Test
    public void testImpressionsJson() throws IOException {

        URI uri = new File(System.getProperty("user.dir") + File.separator + "impressions.json").toURI();

        var impressions = new String(Files.readAllBytes(Path.of(uri)));

        var listType = new TypeToken<List<Impression>>() {
        }.getType();

        List<Impression> impressionsList = gson.fromJson(impressions, listType);

        assertEquals(2047, impressionsList.size());

        var impression = impressionsList.get(0);

        assertEquals("a39747e8-9c58-41db-8f9f-27963bc248b5", impression.id.toString());
        assertEquals(32, impression.appId);
        assertEquals("UK", impression.countryCode);
        assertEquals(8, impression.advertiserId);
    }

    @Test
    public void testClicksJson() throws IOException {

        URI uri = new File(System.getProperty("user.dir") + File.separator + "clicks.json").toURI();

        var clicks = new String(Files.readAllBytes(Path.of(uri)));

        var listType = new TypeToken<List<Click>>() {
        }.getType();

        var gson = new GsonBuilder()
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create();

        List<Click> clicksList = gson.fromJson(clicks, listType);

        assertEquals(95, clicksList.size());

        var click = clicksList.get(0);

        assertEquals("97dd2a0f-6d42-4c63-8cd6-5270c19f20d6", click.impressionId.toString());
        assertEquals(2.091225600111518, click.revenue);
    }

    public record Person(String name, int age, String address) {
    }

    public record Impression(UUID id, int appId, String countryCode, int advertiserId) {
    }

    public record Click(UUID impressionId, double revenue) {
    }
}
