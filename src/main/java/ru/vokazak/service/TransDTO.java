package ru.vokazak.service;

import java.math.BigDecimal;
import java.util.Date;

public class TransDTO {

    private long id;
    private Date date;
    private String description;
    private BigDecimal money;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id = " + id + ": [" + date + "], " + description + ", money: " + money + ';';
    }

}
