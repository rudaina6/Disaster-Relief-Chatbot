module com.chatbot {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires io.github.cdimascio.dotenv.java;


    opens com.chatbot to javafx.fxml;
    exports com.chatbot;
}