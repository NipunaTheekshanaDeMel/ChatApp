package lk.ijse.playtech.Client;

import animatefx.animation.FadeIn;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import static java.awt.Color.WHITE;
import static lk.ijse.playtech.Client.LoginFormController.users;

import java.util.Base64;

public class RoomFormController extends Thread implements Initializable {

    @FXML
    private JFXButton btnEmoji;

    @FXML
    private JFXButton btnPhoto;
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn;
    @FXML
    public TextField fileChoosePath;
    @FXML
    public ImageView proImage;
    @FXML
    private ImageView imageView;
    @FXML
    public Circle showProPic;

    @FXML
    private AnchorPane emojiPane;
    @FXML
    private VBox vBox;

    private File selectedFile;
    private FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false, toggleProfile = false;
    private Stage stage;
    private List<File> selectedFiles;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;





    private static String username;

    private File file;

    private String finalName;



    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8888);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        username = LoginFormController.name;
        clientName.setText(username);

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost",8888);

                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(),true);

                while (true){
                    String receive = reader.readLine();
                    String[] split = receive.split("~");
                    String name = split[0];
                    String message = split[1];

                    String firstChars = "";
                    if (name.length() > 3) {
                        firstChars = name.substring(0, 3);
                    }
                    if (firstChars.equalsIgnoreCase("img")){
                        String[] imgs = name.split("img");
                        finalName = imgs[1];
                    }
                    if (firstChars.equalsIgnoreCase("img")){
                        if (finalName.equalsIgnoreCase(username)){

                            File receiveFile = new File(message);
                            Image image = new Image(receiveFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(170);
                            imageView.setFitWidth(200);

                            Text text = new Text("~ Me ~");
                            text.getStyleClass().add("nameTextRecieved");
                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView);

                            HBox hBox = new HBox(10);
                            hBox.getStyleClass().add("send-box");
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);

                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);
                            });
                        }else {
                            File receives = new File(message);
                            Image image = new Image(receives.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(170);
                            imageView.setFitWidth(200);

                            Text text = new Text("~ "+finalName+ " ~");

                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView);

                            HBox hBox = new HBox(10);
                            hBox.getStyleClass().add("receive-box");
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);
                            });
                        }
                    }else {
                        if(name.equalsIgnoreCase(username)){
                            TextFlow tempFlow = new TextFlow();
                            Text text = new Text(message);
                            text.setWrappingWidth(200);
                            text.getStyleClass().add("send-text");
                            tempFlow.getChildren().add(text);
                            tempFlow.setMaxWidth(150);

                            Text nameText = new Text("~ Me ~");
                            nameText.getStyleClass().add("nameText");
                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(nameText, tempFlow);

                            HBox hBox = new HBox(12);
                            hBox.getStyleClass().add("send-box");
                            hBox.setMaxWidth(220);
                            hBox.setMaxHeight(50);
                            hBox.getChildren().add(vbox);
                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);
                            });
                        }else {
                            TextFlow tempFlow = new TextFlow();
                            Text text = new Text(message);
                            text.setWrappingWidth(200);
                            text.getStyleClass().add("receive-text");
                            tempFlow.getChildren().add(text);
                            tempFlow.setMaxWidth(150);

                            Text nameText = new Text("~ "+name+" ~");
                            nameText.getStyleClass().add("nameText");
                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(nameText, tempFlow);

                            HBox hBox = new HBox();
                            hBox.getStyleClass().add("receive-box");
                            hBox.setMaxWidth(220);
                            hBox.setMaxHeight(50);
                            hBox.getChildren().add(vbox);

                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);
                            });
                        }
                    }
                    file = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    public void setProfile() {
        for (User user : users) {
            if (LoginFormController.username.equalsIgnoreCase(user.name)) {
                fullName.setText(user.fullName);
                fullName.setOpacity(1);
                email.setText(user.email);
                email.setOpacity(1);
                phoneNo.setText(user.phoneNo);
                gender.setText(user.gender);
            }
        }
    }

    public void handleSendEvent(MouseEvent event) {
        send();
        for(User user : users) {
            System.out.println(user.name);
        }
    }


    public void send() {
        if (!msgField.getText().isEmpty()){
            if (file != null){
                writer.println("img"+username+"~"+file.getPath());
            }else {
                writer.println(username + "~" + msgField.getText());
            }
            msgField.setEditable(true);
            msgField.clear();
        }
    }

    // Changing profile pic

    public boolean saveControl = false;

    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if(LoginFormController.gender.equalsIgnoreCase("Male")) {
            image = new Image("asserts/assert/user.png", false);
        } else {
            image = new Image("asserts/assert/female.png", false);
            proImage.setImage(image);
        }
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(LoginFormController.username);
        connectSocket();
    }

    @FXML
    void btnEmojiOnActon(ActionEvent event) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    @FXML
    void grinningFaceEmojiOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE00");
        emojiPane.setVisible(false);
    }

    @FXML
    void grinningSquintingOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE06");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithOpenHandsOnAction(MouseEvent event) {
        msgField.appendText("\uD83E\uDD17");
        emojiPane.setVisible(false);
    }

    @FXML
    void grinningFaceWithSweatOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE05");
        emojiPane.setVisible(false);
    }

    @FXML
    void faceWithTearsOfJoyOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    @FXML
    void cryingFaceOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE22");
        emojiPane.setVisible(false);
    }

    @FXML
    void sunglassesFaceOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHeartEyesOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHornsOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    @FXML
    void thumbsUpOnAction(MouseEvent event) {
        msgField.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }

    @FXML
    void btnPhotoOnActon(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(msgField.getScene().getWindow());
        if (file != null){
            msgField.setText("Image selected..");
            msgField.setEditable(false);
        }
    }
}
