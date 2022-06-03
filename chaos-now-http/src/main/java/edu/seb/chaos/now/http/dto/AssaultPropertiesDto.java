package edu.seb.chaos.now.http.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssaultPropertiesDto {
    private boolean insertRandomAttribute;
    private boolean insertSpecificAttribute;
    private boolean deleteSpecificAttribute;
    private boolean deleteRandomAttribute;
    private boolean modifySpecificAttribute;
    private boolean modifyRandomAttribute;

    private String insertAttributeKey;
    private String insertAttributeValue;

    private String deleteAttributeKey;
}
