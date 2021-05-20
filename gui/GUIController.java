package ru.nsu.fit.minesweeper.gui;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.minesweeper.CellState;
import ru.nsu.fit.minesweeper.MineState;
import ru.nsu.fit.minesweeper.Minesweeper;

import java.io.*;
import java.util.Timer;

public class GUIController extends Application {

    final int SIG_EXITGAME = 0;
    final int SIG_NEWGAME = 1;
    public static void game(String[] args) {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {//TODO: change it
        //primaryStage.show();
        //Minesweeper model = new Minesweeper();
        primaryStage.setTitle("Minesweeper");
        primaryStage.setWidth(400);
        primaryStage.setHeight(400);
        //InputStream iconStream= getClass().getResourceAsStream("src/images/icon.jpg");
        //Image image = new Image(iconStream);
        Button button = new Button("Start");
        button.setOnAction(actionEvent -> {
            //Alert alert = new Alert(Alert.AlertType.INFORMATION, "igra nachalas");
            doGame(primaryStage);
            //alert.showAndWait();
        });
        Scene primaryScene = new Scene(button);
        primaryStage.setScene(primaryScene);

        primaryStage.show();
    }
    /*void qwrety(SString a, String b) {
        a.data = b;
    }*/
    void showGameField(Minesweeper model, Button[][] field) {
        for (int i = 0; i < model.getLengthField(); i++) {
            for (int j = 0; j < model.getLengthField(); j++) {

                if (model.getUserField()[i][j] == CellState.FLAG){
                    //System.out.print("f");
                    ((Rectangle)((StackPane)field[i][j].getGraphic()).getChildren().get(0)).setFill(Color.YELLOW);
                }

                if (model.getUserField()[i][j] == CellState.KNOWN) {
                    int count = 0;
                    count = model.getUserMineField()[i][j];
                    //System.out.print(count);
                    int colors[] = new int[3];
                    switch (count){
                        case 0:
                            colors[0] = 255; colors[1] = 255; colors[2] = 255; break;
                        case 1:
                            colors[0] = 0xe8; colors[1] = 0xd8; colors[2] = 255; break;
                        case 2:
                            colors[0] = 0xcf; colors[1] = 0xb1; colors[2] = 0xff; break;
                        case 3:
                            colors[0] = 0xb3; colors[1] = 0x8b; colors[2] = 0xff; break;
                        case 4:
                            colors[0] = 0x92; colors[1] = 0x65; colors[2] = 0xff; break;
                        case 5:
                            colors[0] = 0x67; colors[1] = 0x73; colors[2] = 0xff; break;
                        case 6:
                            colors[0] = 0x00; colors[1] = 0x00; colors[2] = 0xff; break;
                        case 7:
                            colors[0] = 0x25; colors[1] = 0x10; colors[2] = 0xa3; break;
                        case 8:
                            colors[0] = 0x0; colors[1] = 0x0; colors[2] = 0x0; break;
                    }

                    ((Rectangle)((StackPane)field[i][j].getGraphic()).getChildren().get(0)).setFill(Color.rgb(colors[0], colors[1], colors[2]));
                    ((Text)((StackPane)field[i][j].getGraphic()).getChildren().get(1)).setText(String.valueOf(count));
                }
                if (model.getUserField()[i][j] == CellState.UNKNOWN) {
                    ((Rectangle)((StackPane)field[i][j].getGraphic()).getChildren().get(0)).setFill(Color.GREY);
                }
                if (model.getUserMineField()[i][j] == -10)
                    ((Rectangle)((StackPane)field[i][j].getGraphic()).getChildren().get(0)).setFill(Color.RED);
            }
            //System.out.println();
        }
    }

    void doUnclickable(Button[][] field) {
        for (Button[] a : field) {
            for (Button b : a) {
                b.setOnMouseClicked(null);
            }
        }
    }

    int doGame(Stage stage) {
        Minesweeper model = new Minesweeper();
        //TextField forLengthField = new TextField();
        //Alert alert = new Alert(Alert.AlertType.INFORMATION, "igra nachalas");
        //alert.showAndWait();

        //Label lbl = new Label();
        TextField textField1 = new TextField();
        textField1.setPrefColumnCount(11);
        TextField textField2 = new TextField();
        textField2.setPrefColumnCount(11);
        Button btn = new Button("Click");
        //btn.setOnAction(event -> lbl.setText("Input: " + textField1.getText()));
        StringProperty textLengthField = new SimpleStringProperty();
        StringProperty textMinesCount = new SimpleStringProperty();
        //SString asdf = new SString();
        VBox battleLayout = new VBox();
        GridPane field = new GridPane();
        EventHandler<ActionEvent> eventButt = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                long startTime = System.nanoTime();
                //textField1.getText();
                //asdf.data = textField1.getText();
                textLengthField.set(textField1.getText());
                textMinesCount.set(textField2.getText());
                Label countKnow = new Label();

                int lengthField = 10;
                int minesCount = 10;
                try {
                    lengthField = Integer.parseInt(textLengthField.get());
                    minesCount = Integer.parseInt(textMinesCount.get());
                }
                catch (Exception error) {
                    error.printStackTrace();
                }
                model.setMinesCount(minesCount);
                model.setLengthField(lengthField);
                model.generateField();
                CellState[][] userField = model.generateUserField();
                Integer[][] userMineField = model.getUserMineField();
                //String pathToImages = "C:/Users/sasha/IdeaProjects/mineswepper/src/ru/nsu/fit/minesweeper";
                Button[][] buttonsField = new Button[lengthField][lengthField];
                IntegerPropertyBase turnResult = new SimpleIntegerProperty(0);
                for (Integer i = 0; i < lengthField; ++i) {
                    for (Integer j = 0; j < lengthField; ++j) {
                        /*InputStream mineStream = getClass().getResourceAsStream("C:\\Users\\sasha\\IdeaProjects\\mineswepper\\src\\ru\\nsu\\fit\\minesweeper\\images\\0.png");
                        Image nomines = new Image(mineStream);
                        ImageView qewr = new ImageView(nomines);*/
                        StackPane stackPane = new StackPane();
                        Text butText = new Text("");
                        Rectangle qewr = new Rectangle(20,20);
                        stackPane.getChildren().addAll(qewr, butText);
                        qewr.setFill(Color.GREY);
                        Button addToField = new Button(Character.toString((char)((i + 'a'))) + (char)(j + '0'), stackPane);
                        buttonsField[i][j] = addToField;
                        addToField.setOnMouseClicked(mouseEvent -> {

                            if (mouseEvent.getButton() == MouseButton.PRIMARY) {

                                //System.out.println(addToField.getText());
                                //System.out.println(((Button)mouseEvent.getSource()).getText());
                                //model.Open(((Button)mouseEvent.getSource()).getText());
                                int wqerety = model.Open(addToField.getText());
                                //System.out.print(wqerety);
                                turnResult.set(wqerety);
                                //System.out.println(turnResult.get());
                                //addToField.setText("oop");
                            }
                            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                                model.Flag(addToField.getText());
                            }
                            /*if (userField[addToField.getText().charAt(0) - 'a'][addToField.getText().charAt(1) - '0'] == CellState.KNOWN) {
                                qewr.setFill(Color.GREEN);
                            }
                            if (userField[addToField.getText().charAt(0) - 'a'][addToField.getText().charAt(1) - '0'] == CellState.FLAG) {
                                qewr.setFill(Color.RED);
                            }*/
                            showGameField(model, buttonsField);
                            countKnow.setText(model.getCountKnown().toString());
                            if (turnResult.get() == 1) {
                                //System.out.println("lose...");
                                showGameField(model, buttonsField);
                                countKnow.setText("You lose!");
                                doUnclickable(buttonsField);
                            }
                            if (model.getCountKnown() >= model.getLengthField()*model.getLengthField() - model.getMinesCount()) {
                                countKnow.setText("You win!");
                                TextField textNameField = new TextField();
                                textNameField.setPrefColumnCount(11);
                                long time = System.nanoTime();
                                Button recBut = new Button("CLick to save");
                                recBut.setOnAction(lam -> {
                                    try {
                                        FileWriter records = new FileWriter("Record.txt", true);
                                        String name = textNameField.getText();
                                        records.write(name+" "+((time - startTime) / 10e8)+"\n");
                                        //System.out.println(name+";"+((time - startTime) / 10e8));
                                        records.close();
                                    }
                                    catch (IOException err) {
                                        err.printStackTrace();
                                    }
                                    recBut.setOnAction(null);
                                });
                                battleLayout.getChildren().add(textNameField);
                                battleLayout.getChildren().add(recBut);
                                //System.out.println((time - startTime) / 10e8);

                                doUnclickable(buttonsField);
                            }
                        });
                        //EventHandler<MouseEvent> eventCellButton = ;
                        field.add(addToField, i, j);
                    }
                }
                battleLayout.getChildren().add(field);
                battleLayout.getChildren().add(countKnow);
                Button newGameButton = new Button("New game");
                newGameButton.setOnAction(actionEvent -> {
                    doGame(stage);
                });
                Button records = new Button("Show records");
                records.setOnAction(event -> {
                    String recs = new String();
                    try {
                        File auch = new File("Record.txt");
                        FileReader fr = new FileReader(auch);
                        BufferedReader reader = new BufferedReader(fr);
                        String line = reader.readLine();
                        while (line != null) {
                            recs = recs.concat(line).concat("\n");
                            line = reader.readLine();
                        }

                    }
                    catch (IOException err) {
                        err.printStackTrace();
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Рекорды");
                    TextArea area = new TextArea(recs);
                    area.setWrapText(true);
                    area.setEditable(false);
                    alert.getDialogPane().setContent(area);

                    alert.showAndWait();

                });
                battleLayout.getChildren().add(records);
                Button helpButton = new Button("Help");
                helpButton.setOnAction(actionEvent -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Правила");
                    String message = "Цель игры -- открыть все поля без мин. Левая кнопка -- открыть поле, правая -- отметить флагом поле";
                    // alert.setHeaderText("Results:");
                    TextArea area = new TextArea(message);
                    area.setWrapText(true);
                    area.setEditable(false);
                    alert.getDialogPane().setContent(area);

                    alert.showAndWait();
                });
                battleLayout.getChildren().add(newGameButton);
                battleLayout.getChildren().add(helpButton);
                Scene battle = new Scene(battleLayout, 250, 200);
                stage.setScene(battle);
            }
        };
        //btn.setOnAction(event -> {textLengthField = textField1.getText();});
        btn.setOnAction(eventButt);
        //Label isAll = new Label();
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10, textField1, textField2, btn);
        Scene scene = new Scene(root, 250, 200);
        stage.setScene(scene);
        stage.show();
        return 0;
    }

    //public GUIController() {
        /*Minesweeper model = new Minesweeper();
        int exit_status;
        do {
            exit_status = doGame(model);
        } while (exit_status != 0);
        if (model.getCountKnown() >= model.getLengthField()*model.getLengthField() - model.getMinesCount()) {
            System.out.println("Congratulations!");
        }*/
   // }
}
