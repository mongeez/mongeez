package org.mongeez.examples.dto;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "assetType", "status"})
@Entity(value = "Asset", concern = "safe")
public abstract class Asset {
    @Id
    private String id;

    private String assetType;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
