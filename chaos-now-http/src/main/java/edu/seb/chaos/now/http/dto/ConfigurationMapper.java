package edu.seb.chaos.now.http.dto;

import edu.seb.chaos.now.http.config.AssaultProperties;
import org.mapstruct.Mapper;

@Mapper
public interface ConfigurationMapper {
    AssaultProperties fromDto(AssaultPropertiesDto dto);
    AssaultPropertiesDto toDto(AssaultProperties source);
}
