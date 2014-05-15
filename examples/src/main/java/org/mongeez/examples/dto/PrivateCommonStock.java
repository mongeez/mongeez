package org.mongeez.examples.dto;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "Asset", concern = "SAFE")
public class PrivateCommonStock extends Asset {
    private String ticker;         // PRIVATE_COMMONSTOCK

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
