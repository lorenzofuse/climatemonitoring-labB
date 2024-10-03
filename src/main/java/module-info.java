module climate.monitoring {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.rmi;
    requires java.sql;

    // Esporta il pacchetto client per consentire l'accesso da parte di altri moduli
    exports com.climatemonitoring.client;
    exports com.climatemonitoring.shared;
    exports com.climatemonitoring.server;

    // Apre il pacchetto client al modulo javafx.fxml per permettere il riflesso
    opens com.climatemonitoring.client to javafx.fxml;
}
