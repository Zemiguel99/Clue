/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clue.client;

import clue.GameController;
import clue.MissingRoomDuringCreationException;
import clue.action.UnknownActionException;
import clue.tile.NoSuchRoomException;
import clue.tile.NoSuchTileException;
import clue.tile.TileOccupiedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author hungb
 */
public class ClueClient extends Application {
    
    private Scene prevScene;
    private Stage stage;
    
    private int width;
    private int height;
    private String currentWindowMode;
    
    private HashMap<String, String> textureMap;
    
    private int numberOfPlayers;
    private int numberOfAIs;
    
    // Fonts
    private final Font avenirLarge = Font.loadFont(getClass().getResourceAsStream("resources/fonts/Avenir-Book.ttf"), 30);
    private final Font avenirTitle = Font.loadFont(getClass().getResourceAsStream("resources/fonts/Avenir-Book.ttf"), 20);
    private final Font avenirNormal = Font.loadFont(getClass().getResourceAsStream("resources/fonts/Avenir-Book.ttf"), 12);   
    
    // BackgroundFill
    private Background blackFill = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Clue");
        
        width = 1280;
        height = 720;
        
        stage = primaryStage;
        
        VBox menuOptions = new VBox();
        menuOptions.setPadding(new Insets(10));
        menuOptions.setSpacing(8);
        menuOptions.setAlignment(Pos.CENTER);

        addUIControls(menuOptions);

        Scene scene = new Scene(menuOptions, width, height);
        
        //BackgroundImage background = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        menuOptions.setBackground(blackFill);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Creates the main scene and adds the nodes to the given VBox.
     * @param menuOptions 
     */
    private void addUIControls(VBox menuOptions) {
        // Game Title
        Font titleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/ringbearer.ttf"), 80);
        Label gameTitle = new Label("cluE");
        gameTitle.setFont(titleFont);
        gameTitle.setTextFill(Color.WHITE);
        
        // Test new Button design
        MenuItem createGameButton = new MenuItem("Play", avenirTitle);
        createGameButton.setOnMouseClicked(e -> startGameScene(stage));
        
        MenuItem howToPlayButton = new MenuItem("How To Play", avenirTitle);
        howToPlayButton.setOnMouseClicked(e -> howToPlayScene(stage));
        
        MenuItem settingsButton = new MenuItem("Settings", avenirTitle);
        settingsButton.setOnMouseClicked(e -> settingScene(stage));

         menuOptions.getChildren().addAll(gameTitle, createGameButton, howToPlayButton, settingsButton);
    }
    
    /**
     * Creates the game creation scene where the number of human and AI
     * players specified, and can be started.
     * @param stage 
     */
    private void startGameScene(Stage stage) {
        numberOfPlayers = 1;
        
        BorderPane alignmentPane = new BorderPane();
        alignmentPane.setBackground(blackFill);

        GridPane startGameOptions = new GridPane();
        startGameOptions.setAlignment(Pos.CENTER);
        startGameOptions.setBackground(blackFill);
        
        alignmentPane.setCenter(startGameOptions);
        
        // Scene Title
        Label startGameTitle = getLabel("Create Game", avenirTitle);
        
        // Number of Players

        
        Label numberOfPlayersLabel = getLabel("Number of Players", avenirNormal);
        
        HBox players = new HBox();
        players.setAlignment(Pos.CENTER);
        
        MenuItem playersNumber = new MenuItem(String.valueOf(numberOfPlayers), avenirTitle);
        playersNumber.setActiveColor(Color.GREY);
        
        //TextField playersNumber = getTextField(1, false);
        
        MenuItem minusPlayer = new MenuItem("-", avenirTitle);
        minusPlayer.setOnMouseClicked(e -> {
            if (numberOfPlayers > 1) {
            updateNumberOfPlayers(false, playersNumber);
            }
        });
        
        MenuItem addPlayer = new MenuItem("+", avenirTitle);
        addPlayer.setOnMouseClicked(e -> {
            if (numberOfPlayers < 6) {
            updateNumberOfPlayers(true, playersNumber);
            }
        });
        
        players.getChildren().addAll(minusPlayer, playersNumber, addPlayer);
        
        Label aiPlayersLabel = getLabel("Number of AIs", avenirNormal);
        
        HBox AIs = new HBox();
        AIs.setAlignment(Pos.CENTER);
        
        MenuItem AIsNumber = new MenuItem("0", avenirTitle);
        AIsNumber.setActiveColor(Color.GREY);
        
        MenuItem minusAI = new MenuItem("-", avenirTitle);
        minusAI.setOnMouseClicked(e -> {
            if (numberOfAIs > 0) {
                updateNumberOfAIs(false, AIsNumber);
            }
        });
        
        MenuItem addAI = new MenuItem("+", avenirTitle);
        addAI.setOnMouseClicked(e -> {
            if (numberOfAIs < (6 - numberOfPlayers)) {
                updateNumberOfAIs(true, AIsNumber);
            }
        });
        
        AIs.getChildren().addAll(minusAI, AIsNumber, addAI);
        
        // Create Game Instance
        gameInstance game = new gameInstance();
        
        MenuItem startGameButton = new MenuItem("Start Game", avenirTitle);
        startGameButton.setOnMouseClicked(e -> {
            stage.hide();
            try {
                GameController gameController = new GameController(numberOfPlayers, numberOfAIs, "testCsv/tiles1.csv", "testCsv/doors1.csv");
                game.startGame(numberOfPlayers, numberOfAIs, gameController);
            } catch(InterruptedException | UnknownActionException | NoSuchRoomException | NoSuchTileException | MissingRoomDuringCreationException | GameController.TooManyPlayersException | TileOccupiedException ex) {
                
            }
        });

        // Return to menu
        MenuItem returnButton = new MenuItem("Back", avenirTitle);
        returnButton.setOnMouseClicked(e -> stage.setScene(prevScene));
        
        startGameOptions.add(startGameTitle, 0, 0, 2, 1);
        GridPane.setHalignment(startGameTitle, HPos.CENTER);
        
        startGameOptions.add(numberOfPlayersLabel, 0, 1);
        GridPane.setHalignment(numberOfPlayersLabel, HPos.CENTER);
        
        startGameOptions.add(players, 1, 1);
        GridPane.setMargin(players, new Insets(0, 0, 0, 10));
        
        startGameOptions.add(aiPlayersLabel, 0, 2);
        GridPane.setHalignment(aiPlayersLabel, HPos.CENTER);
        
        startGameOptions.add(AIs, 1, 2);
        GridPane.setMargin(AIs, new Insets(0, 0, 0, 10));
        
        startGameOptions.add(startGameButton, 0, 3, 2, 1);
        GridPane.setHalignment(startGameButton, HPos.CENTER);
        
        alignmentPane.setBottom(returnButton);
        BorderPane.setMargin(returnButton, new Insets(0,0,10,20));
        
        Scene scene = new Scene(alignmentPane, width, height);
        prevScene = stage.getScene();
        stage.setScene(scene);
    }
    
    private void updateNumberOfAIs(boolean increase, MenuItem label) {
        if (increase) {
            numberOfAIs++;
        } else {
            numberOfAIs--;
        }
        label.setText(String.valueOf(numberOfAIs));
    }
    
    private void updateNumberOfPlayers(boolean increase, MenuItem label) {
        if (increase) {
            numberOfPlayers++;
        } else {
            numberOfPlayers--;
        }
        label.setText(String.valueOf(numberOfPlayers));
    }
    
    /**
     * Creates the HowToPlay scene. Before setting the stage to the HowToPlay
     * scene, the prevScene has to be set to the currentScene which is used to
     * return to home Scene.
     * @param stage 
     */
    private void howToPlayScene(Stage stage) {
        GridPane howToPlayLayout = new GridPane();
        howToPlayLayout.setAlignment(Pos.CENTER);
        howToPlayLayout.setBackground(blackFill);
        
        Label howToPlayTitle = getLabel("How To Play", avenirTitle);
        
        MenuItem returnButton = new MenuItem("Back", avenirTitle);
        returnButton.setOnMouseClicked(e -> stage.setScene(prevScene));
        
        howToPlayLayout.add(howToPlayTitle, 0, 0);
        howToPlayLayout.add(returnButton, 0, 1);

        Scene scene = new Scene(howToPlayLayout, width, height);
        prevScene = stage.getScene();
        stage.setScene(scene);
    }
    
    /**
     * 
     * @param stage 
     */
    private void settingScene(Stage stage) {        
        BorderPane settingsLayout = new BorderPane();
        settingsLayout.setBackground(blackFill);
        
        BorderPane leftLayout = new BorderPane();
        
        VBox settingsOptions = new VBox();
        
        GridPane textureSettings = new GridPane();
        textureSettings.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        textureSettings.setPadding(new Insets(20,30,20,30));
        GridPane audioSettings = new GridPane();
        audioSettings.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        audioSettings.setPadding(new Insets(20,30,20,30));
        GridPane creditsPane = new GridPane();
        creditsPane.setBackground(new Background(new BackgroundFill(Color.rgb(50, 50, 50), CornerRadii.EMPTY, Insets.EMPTY)));
        creditsPane.setPadding(new Insets(20,30,20,30));
        
        
        Label settingsTitle = getLabel("Settings", avenirLarge);
        
        MenuItem texturesButton = new MenuItem("Textures", avenirTitle);
        texturesButton.setOnMouseClicked(e -> {
            settingsLayout.setCenter(textureSettings);
            BorderPane.setMargin(textureSettings, new Insets(0,50,50,0));
        });
        
        MenuItem audioButton = new MenuItem("Audio", avenirTitle);
        audioButton.setOnMouseClicked(e -> {
            settingsLayout.setCenter(audioSettings);
            BorderPane.setMargin(audioSettings, new Insets(0,50,50,0));
        });
        
        MenuItem creditsButton = new MenuItem("Credits", avenirTitle);
        creditsButton.setOnMouseClicked(e -> {
            settingsLayout.setCenter(creditsPane);
            BorderPane.setMargin(creditsPane, new Insets(0,50,50,0));
        });
        
        MenuItem returnButton = new MenuItem("Back", avenirTitle);
        returnButton.setOnMouseClicked(e -> stage.setScene(prevScene));
        
        // Textures
        
        textureSettingsScene(textureSettings);
        
        // Audio
        
        audioSettingsScene(audioSettings);
        
        // Credits
        
        creditsScene(creditsPane);
        
        // adding nodes to panes
        
        settingsOptions.getChildren().addAll(texturesButton, audioButton, creditsButton);
        settingsOptions.setPadding(new Insets(0,10,0,20));
        
        settingsLayout.setLeft(leftLayout);
        
        settingsLayout.setTop(settingsTitle);
        BorderPane.setAlignment(settingsTitle, Pos.CENTER_LEFT);
        BorderPane.setMargin(settingsTitle, new Insets(10,0,10,20));
        
        leftLayout.setTop(settingsOptions);
        leftLayout.setBottom(returnButton);
        BorderPane.setMargin(returnButton, new Insets(0,0,10,20));
        
        Scene scene = new Scene(settingsLayout, width, height);
        prevScene = stage.getScene();
        stage.setScene(scene);
    }
    
    /**
     * 
     * @param layout
     * @return 
     */
    private GridPane creditsScene(GridPane layout) {      
        LinkedHashMap <String, String> credits = new LinkedHashMap<>();
        credits.put("Produced By", "Big Sage Productions");
        credits.put("Project Manager", "Jak and Whip");
        credits.put("AI", "Jose");
        credits.put("Lead Programmer", "Steve");
        credits.put("Programmer", "Mathew");
        credits.put(" ", "Jak");
        credits.put("  ", "Jose");
        credits.put("Artist", "Hung");
        credits.put("UX Design","Hung");
        credits.put("Composer", "What Music XD");
        credits.put("Tester", "Your Nan");
        credits.put("Yasuo", "DAB");
        credits.put("Special Thanks to", "Big Sage");
        
        int index = 1;
        for(Map.Entry<String, String> entry: credits.entrySet()) {
            Label title = getLabel(entry.getKey(), avenirTitle);
            Label credit = getLabel(entry.getValue(), avenirTitle);
            
            layout.add(title, 0, index);
            layout.add(credit, 1, index);
            GridPane.setMargin(credit, new Insets(0, 0, 0, 50));
            index++;
        }
        
        layout.setAlignment(Pos.CENTER);
        
        return layout;
    }
    
    /**
     * 
     * @param layout
     * @return 
     */
    private GridPane audioSettingsScene(GridPane layout) {
        Label masterLabel = getLabel("Master Volume", avenirTitle);
        
        Slider masterVolume = new Slider(0, 100, 100);
        masterVolume.setMaxWidth(1000);
        masterVolume.setBlockIncrement(10);
        Label masterVolumeShow = getLabel("", avenirTitle);
        masterVolumeShow.setText("100");
                
        masterVolume.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                masterVolumeShow.setText(String.valueOf((int) masterVolume.getValue()));
            }
        });
        
        layout.add(masterLabel, 0, 0);
        GridPane.setMargin(masterLabel, new Insets(0, 10, 0, 0));
        layout.add(masterVolume, 1, 0);
        layout.add(masterVolumeShow, 2, 0);
        GridPane.setMargin(masterVolumeShow, new Insets(0, 0, 0, 10));
        
        return layout;
    }
    
    /**
     * 
     * @param layout
     * @return 
     */
    private GridPane textureSettingsScene(GridPane layout) {
        textureMap = new HashMap<>();
        
        ExtensionFilter imageFormats = new ExtensionFilter("Image files", "*.jpg", "*.png", "*.jpeg");
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFormats);
        fileChooser.setTitle("Select");
        
        Label board = getLabel("Board", avenirTitle);
        TextField boardFilePath = getTextField(40, false);
        
        MenuItem selectBoardFile = new MenuItem("Choose", avenirTitle);
        selectBoardFile.setOnMouseClicked(e -> {
            File boardTexture = fileChooser.showOpenDialog(stage);
            boardFilePath.setText(boardTexture.getAbsolutePath());
            textureMap.put("BoardTexture",boardTexture.getAbsolutePath());
        });
        
        layout.add(board, 0, 0);
        GridPane.setMargin(board, new Insets(0, 10, 0, 0));
        layout.add(boardFilePath, 1, 0);
        layout.add(selectBoardFile, 2, 0);
        GridPane.setMargin(selectBoardFile, new Insets(0, 0, 0, 10));

        for (int i=1; i < 7; i++) {
            Label character = getLabel("Character " + i, avenirTitle);
            TextField characterFilePath = getTextField(40, false);
            
            final String propertiesCharacter = "Character"+i+"Texture";
            
            MenuItem selectCharacterFile = new MenuItem("Choose", avenirTitle);
            selectCharacterFile.setOnMouseClicked(e -> {
                File characterTexture = fileChooser.showOpenDialog(stage);
                characterFilePath.setText(characterTexture.getAbsolutePath());
                textureMap.put(propertiesCharacter,characterTexture.getAbsolutePath());
            });
            
            layout.add(character, 0, i);
            GridPane.setMargin(character, new Insets(0, 10, 0, 0));
            layout.add(characterFilePath, 1, i);
            layout.add(selectCharacterFile, 2, i);
            GridPane.setMargin(selectCharacterFile, new Insets(0, 0, 0, 10));
        }
        
        for (int i=1; i < 7; i++) {
            Label weapon = getLabel("Weapon " + i, avenirTitle);
            TextField weaponFilePath = getTextField(40, false);
            
            final String propertiesWeapon = "Weapon"+i+"Texture";
            MenuItem selectWeaponFile = new MenuItem("Choose", avenirTitle);
            selectWeaponFile.setOnMouseClicked(e -> {
                File weaponTexture = fileChooser.showOpenDialog(stage);
                weaponFilePath.setText(weaponTexture.getAbsolutePath());
                textureMap.put(propertiesWeapon,weaponTexture.getAbsolutePath());
            });
            
            layout.add(weapon, 0, i + 6);
            GridPane.setMargin(weapon, new Insets(0, 10, 0, 0));
            layout.add(weaponFilePath, 1, i + 6);
            layout.add(selectWeaponFile, 2, i + 6);
            GridPane.setMargin(selectWeaponFile, new Insets(0, 0, 0, 10));
        }
        
        MenuItem applyChanges = new MenuItem("Apply", avenirTitle);
        applyChanges.setOnMouseClicked(e -> saveProperties());
        
        layout.add(applyChanges, 2, 14);
        GridPane.setMargin(applyChanges, new Insets(0, 0, 0, 15));
        
        return layout;
    }
    
    // TODO: NullPointerException - can't load properties file
    private boolean saveProperties() {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/config.properties");
            String path = getClass().getResource("assets/config.properties").toString();
            File file = new File("assets/config.properties");
            try (FileInputStream fileInput = new FileInputStream(path)) {
                Properties properties = new Properties();
                properties.load(stream);
                
                textureMap.entrySet().forEach((entry) -> {
                    properties.put(entry.getKey(), entry.getValue());
                });
                
                Prompt confirmPrompt = new Prompt("Changes Saved");
                confirmPrompt.showAndWait();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns a TextField with specified settings, the column size and can the
     * content be edited.
     * @param columnCount
     * @param editable
     * @return 
     */
    private TextField getTextField(int columnCount, boolean editable) {
        TextField filePath = new TextField();
        filePath.setPrefColumnCount(columnCount);
        filePath.setEditable(editable);
        return filePath;        
    }
    
    /**
     * Returns a Label with the given text and font.
     * @param text
     * @param font
     * @return 
     */
    private Label getLabel(String text, Font font) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(font);
        return label;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
