package edu.seb.chaos.now.http.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "chaos.now.http.assaults")
public class AssaultProperties {
    private boolean insertRandomAttribute = false;
    private boolean insertSpecificAttribute = false;
    private boolean deleteSpecificAttribute = false;
    private boolean deleteRandomAttribute = false;
    private boolean modifySpecificAttribute = false;
    private boolean modifyRandomAttribute = false;

    private String insertAttributeKey = "key";
    private String insertAttributeValue = "value";
    private String deleteAttributeKey = "username";

    public void setInsertRandomAttribute(boolean insertRandomAttribute) {
        this.insertRandomAttribute = insertRandomAttribute;
    }

    public void setInsertSpecificAttribute(boolean insertSpecificAttribute) {
        this.insertSpecificAttribute = insertSpecificAttribute;
    }

    public void setDeleteSpecificAttribute(boolean deleteSpecificAttribute) {
        this.deleteSpecificAttribute = deleteSpecificAttribute;
    }

    public void setDeleteRandomAttribute(boolean deleteRandomAttribute) {
        this.deleteRandomAttribute = deleteRandomAttribute;
    }

    public void setModifySpecificAttribute(boolean modifySpecificAttribute) {
        this.modifySpecificAttribute = modifySpecificAttribute;
    }

    public void setModifyRandomAttribute(boolean modifyRandomAttribute) {
        this.modifyRandomAttribute = modifyRandomAttribute;
    }

    public void setInsertAttributeKey(String insertAttributeKey) {
        this.insertAttributeKey = insertAttributeKey;
    }

    public void setInsertAttributeValue(String insertAttributeValue) {
        this.insertAttributeValue = insertAttributeValue;
    }

    public void setDeleteAttributeKey(String deleteAttributeKey) {
        this.deleteAttributeKey = deleteAttributeKey;
    }
}
