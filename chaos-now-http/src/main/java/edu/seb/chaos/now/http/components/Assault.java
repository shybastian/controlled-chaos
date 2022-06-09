package edu.seb.chaos.now.http.components;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import edu.seb.chaos.now.http.config.HttpSettings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
public class Assault {
    private final Gson gson = new Gson();

    private final HttpSettings httpSettings;

    public Assault(HttpSettings httpSettings) {
        this.httpSettings = httpSettings;
    }

    public String attack(String body) {
        if (!this.httpSettings.getHttpProperties().isEnabled()) {
            return body;
        }

        StringBuilder builder = new StringBuilder(body);

        // Delete first, so we don't delete something we added
        if (this.httpSettings.getAssaultProperties().isDeleteSpecificAttribute()) {
            builder = this.deleteSpecific(builder);
        }
        if (this.httpSettings.getAssaultProperties().isDeleteRandomAttribute()) {
            builder = this.deleteRandom(builder);
        }

        // Add last, so we don't delete something we added
        if (this.httpSettings.getAssaultProperties().isInsertSpecificAttribute()) {
            builder = this.insertSpecific(builder);
        }
        if (this.httpSettings.getAssaultProperties().isInsertRandomAttribute()) {
            builder = this.insertRandom(builder);
        }

        return builder.toString();
    }

    private StringBuilder deleteRandom(StringBuilder bodyBuilder) {
        JsonElement element = this.gson.fromJson(bodyBuilder.toString(), JsonElement.class);
        String keyToDelete = this.getKeyToDelete(element.getAsJsonObject().keySet());
        element.getAsJsonObject().remove(keyToDelete);
        bodyBuilder = new StringBuilder(element.toString());

        return bodyBuilder;
    }

    private StringBuilder deleteSpecific(StringBuilder bodyBuilder) {
        JsonElement element = this.gson.fromJson(bodyBuilder.toString(), JsonElement.class);
        element.getAsJsonObject().remove(this.httpSettings.getAssaultProperties().getDeleteAttributeKey());
        bodyBuilder = new StringBuilder(element.toString());

        return bodyBuilder;
    }

    private StringBuilder insertRandom(StringBuilder bodyBuilder) {
        final String key = RandomStringUtils.randomAlphanumeric(10);
        final String value = RandomStringUtils.randomAlphanumeric(10);

        JsonElement jsonElement = this.gson.fromJson(bodyBuilder.toString(), JsonElement.class);
        jsonElement.getAsJsonObject().addProperty(key, value);
        bodyBuilder = new StringBuilder(jsonElement.toString());

        return bodyBuilder;
    }

    private StringBuilder insertSpecific(StringBuilder bodyBuilder) {
        final String key = this.httpSettings.getAssaultProperties().getInsertAttributeKey();
        final String value = this.httpSettings.getAssaultProperties().getInsertAttributeValue();

        JsonElement jsonElement = this.gson.fromJson(bodyBuilder.toString(), JsonElement.class);
        jsonElement.getAsJsonObject().addProperty(key, value);
        bodyBuilder = new StringBuilder(jsonElement.toString());

        return bodyBuilder;
    }

    private String getKeyToDelete(Set<String> keySet) {
        List<String> keyList = new ArrayList<>(keySet);
        return keyList.get(new Random().nextInt(keyList.size()));
    }
}
