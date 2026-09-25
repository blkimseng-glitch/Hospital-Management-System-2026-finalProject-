package org.example.hms2026.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty phone;
    private final IntegerProperty age;
    private final StringProperty gender;
    private final StringProperty bloodGroup;
    private final StringProperty diagnosis;

    public Patient(String id, String name, String email, String phone, int age, String gender, String bloodGroup, String diagnosis) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        this.age = new SimpleIntegerProperty(age);
        this.gender = new SimpleStringProperty(gender);
        this.bloodGroup = new SimpleStringProperty(bloodGroup);
        this.diagnosis = new SimpleStringProperty(diagnosis);
    }

    // --- Getters for Property ---
    public StringProperty idProperty() { return id; }
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public StringProperty emailProperty() { return email; }
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public StringProperty phoneProperty() { return phone; }
    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }

    public IntegerProperty ageProperty() { return age; }
    public int getAge() { return age.get(); }
    public void setAge(int age) { this.age.set(age); }

    public StringProperty genderProperty() { return gender; }
    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }

    public StringProperty bloodGroupProperty() { return bloodGroup; }
    public String getBloodGroup() { return bloodGroup.get(); }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup.set(bloodGroup); }

    public StringProperty diagnosisProperty() { return diagnosis; }
    public String getDiagnosis() { return diagnosis.get(); }
    public void setDiagnosis(String diagnosis) { this.diagnosis.set(diagnosis); }

    // 💡 បន្ថែម toString() ដើម្បីឱ្យ ComboBox បង្ហាញឈ្មោះអ្នកជំងឺបានត្រឹមត្រូវ
    @Override
    public String toString() {
        return getName();
    }
}