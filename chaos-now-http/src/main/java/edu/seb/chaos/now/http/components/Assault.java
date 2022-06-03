package edu.seb.chaos.now.http.components;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import edu.seb.chaos.now.http.config.HttpSettings;
import lombok.extern.slf4j.Slf4j;

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

        // Delete first, so we don't delete something we added or something we modified
        if (this.httpSettings.getAssaultProperties().isDeleteRandomAttribute()) {
            log.info("Deleting random attribute ...");
            log.info("Body before delete: {}", builder);
            builder = this.delete(builder);
            log.info("Body after delete: {}", builder);

        }

        // Updating second, so we don't delete something we modified or update something we added
        if (this.httpSettings.getAssaultProperties().isModifyRandomAttribute()) {
            log.info("Modifying random attribute ...");

        }

        // Add last, so we don't delete something we added or update what we added
        if (this.httpSettings.getAssaultProperties().isInsertRandomAttribute()) {
            log.info("Inserting random attribute ...");
            log.info("Body before insert: {}", builder);
            builder = this.insert(builder);
            log.info("Body after insert: {}", builder);
        }

        return builder.toString();
    }

    private StringBuilder delete(StringBuilder bodyBuilder) {
        JsonElement element = this.gson.fromJson(bodyBuilder.toString(), JsonElement.class);

        if (this.httpSettings.getAssaultProperties().isDeleteSpecificAttribute()) {
            log.info("Deleting specific Attribute ...");
            element.getAsJsonObject().remove(this.httpSettings.getAssaultProperties().getDeleteAttributeKey());
            bodyBuilder = new StringBuilder(element.toString());
            return bodyBuilder;
        }

        log.info("Deleting random Attribute ...");
        String keyToDelete = this.getKeyToDelete(element.getAsJsonObject().keySet());
        element.getAsJsonObject().remove(keyToDelete);
        bodyBuilder = new StringBuilder(element.toString());

        return bodyBuilder;
    }

    private StringBuilder update(StringBuilder bodyBuilder) {
        return null;
    }

    private StringBuilder insert(StringBuilder bodyBuilder) {
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
