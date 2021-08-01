package com.stetis.entities;

public class Item {
    private int itemid;
    private String item;
    private String unit;


    public Item() {
    }

    public Item(int itemid, String item) {
        this.itemid = itemid;
        this.item = item;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return item;
    }
}
