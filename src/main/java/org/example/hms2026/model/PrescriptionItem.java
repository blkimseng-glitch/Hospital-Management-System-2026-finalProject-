package org.example.hms2026.model;

public class PrescriptionItem {
    private Medicine medicine;
    private int quantity;
    private double subTotal;

    public PrescriptionItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.subTotal = medicine.getPrice() * quantity;
    }

    public Medicine getMedicine() { return medicine; }
    public String getMedicineName() { return medicine.getName(); }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subTotal = medicine.getPrice() * quantity;
    }
    public double getSubTotal() { return subTotal; }
}