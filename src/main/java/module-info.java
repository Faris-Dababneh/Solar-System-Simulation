module project.sss {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens project.sss to javafx.fxml;
    exports project.sss;
}