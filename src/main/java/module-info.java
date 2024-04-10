module spacca.spacca {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens spacca.spacca to javafx.fxml;
    exports spacca.spacca;
}