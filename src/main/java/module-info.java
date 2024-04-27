module spacca.spacca {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires java.desktop;


    opens spacca.spacca to javafx.fxml;
    exports spacca.spacca;
}