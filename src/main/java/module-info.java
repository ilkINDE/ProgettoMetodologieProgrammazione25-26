module it.unicam.cs.mpgc.rpg123388 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    exports it.unicam.cs.mpgc.rpg123388.view;
    opens it.unicam.cs.mpgc.rpg123388.view to javafx.fxml;
}