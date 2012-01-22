package org.mongeez.examples.dto;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({"id", "assetType", "status"})
@Entity(value = "Asset", concern = "safe")
public class Asset {
    @Id
    private String id;

    private String assetType;
    private String status;

    private Double dividend;       // PRIVATE_PREFERRED
    private Date expirationDate;   // PRIVATE_OPTIONS
    private String ticker;         // PRIVATE_COMMONSTOCK

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

    public Double getDividend() {
        return dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
