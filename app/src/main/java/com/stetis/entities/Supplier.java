package com.stetis.entities;

public class Supplier {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String accountNumber;
    private String bank;
    private String natureOfMaterial;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNatureOfMaterial() {
        return natureOfMaterial;
    }

    public void setNatureOfMaterial(String natureOfMaterial) {
        this.natureOfMaterial = natureOfMaterial;
    }

    @Override
    public String toString() {
        return name;
    }
}
