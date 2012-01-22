package org.mongeez.examples.dto;

import com.google.code.morphia.annotations.Entity;

import java.util.Date;

@Entity(value = "Asset", concern = "SAFE")
public class PrivateOption extends Asset {
    private Date expirationDate;   // PRIVATE_OPTIONS

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
