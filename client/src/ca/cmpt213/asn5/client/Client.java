package ca.cmpt213.asn5.client;

/**
 * Client class allows the user to pick either to display all tokimons, display 1, edit tokimon, remove tokimon, and add tokimon
 * Has seperate event handlers for each time the user clicks the submit button after inputting data
 */

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Text;

public class Client extends Application {

    private VBox vBox = new VBox(200);
    private List<TokimonDisplay> displayObjects = new ArrayList<>();

    // Menu bar
    // Gives the user 6 choices to select from and has a separate event handler for each choice
    public void menu(Stage primaryStage) {

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem displayAllToki = new Menu("Display All");
        MenuItem displayOneToki = new Menu("Display One");
        MenuItem addToki = new Menu("Add New");
        MenuItem editToki = new Menu("Edit");
        MenuItem deleteToki = new Menu("Delete");
        MenuItem exit = new Menu("Exit");

        menu.getItems().addAll(displayAllToki,displayOneToki,addToki,editToki,deleteToki,exit);

        menuBar.getMenus().add(menu);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        vBox.getChildren().addAll(borderPane);

        // User selects the choice of displaying all tokimons
        displayAllToki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage subStage =  new Stage();
                subStage.setTitle("Display All Tokimons");

                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);
                GridPane paneOfRectangles = new GridPane();

                try {
                    URL url = new URL("http://localhost:8080/tokimon/all");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    System.out.println(connection.getResponseCode());
                    String output = br.readLine();

                    JsonProcessor processor = new JsonProcessor();
                    processor.processJsonString(output);
                    displayObjects = processor.objects;
                    List<Rectangle> rectangles = displayRectangles(displayObjects);


                    for(int i = 0; i < rectangles.size(); i++) {
                        paneOfRectangles.add(rectangles.get(i),i,0);
                        paneOfRectangles.setVgap(20);
                        paneOfRectangles.setHgap(20);
                        Label name = new Label(displayObjects.get(i).getName());
                        paneOfRectangles.add(name,i,1);
                        paneOfRectangles.setAlignment(Pos.CENTER);
                    }
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Scene subScene = new Scene(paneOfRectangles,600,600);
                subStage.setScene(subScene);
                subStage.show();

            }
        });

        // User selects the choice of displaying one tokimon
        displayOneToki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage subStage =  new Stage();
                subStage.setTitle("Display One Toki");

                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Label tokiID = new Label("ID of Tokimon To Search For: ");
                TextField idField = new TextField();
                Button submitButton = new Button("Submit Information");

                GridPane gridpane = new GridPane();
                gridpane.setVgap(20);
                gridpane.setHgap(20);

                gridpane.add(tokiID, 0, 0);
                gridpane.add(idField, 1, 0);

                gridpane.add(submitButton,0,1);
                gridpane.setAlignment(Pos.CENTER);

                submitButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                        Stage displayStage = new Stage();
                        GridPane paneOfRectangle = new GridPane();
                        try {
                            URL url = new URL("http://localhost:8080/tokimon/" + idField.getText());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();

                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            System.out.println(connection.getResponseCode());
                            String output = br.readLine();
                            JsonProcessor processor = new JsonProcessor();
                            processor.processJsonString(output);
                            displayObjects = processor.objects;

                            long id = Long.parseLong(idField.getText());
                            Rectangle tokiRectangle = displayOneRectangle(displayObjects,id);
                            String nameOfToki = null;
                            for(int i = 0; i < displayObjects.size(); i++) {
                                if(displayObjects.get(i).getTid() == id) {
                                    nameOfToki = displayObjects.get(i).getName();
                                }
                            }
                            Label name = new Label(nameOfToki);
                            paneOfRectangle.add(tokiRectangle,0,0);
                            paneOfRectangle.setVgap(20);
                            paneOfRectangle.setHgap(20);
                            paneOfRectangle.add(name,0,1);
                            paneOfRectangle.setAlignment(Pos.CENTER);
                            connection.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene subScene = new Scene(paneOfRectangle,400,400);
                        displayStage.setScene(subScene);
                        displayStage.show();
                    }
                });

                Scene subScene = new Scene(gridpane,400,400);
                subStage.setScene(subScene);
                subStage.show();
            }
        });

        // User selects the choice of adding a new tokimon to the database
        addToki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage subStage =  new Stage();
                subStage.setTitle("Add A New Tokimon");
                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Label nameLabel = new Label("Name: ");
                Label weightLabel = new Label("Weight: ");
                Label heightLabel = new Label("Height: ");
                Label abilityLabel = new Label("Ability: ");
                Label strengthLabel = new Label("Strength: ");
                Label colourLabel = new Label("Colour: ");
                Label fillOutEverything = new Label("Please fill out all of the fields");

                TextField nameField = new TextField();
                TextField weightField = new TextField();
                TextField heightField = new TextField();
                TextField abilityField = new TextField();
                TextField strengthField = new TextField();
                TextField colourField = new TextField();

                Button submitButton = new Button("Submit Information");

                nameLabel.setPadding(new Insets(0, 0, 0, 10));
                weightLabel.setPadding(new Insets(0, 0, 0, 10));
                heightLabel.setPadding(new Insets(0, 0, 0, 10));
                abilityLabel.setPadding(new Insets(0, 0, 0, 10));
                strengthLabel.setPadding(new Insets(0, 0, 0, 10));
                colourLabel.setPadding(new Insets(0, 0, 0, 10));

                GridPane gridpane = new GridPane();
                gridpane.setVgap(20);
                gridpane.setHgap(20);

                gridpane.add(nameLabel, 0, 0);
                gridpane.add(weightLabel, 0, 1);
                gridpane.add(heightLabel, 0, 2);
                gridpane.add(abilityLabel, 0, 3);
                gridpane.add(strengthLabel, 0, 4);
                gridpane.add(colourLabel, 0, 5);

                gridpane.add(nameField, 1, 0);
                gridpane.add(weightField, 1, 1);
                gridpane.add(heightField, 1, 2);
                gridpane.add(abilityField, 1, 3);
                gridpane.add(strengthField, 1, 4);
                gridpane.add(colourField, 1, 5);

                gridpane.add(submitButton,0,7);
                gridpane.add(fillOutEverything,2,7);
                gridpane.setAlignment(Pos.CENTER);

                submitButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                       try {
                           URL url = new URL("http://localhost:8080/tokimon/add");
                           postToServer(url, nameField, weightField, heightField, abilityField, strengthField, colourField);
                           Label newTokiLabel = new Label("Tokimon Successfully Added!");
                           gridpane.add(newTokiLabel,1,7);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                    }
                });

                Scene subScene = new Scene(gridpane,600,400);
                subStage.setScene(subScene);
                subStage.show();

            }
        });

        // User selects the choice of editing an existing tokimon
        editToki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage subStage =  new Stage();
                subStage.setTitle("Edit A Tokimon");

                subStage.initModality(Modality.WINDOW_MODAL);
                subStage.initOwner(primaryStage);

                Label tokiID = new Label("ID of Tokimon To Edit: ");
                TextField idField = new TextField();
                Button submitButton = new Button("Submit Information");

                GridPane gridpane = new GridPane();
                gridpane.setVgap(20);
                gridpane.setHgap(20);
                gridpane.add(tokiID, 0, 0);
                gridpane.add(idField, 1, 0);

                gridpane.add(submitButton,0,1);
                gridpane.setAlignment(Pos.CENTER);


                submitButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Stage editInfoScreen = new Stage();
                        editInfoScreen.setTitle("New Edits");

                        Label nameLabel = new Label("Name: ");
                        Label weightLabel = new Label("Weight: ");
                        Label heightLabel = new Label("Height: ");
                        Label abilityLabel = new Label("Ability: ");
                        Label strengthLabel = new Label("Strength: ");
                        Label colourLabel = new Label("Colour: ");
                        Label fillOutEverything = new Label("Please fill out all of the fields");

                        TextField nameField = new TextField();
                        TextField weightField = new TextField();
                        TextField heightField = new TextField();
                        TextField abilityField = new TextField();
                        TextField strengthField = new TextField();
                        TextField colourField = new TextField();

                        Button submitButton = new Button("Submit Information");

                        nameLabel.setPadding(new Insets(0, 0, 0, 10));
                        weightLabel.setPadding(new Insets(0, 0, 0, 10));
                        heightLabel.setPadding(new Insets(0, 0, 0, 10));
                        abilityLabel.setPadding(new Insets(0, 0, 0, 10));
                        strengthLabel.setPadding(new Insets(0, 0, 0, 10));
                        colourLabel.setPadding(new Insets(0, 0, 0, 10));

                        GridPane gridpane = new GridPane();
                        gridpane.setVgap(20);
                        gridpane.setHgap(20);

                        gridpane.add(nameLabel, 0, 0);
                        gridpane.add(weightLabel, 0, 1);
                        gridpane.add(heightLabel, 0, 2);
                        gridpane.add(abilityLabel, 0, 3);
                        gridpane.add(strengthLabel, 0, 4);
                        gridpane.add(colourLabel, 0, 5);

                        gridpane.add(nameField, 1, 0);
                        gridpane.add(weightField, 1, 1);
                        gridpane.add(heightField, 1, 2);
                        gridpane.add(abilityField, 1, 3);
                        gridpane.add(strengthField, 1, 4);
                        gridpane.add(colourField, 1, 5);

                        gridpane.add(submitButton,0,7);
                        gridpane.add(fillOutEverything,2,7);
                        gridpane.setAlignment(Pos.CENTER);

                        submitButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try {
                                    URL url = new URL("http://localhost:8080/tokimon/change/" + idField.getText());
                                    postToServer(url, nameField, weightField, heightField, abilityField, strengthField, colourField);
                                    Label newTokiLabel = new Label("Tokimon with ID " + idField.getText() + "Successfully Edited!");
                                    gridpane.add(newTokiLabel,1,7);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Scene subScene = new Scene(gridpane,600,400);
                        editInfoScreen.setScene(subScene);
                        editInfoScreen.show();
                    }
                });

                Scene subScene = new Scene(gridpane,400,400);
                subStage.setScene(subScene);
                subStage.show();
            }
        });

        // User selects the choice of deleting an existing tokimon from the database
        deleteToki.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage subStage =  new Stage();
                subStage.setTitle("Delete A Tokimon");

                Label tokiID = new Label("ID of Tokimon To Delete: ");
                TextField idField = new TextField();
                Button submitButton = new Button("Submit Information");

                GridPane gridpane = new GridPane();
                gridpane.setVgap(20);
                gridpane.setHgap(20);

                gridpane.add(tokiID, 0, 0);
                gridpane.add(idField, 1, 0);

                gridpane.add(submitButton,0,1);
                gridpane.setAlignment(Pos.CENTER);

                submitButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            URL url = new URL("http://localhost:8080/tokimon/" + idField.getText());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("DELETE");
                            connection.connect();

                            Label deleteLabel = new Label("Tokimon with the ID: " + idField.getText()
                                    + " has been deleted");

                            System.out.println(connection.getResponseCode());
                            connection.disconnect();
                            gridpane.add(deleteLabel,0,2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Scene subScene = new Scene(gridpane,400,400);
                subStage.setScene(subScene);
                subStage.show();

            }
        });

        // // User selects the choice of exiting the application
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });

    }

    // Posts a new tokimon to the server by putting in the approiate fields of name, height, colour, ability, strength, weight
    // Prints out the status code
    private void postToServer(URL url, TextField nameField, TextField weightField, TextField heightField,
                              TextField abilityField, TextField strengthField, TextField colourField) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

        wr.write("{\"name\":\"" + nameField.getText() + "\",\"weight\":" + weightField.getText()
                + ",\"height\":" + heightField.getText() + ",\"ability\":\"" + abilityField.getText()
                + "\",\"strength\":" + strengthField.getText() + ",\"colour\":\"" + colourField.getText() + "\"}");
        wr.flush();
        wr.close();
        connection.connect();
        System.out.println(connection.getResponseCode());
        connection.disconnect();
    }

    // Stores all rectangles
    private List<Rectangle> displayRectangles(List<TokimonDisplay> tokimonDisplays) {
        List<Rectangle> rectangles = new ArrayList<>();
        for(int i = 0; i < tokimonDisplays.size(); i++) {
            rectangles.add(createRectangles(tokimonDisplays, i));
        }
        return rectangles;
    }

    // Creates 1 rectangle corresponding to the tokimon user wants to view
    private Rectangle displayOneRectangle(List<TokimonDisplay> tokimonDisplays, long tid) {
        for(int i = 0; i < tokimonDisplays.size(); i++) {
            if(tokimonDisplays.get(i).getTid() == tid) {
                return createRectangles(tokimonDisplays, i);
            }
        }
        return null;
    }

    // Creates rectangles corresponding to the tokimons
    private Rectangle createRectangles(List<TokimonDisplay> tokimonDisplays, int i) {
        int weight = (int)tokimonDisplays.get(i).getWeight();
        int height = (int)tokimonDisplays.get(i).getHeight() * 10;
        if(weight < 50) {
            weight *= 10;
        }
        if(height < 50) {
            height *= 10;
        }

        Rectangle toki = new Rectangle(0,0,weight,height);
        if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("red")) {
            toki.setFill(Color.RED);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("yellow")) {
            toki.setFill(Color.YELLOW);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("blue")) {
            toki.setFill(Color.BLUE);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("green")) {
            toki.setFill(Color.GREEN);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("white")) {
            toki.setFill(Color.WHITE);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("purple")) {
            toki.setFill(Color.PURPLE);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("orange")) {
            toki.setFill(Color.ORANGE);
        } else if(tokimonDisplays.get(i).getColour().equalsIgnoreCase("brown")) {
            toki.setFill(Color.BROWN);
        } else {
            toki.setFill(Color.BLACK);
        }
        return toki;
    }


    @Override
    public void start(Stage primaryStage){
        Text greeting = new Text(0,0,"Welcome to the Tokimon Database!");
        greeting.setFont(new Font("SansSerif",15));
        HBox greetingBox = new HBox(20,greeting);
        greetingBox.setAlignment(Pos.TOP_CENTER);
        menu(primaryStage);
        vBox.getChildren().add(greetingBox);

        Scene scene = new Scene(vBox, 300, 300);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tokimon DataBase");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
