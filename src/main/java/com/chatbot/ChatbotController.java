package com.chatbot;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;

public class ChatbotController {
    @FXML
    private VBox chatContainer;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView logoImage;

    private final ChatbotService chatbotService = new ChatbotService();

    @FXML
    public void initialize() {
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImage.setImage(logo);
            setupLogoAnimation();
        } catch (Exception e) {
            logoImage.setStyle(logoImage.getStyle() + "; -fx-background-color: #f3e5f5;");
        }

        addWelcomeMessage("Welcome to the Disaster Relief Assistant! How can I help you today?");
        addWelcomeMessage("\uD83D\uDCA1 You can ask about emergency helplines, shelter locations, disaster statistics, and medical aid information.");

        sendButton.setOnAction(event -> sendMessage());
        messageInput.setOnAction(event -> sendMessage());
    }

    private void setupLogoAnimation() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.2), logoImage);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.08);
        scaleTransition.setToY(1.08);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1.8), logoImage);
        rotateTransition.setFromAngle(-3);
        rotateTransition.setToAngle(3);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);

        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, rotateTransition);
        parallelTransition.play();

        logoImage.setOnMouseEntered(e -> {
            parallelTransition.pause();

            RotateTransition spinTransition = new RotateTransition(Duration.seconds(0.4), logoImage);
            spinTransition.setFromAngle(0);
            spinTransition.setToAngle(360);
            spinTransition.setCycleCount(1);

            ScaleTransition bounceTransition = new ScaleTransition(Duration.seconds(0.4), logoImage);
            bounceTransition.setFromX(1.0);
            bounceTransition.setFromY(1.0);
            bounceTransition.setToX(1.25);
            bounceTransition.setToY(1.25);
            bounceTransition.setAutoReverse(true);
            bounceTransition.setCycleCount(2);

            ParallelTransition hoverAnimation = new ParallelTransition(spinTransition, bounceTransition);
            hoverAnimation.play();

            hoverAnimation.setOnFinished(event -> parallelTransition.play());
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addUserMessage(message);
            messageInput.clear();

            VBox loadingBubble = createLoadingBubble();
            chatContainer.getChildren().add(loadingBubble);
            scrollToBottom();

            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                chatContainer.getChildren().remove(loadingBubble);
                String response = chatbotService.processUserInput(message);
                addBotMessage(response);
            });
            delay.play();
        }
    }

    private void addUserMessage(String message) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setPadding(new Insets(5, 10, 5, 50));

        Text text = new Text(message);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("System", 16));

        TextFlow bubble = new TextFlow(text);
        bubble.setStyle("-fx-background-color: #ff8c00; -fx-background-radius: 20; -fx-padding: 12 20; -fx-effect: dropshadow(gaussian, rgba(255,140,0,0.3), 5, 0, 0, 2);");

        container.getChildren().add(bubble);
        chatContainer.getChildren().add(container);
        scrollToBottom();
    }

    private void addWelcomeMessage(String message) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(5, 50, 5, 10));

        Text text = new Text(message);
        text.setFill(Color.web("#175ca0"));
        text.setFont(Font.font("System", 16));

        TextFlow bubble = new TextFlow(text);
        bubble.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 20; -fx-padding: 12 20; -fx-border-color: #bbdefb; -fx-border-radius: 20; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(23,92,160,0.1), 5, 0, 0, 2);");

        container.getChildren().add(bubble);
        chatContainer.getChildren().add(container);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(5, 50, 5, 10));

        VBox messageBox = new VBox(2);
        messageBox.setAlignment(Pos.CENTER_LEFT);

        Text text = new Text(message);
        text.setFill(Color.web("#175ca0"));
        text.setFont(Font.font("System", 16));

        TextFlow bubble = new TextFlow(text);
        bubble.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 20; -fx-padding: 12 20; -fx-border-color: #bbdefb; -fx-border-radius: 20; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(23,92,160,0.1), 5, 0, 0, 2);");

        HBox ratingBox = new HBox(5); // Reduced spacing between buttons
        ratingBox.setAlignment(Pos.CENTER_LEFT);
        ratingBox.setPadding(new Insets(2, 0, 0, 15)); // Reduced top padding, increased left padding

        Button thumbsUp = createRatingButton("👍", "Rate as helpful");
        Button thumbsDown = createRatingButton("👎", "Rate as not helpful");

        thumbsUp.setOnAction(e -> handleRating(true, message));
        thumbsDown.setOnAction(e -> handleRating(false, message));

        ratingBox.getChildren().addAll(thumbsUp, thumbsDown);
        messageBox.getChildren().addAll(bubble, ratingBox);
        container.getChildren().add(messageBox);
        chatContainer.getChildren().add(container);
        scrollToBottom();
    }

    private Button createRatingButton(String emoji, String tooltipText) {
        Button button = new Button(emoji);
        button.setStyle("-fx-background-color: rgba(255,140,0,0.05); -fx-border-color: #ffbe99; -fx-border-width: 1; -fx-border-radius: 15; -fx-cursor: hand; -fx-font-size: 14px; -fx-text-fill: #ff8c00; -fx-padding: 5 10;");
        button.setTooltip(new Tooltip(tooltipText));

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: rgba(255,140,0,0.15); -fx-border-color: #ff8c00; -fx-border-width: 1; -fx-border-radius: 15; -fx-cursor: hand; -fx-font-size: 14px; -fx-text-fill: #ff8c00; -fx-padding: 5 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: rgba(255,140,0,0.05); -fx-border-color: #ffbe99; -fx-border-width: 1; -fx-border-radius: 15; -fx-cursor: hand; -fx-font-size: 14px; -fx-text-fill: #ff8c00; -fx-padding: 5 10;"));
        
        return button;
    }

    private void handleRating(boolean isPositive, String message) {
        System.out.println("Message: " + message);
        System.out.println("Rating: " + (isPositive ? "Positive" : "Negative"));

        String feedback = isPositive ? "Thank you for your positive feedback!" : "Thank you for your feedback. We'll work to improve our responses.";
        addWelcomeMessage(feedback); // Use welcome message style for feedback to avoid rating buttons on feedback
    }

    private VBox createLoadingBubble() {
        VBox loadingBox = new VBox();
        loadingBox.setAlignment(Pos.CENTER_LEFT);
        loadingBox.setPadding(new Insets(5, 50, 5, 10));

        HBox bubble = new HBox(10); // 10 pixels spacing between elements
        bubble.setAlignment(Pos.CENTER_LEFT);
        bubble.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 20; -fx-padding: 12 20; -fx-border-color: #bbdefb; -fx-border-radius: 20; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(23,92,160,0.1), 5, 0, 0, 2);");

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(20, 20);
        spinner.setStyle("-fx-progress-color: #175ca0;");

        Text loadingText = new Text("Getting information...");
        loadingText.setFill(Color.web("#175ca0"));
        loadingText.setFont(Font.font("System", 16));


        bubble.getChildren().addAll(spinner, loadingText);
        loadingBox.getChildren().add(bubble);
        return loadingBox;
    }

    private void scrollToBottom() {
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    @FXML
    private void handleSuggestionClick(javafx.scene.input.MouseEvent event) {
        HBox clickedBubble = (HBox) event.getSource();
        Text text = (Text) clickedBubble.getChildren().get(0);
        String suggestion = text.getText();

        messageInput.setText(suggestion);

        sendMessage();
    }
}
