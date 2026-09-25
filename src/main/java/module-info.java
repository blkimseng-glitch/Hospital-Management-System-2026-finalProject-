module org.example.hms2026 {
    requires javafx.controls;
    requires javafx.fxml;

    // 🟢 ប្រកាសអាន iText 7 Modules ឱ្យត្រូវតាមឈ្មោះ Automatic Module របស់វា
//    requires itextpdf.kernel;
//    requires itextpdf.layout;
//    requires itextpdf.io;

    opens org.example.hms2026 to javafx.fxml;
    opens org.example.hms2026.controller_admin to javafx.fxml;
    opens org.example.hms2026.controller_auth to javafx.fxml;
    opens org.example.hms2026.controller_doctor to javafx.fxml;
    opens org.example.hms2026.controller_patient to javafx.fxml;

    opens org.example.hms2026.model to javafx.base;

    exports org.example.hms2026;
}