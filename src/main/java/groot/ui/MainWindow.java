package groot.ui;

import java.util.Objects;

import groot.Groot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls the main Groot window.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image grootImage = loadImage("/images/DaDuke.png");

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Groot groot;

    /**
     * Binds the scroll position to the growing dialog container.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the Groot instance that processes user commands.
     *
     * @param groot Groot application instance.
     */
    public void setGroot(Groot groot) {
        this.groot = groot;
    }

    /**
     * Submits the current user input and displays both sides of the conversation.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = groot.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getGrootDialog(response, grootImage));
        userInput.clear();
    }

    /**
     * Loads a required image resource.
     *
     * @param resourcePath Absolute classpath resource path.
     * @return Loaded image.
     */
    private Image loadImage(String resourcePath) {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
    }
}
