package org.mongeez.examples.dto;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "Asset", concern = "SAFE")
public class PrivatePreferredStock extends Asset {
    private Double dividend;       // PRIVATE_PREFERRED

    public Double getDividend() {
        return dividend;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }
}
