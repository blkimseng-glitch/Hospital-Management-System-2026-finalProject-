package org.example.hms2026.model;

public class Billing {
    private String billId;
    private String patientName;    // ឈ្មោះអ្នកជំងឺ (បន្ថែមថ្មី)
    private String doctorName;
    private String diagnosis;      // រោគវិនិច្ឆ័យជំងឺ
    private String medicineName;   // ឈ្មោះថ្នាំ និងវេជ្ជបញ្ជា
    private String diseaseDetails; // ព័ត៌មានលម្អិតពីបញ្ហាជំងឺ
    private double totalAmount;    // ចំនួនទឹកប្រាក់
    private String status;         // ស្ថានភាព (Paid / Unpaid)

    public Billing(String billId, String patientName, String doctorName, String diagnosis, String medicineName, String diseaseDetails, double totalAmount, String status) {
        this.billId = billId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.medicineName = medicineName;
        this.diseaseDetails = diseaseDetails;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters
    public String getBillId() { return billId; }
    public String getPatientName() { return patientName; } // ត្រូវមាន Method នេះ
    public String getDoctorName() { return doctorName; }
    public String getDiagnosis() { return diagnosis; }
    public String getMedicineName() { return medicineName; }
    public String getDiseaseDetails() { return diseaseDetails; }

    // បន្ថែម Method នេះដើម្បីឱ្យត្រូវនឹង InvoicePDFGenerator
    public String getMedicineDetails() {
        return "Diagnosis: " + diagnosis + "\nMedications: " + medicineName + "\nDetails: " + diseaseDetails;
    }

    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }

    // Setters
    public void setBillId(String billId) { this.billId = billId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public void setDiseaseDetails(String diseaseDetails) { this.diseaseDetails = diseaseDetails; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
}