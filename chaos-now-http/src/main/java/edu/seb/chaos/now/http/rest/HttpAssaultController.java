package edu.seb.chaos.now.http.rest;

import edu.seb.chaos.now.http.config.AssaultProperties;
import edu.seb.chaos.now.http.config.HttpProperties;
import edu.seb.chaos.now.http.config.HttpSettings;
import edu.seb.chaos.now.http.dto.AssaultPropertiesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.web.bind.annotation.*;

@RestControllerEndpoint(id = "chaos-now-http", enableByDefault = true)
@RestController
@CrossOrigin
@RequestMapping("/assault/http")
@Slf4j
public class HttpAssaultController {
    private final static String RESPONSE_TYPE = "application/json";
    private final static String ACCEPT_TYPE = "application/json";

    private final HttpSettings settings;

    public HttpAssaultController(HttpSettings settings) {
        this.settings = settings;
    }

    @GetMapping("/isEnabled")
    public Boolean isEnabled() {
        HttpProperties properties = this.settings.getHttpProperties();
        return properties.isEnabled();
    }

    @PostMapping("/isEnabled/{isActive}")
    public void setIsActive(@PathVariable Boolean isActive) {
        log.info("Is Attack Enabled? {}", isActive);
        HttpProperties properties = this.settings.getHttpProperties();
        properties.setEnabled(isActive);
    }

    @GetMapping(produces = RESPONSE_TYPE)
    public AssaultPropertiesDto getProperties() {
        return getPropertiesDto();
    }

    @PostMapping(produces = RESPONSE_TYPE, consumes = ACCEPT_TYPE)
    public AssaultPropertiesDto setAssaultProperties(@RequestBody AssaultPropertiesDto dto) {
        return setProperties(dto);
    }

    private AssaultPropertiesDto setProperties(AssaultPropertiesDto propertiesDto) {
        AssaultProperties properties = this.settings.getAssaultProperties();

        properties.setInsertRandomAttribute(propertiesDto.isInsertRandomAttribute());
        properties.setInsertSpecificAttribute(propertiesDto.isInsertSpecificAttribute());
        properties.setDeleteRandomAttribute(propertiesDto.isDeleteRandomAttribute());
        properties.setDeleteSpecificAttribute(propertiesDto.isDeleteSpecificAttribute());
        properties.setModifyRandomAttribute(propertiesDto.isModifyRandomAttribute());
        properties.setModifySpecificAttribute(propertiesDto.isModifySpecificAttribute());

        properties.setInsertAttributeKey(propertiesDto.getInsertAttributeKey());
        properties.setInsertAttributeValue(propertiesDto.getInsertAttributeValue());
        properties.setDeleteAttributeKey(propertiesDto.getDeleteAttributeKey());

        return propertiesDto;
    }

    private AssaultPropertiesDto getPropertiesDto() {
        AssaultProperties properties = this.settings.getAssaultProperties();
        AssaultPropertiesDto propertiesDto = new AssaultPropertiesDto();

        propertiesDto.setInsertRandomAttribute(properties.isInsertRandomAttribute());
        propertiesDto.setInsertSpecificAttribute(properties.isInsertSpecificAttribute());
        propertiesDto.setDeleteRandomAttribute(properties.isDeleteRandomAttribute());
        propertiesDto.setDeleteSpecificAttribute(properties.isDeleteSpecificAttribute());
        propertiesDto.setModifyRandomAttribute(properties.isModifyRandomAttribute());
        propertiesDto.setModifySpecificAttribute(properties.isModifySpecificAttribute());

        propertiesDto.setInsertAttributeKey(properties.getInsertAttributeKey());
        propertiesDto.setInsertAttributeValue(properties.getInsertAttributeValue());
        propertiesDto.setDeleteAttributeKey(properties.getDeleteAttributeKey());

        return propertiesDto;
    }


}
