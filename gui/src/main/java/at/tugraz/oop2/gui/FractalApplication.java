package at.tugraz.oop2.gui;

import at.tugraz.oop2.shared.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.NumberStringConverter;
import lombok.Getter;

import javax.script.Bindings;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.*;
import lombok.SneakyThrows;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;



import java.lang.Object;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math; // me
import static javafx.scene.image.PixelFormat.getByteRgbInstance;

public class FractalApplication extends Application {

    private GridPane mainPane;
    private Canvas rightCanvas;
    private Canvas leftCanvas;
    private GridPane controlPane;

    private double pressedX, pressedY;

    @Getter
    private DoubleProperty leftHeight = new SimpleDoubleProperty();
    @Getter
    private DoubleProperty leftWidth = new SimpleDoubleProperty();
    @Getter
    private DoubleProperty rightHeight = new SimpleDoubleProperty();
    @Getter
    private DoubleProperty rightWidth = new SimpleDoubleProperty();

    private void updateSizes() {

        Bounds leftSize = mainPane.getCellBounds(0, 0);
        leftCanvas.widthProperty().set(leftSize.getWidth());
        leftCanvas.heightProperty().set(leftSize.getHeight());

        Bounds rightSize = mainPane.getCellBounds(1, 0);
        rightCanvas.widthProperty().set(rightSize.getWidth());
        rightCanvas.heightProperty().set(rightSize.getHeight());
    }


    @Override
    public void start(Stage primaryStage) {
        Button button1 = new Button("button 1");

        mainPane = new GridPane();

        leftCanvas = new Canvas();


        leftCanvas.setCursor(Cursor.HAND);

        mainPane.setGridLinesVisible(true);
        mainPane.add(leftCanvas, 0, 0);

        rightCanvas = new Canvas();
        rightCanvas.setCursor(Cursor.HAND);

        mainPane.add(rightCanvas, 1, 0);

        ColumnConstraints cc1 =
                new ColumnConstraints(1, 1, -1, Priority.ALWAYS, HPos.CENTER, true);
        ColumnConstraints cc2 =
                new ColumnConstraints(1, 1, -1, Priority.ALWAYS, HPos.CENTER, true);
        ColumnConstraints cc3 =
                new ColumnConstraints(400, 400, 400, Priority.ALWAYS, HPos.CENTER, true);

        mainPane.getColumnConstraints().addAll(cc1, cc2, cc3);


        RowConstraints rc1 =
                new RowConstraints(400, 400, -1, Priority.ALWAYS, VPos.CENTER, true);

        mainPane.getRowConstraints().addAll(rc1);

        leftHeight.bind(leftCanvas.heightProperty());
        leftWidth.bind(leftCanvas.widthProperty());
        rightHeight.bind(rightCanvas.heightProperty());
        rightWidth.bind(rightCanvas.widthProperty());

        mainPane.widthProperty().addListener(observable -> updateSizes());
        mainPane.heightProperty().addListener(observable -> updateSizes());


        controlPane = new GridPane();
        ColumnConstraints controlLabelColConstraint =
                new ColumnConstraints(195, 195, 200, Priority.ALWAYS, HPos.CENTER, true);
        ColumnConstraints controlControlColConstraint =
                new ColumnConstraints(195, 195, 195, Priority.ALWAYS, HPos.CENTER, true);
        controlPane.getColumnConstraints().addAll(controlLabelColConstraint, controlControlColConstraint);
        mainPane.add(controlPane, 2, 0);

        Text iterations_text = new Text("Iterations");
        controlPane.add(iterations_text, 0, 0);
        Text mandelbrotX_text = new Text("Mandelbrot X center");
        controlPane.add(mandelbrotX_text, 0, 1);
        Text mandelbrotY_text = new Text("Mandelbrot Y center");
        controlPane.add(mandelbrotY_text, 0, 2);
        Text mandelbrotZoom_text = new Text("Mandelbrot Zoom");
        controlPane.add(mandelbrotZoom_text, 0, 3);
        Text juliaX_text = new Text("Julia X center");
        controlPane.add(juliaX_text, 0, 4);
        Text juliaY_text = new Text("Julia Y center");
        controlPane.add(juliaY_text, 0, 5);
        Text juliaZoom_text = new Text("Julia Zoom");
        controlPane.add(juliaZoom_text, 0, 6);
        Text colourMode_text = new Text("Colour Mode");
        controlPane.add(colourMode_text, 0, 7);
        // new params
        Text renderMode_text = new Text("Render Mode");
        controlPane.add(renderMode_text, 0, 8);
        Text tasksPerWorker_text = new Text("Tasks per Worker");
        controlPane.add(tasksPerWorker_text, 0, 9);
        Text connctionEditor_text = new Text("Connection Editor");
        controlPane.add(connctionEditor_text, 0, 10);
        Text connctedWorkers_text = new Text("Connected Workers");
        controlPane.add(connctedWorkers_text, 0, 11);


        TextField iterations_field = new TextField();
        TextField mandelbrotX_field = new TextField();
        TextField mandelbrotY_field = new TextField();
        TextField mandelbrotZoom_field = new TextField();
        TextField juliaX_field = new TextField();
        TextField juliaY_field = new TextField();
        TextField juliaZoom_field = new TextField();
        ComboBox<ColourModes> colourMode_box = new ComboBox<>();
        // new fields
        ComboBox<RenderMode> renderMode_box = new ComboBox<>();
        TextField tasksPerWorker_field = new TextField();
        Button editConnection = new Button();
        editConnection.setText("Connection Editor");
        Text connectedWorkers_text = new Text("3");

        // Adding buttons on the editConnection.


        controlPane.add(iterations_field, 1,0);
        controlPane.add(mandelbrotX_field, 1,1);
        controlPane.add(mandelbrotY_field, 1,2);
        controlPane.add(mandelbrotZoom_field, 1,3);
        controlPane.add(juliaX_field, 1,4);
        controlPane.add(juliaY_field, 1,5);
        controlPane.add(juliaZoom_field, 1,6);
        controlPane.add(colourMode_box, 1, 7);
        controlPane.add(renderMode_box, 1, 8);
        controlPane.add(editConnection, 1, 9);
        controlPane.add(tasksPerWorker_field, 1, 10);
        controlPane.add(connectedWorkers_text, 1, 11);

        colourMode_box.setItems(FXCollections.observableArrayList(ColourModes.values()));
        colourMode_box.valueProperty().bindBidirectional(Main.colourModeProperty());
        renderMode_box.setItems(FXCollections.observableArrayList(RenderMode.values())); // render box
        renderMode_box.valueProperty().bindBidirectional(Main.renderModeProperty()); // render box
        NumberStringConverter converter = new NumberStringConverter();
        iterations_field.setText(String.valueOf(Main.getIterations()));
        iterations_field.textProperty().bindBidirectional(Main.iterationsProperty(), converter);
        mandelbrotX_field.setText(String.valueOf(Main.getMandelbrotX()));
        mandelbrotX_field.textProperty().bindBidirectional(Main.mandelbrotXProperty(), converter);
        mandelbrotY_field.setText(String.valueOf(Main.getMandelbrotY()));
        mandelbrotY_field.textProperty().bindBidirectional(Main.mandelbrotYProperty(), converter);
        mandelbrotZoom_field.setText(String.valueOf(Main.getMandelbrotZoom()));
        mandelbrotZoom_field.textProperty().bindBidirectional(Main.mandelbrotZoomProperty(), converter);
        juliaX_field.setText(String.valueOf(Main.getJuliaX()));
        juliaX_field.textProperty().bindBidirectional(Main.juliaXProperty(), converter);
        juliaY_field.setText(String.valueOf(Main.getJuliaY()));
        juliaY_field.textProperty().bindBidirectional(Main.juliaYProperty(), converter);
        juliaZoom_field.setText(String.valueOf(Main.getJuliaZoom()));
        juliaZoom_field.textProperty().bindBidirectional(Main.juliaZoomProperty(), converter);

        Scene scene = new Scene(mainPane);

        primaryStage.setTitle("Fractal Displayer");
        primaryStage.setScene(scene);
        primaryStage.show();


        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWING, event -> {
            System.out.println("Ready!");
        });

        primaryStage.setWidth(1080);
        primaryStage.setHeight(720);


        Platform.runLater(() -> {
            updateSizes();
            FractalLogger.logGUIInitialized(mainPane, primaryStage, leftCanvas, rightCanvas);
        });

        editConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Label workerConnections = new Label("Worker Connections:");
                Text workerConnections = new Text("Worker Connections:");

                StackPane newLayout = new StackPane();
                //GridPane newLayout = new GridPane();

                newLayout.getChildren().add(workerConnections);
                newLayout.setAlignment(workerConnections, Pos.TOP_LEFT);
                //newLayout.add(workerConnections, 0, 0);
                Button cancel = new Button("Cancel");
                Button ok = new Button("OK");
                Button plus = new Button("+");
                Button checkConnections = new Button("Check connections");
                newLayout.getChildren().add(cancel);
                newLayout.getChildren().add(ok);
                //newLayout.getChildren().add(plus);
                //newLayout.getChildren().add(checkConnections);
                newLayout.setAlignment(cancel, Pos.BOTTOM_CENTER);
                newLayout.setAlignment(ok, Pos.BOTTOM_RIGHT);
                //newLayout.setAlignment(plus, Pos.);
                //newLayout.setAlignment(checkConnections, Pos.BOTTOM_RIGHT);
                //newLayout.add(cancel, 0, 10);
                //newLayout.add(ok, 1, 11);
                cancel.setLayoutX(200);
                cancel.setLayoutY(220);

                Scene conn = new Scene(newLayout, 250, 430);

                Stage connectionEdit = new Stage();
                //connectionEdit.setTitle("");
                connectionEdit.setScene(conn);

                connectionEdit.setX(primaryStage.getX() + 820);
                connectionEdit.setY(primaryStage.getY() + 150);
                connectionEdit.show();
            }
        });


//ZOOMING
        // MANDELBROT
        leftCanvas.setOnScroll(new EventHandler<ScrollEvent> () {
            @Override
            public void handle(ScrollEvent event){
                DecimalFormat df = new DecimalFormat("0.00");

                double delta = event.getDeltaY();
                double zoomIn = Double.valueOf(mandelbrotZoom_field.getText()) + delta * 0.02;
                if (zoomIn < 0.00) {
                    zoomIn = 1.00;
                }
                mandelbrotZoom_field.setText(String.valueOf(df.format(zoomIn)));
                FractalLogger.logZoom(zoomIn, FractalType.MANDELBROT);
            }
        });

        // JULIA
        rightCanvas.setOnScroll(new EventHandler<ScrollEvent> () {
            @Override
            public void handle(ScrollEvent event){
                DecimalFormat df = new DecimalFormat("0.00");

                double delta = event.getDeltaY();
                double zoomIn = Double.valueOf(juliaZoom_field.getText()) + delta * 0.02;
                if (zoomIn < 0.00) {
                    zoomIn = 1.00;
                }
                juliaZoom_field.setText(df.format(zoomIn));
                FractalLogger.logZoom(Double.valueOf(df.format(zoomIn)), FractalType.JULIA);
            }
        });

        // Adding mouse pressed event on leftCanvas => implementing panning for mandelbrot..
        leftCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pressedX = mouseEvent.getX();
                pressedY = mouseEvent.getY();
            }
        });
        // Adding MouseDrag event on leftCanvas (mandelbrot) => implementing panning for mandelbrot.
        leftCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double width = Math.pow(2, 2 - Main.getMandelbrotZoom());
                double height = leftCanvas.getHeight()/leftCanvas.getWidth() * width;
                double deltaX = pressedX - mouseEvent.getX();
                double deltaY = pressedY - mouseEvent.getY();
                double mandelx = Double.valueOf(mandelbrotX_field.getText()) + width * deltaX / leftCanvas.getWidth();
                double mandely = Double.valueOf(mandelbrotY_field.getText()) + height * deltaY / leftCanvas.getHeight();

                mandelbrotX_field.textProperty().setValue(String.valueOf(mandelx));
                mandelbrotY_field.textProperty().setValue(String.valueOf(mandely));

                //Main.mandelbrotXProperty().set(Main.mandelbrotXProperty().get() + width * deltaX / leftCanvas.getWidth());
                //Main.mandelbrotYProperty().set(Main.mandelbrotYProperty().get() + height * deltaY / leftCanvas.getHeight());
                pressedX = mouseEvent.getX();
                pressedY = mouseEvent.getY();
            }
        });

        // Adding mouse pressed event on rightCanvas => implementing panning for julia.
        rightCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                pressedX = mouseEvent.getX();
                pressedY = mouseEvent.getY();
            }
        });
        // Adding MouseDrag event on rightCanvas (julia) => implementing panning for julia.
        rightCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double width = Math.pow(2, 2 - Main.getMandelbrotZoom());
                double height = rightCanvas.getHeight() / rightCanvas.getWidth() * width;
                double deltaX = pressedX - mouseEvent.getX();
                double deltaY = pressedY - mouseEvent.getY();
                double juliax = Double.valueOf(juliaX_field.getText()) + width * deltaX / rightCanvas.getWidth();
                double juliay = Double.valueOf(juliaY_field.getText()) + height * deltaY / rightCanvas.getHeight();

                juliaX_field.textProperty().setValue(String.valueOf(juliax));
                juliaY_field.textProperty().setValue(String.valueOf(juliay));
                //Main.juliaXProperty().set(Main.juliaXProperty().get() + width * deltaX / rightCanvas.getWidth());
                //Main.juliaYProperty().set(Main.juliaYProperty().get() + height * deltaY / rightCanvas.getHeight());
                pressedX = mouseEvent.getX();
                pressedY = mouseEvent.getY();
            }
        });

        int img_width = (int)primaryStage.getWidth() / 3;
        int img_height = (int)primaryStage.getHeight();
        ThreadTask objectMandelIter = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()), Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

        Thread th = new Thread(objectMandelIter);
        th.setDaemon(true);
        th.start();
// STARTING CALCULATION
        objectMandelIter.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                SimpleImage result1;
                result1 = objectMandelIter.getValue();
                leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0 , img_width*3);
                //FractalLogger.logDrawDone(FractalType.MANDELBROT);
            }
        });
        ThreadTaskJulia objectJuliaIter = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

        Thread thJulia = new Thread(objectJuliaIter);
        thJulia.setDaemon(true);
        thJulia.start();

        objectJuliaIter.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                SimpleImage result1;
                result1 = objectJuliaIter.getValue();
                rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0 , img_width*3);
                //FractalLogger.logDrawDone(FractalType.JULIA);
            }
        });

//CHANGING WINDOW SIZE
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                //int width = newSceneWidth.intValue()/3;
                double height1 = leftCanvas.getHeight();
                int height = (int)height1;
                double width1 = leftCanvas.getWidth();
                int width = (int) width1;
                ThreadTask width_resize = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), width, height);

                Thread th = new Thread(width_resize);
                th.setDaemon(true);
                th.start();

                width_resize.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = width_resize.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, width, height, getByteRgbInstance(), result1.getByteData(), 0 , width*3);
                        //FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });

                double jheight1 = rightCanvas.getHeight();
                int jheight = (int)height1;
                double jwidth1 = rightCanvas.getWidth();
                int jwidth = (int) width1;
                ThreadTaskJulia julia_width = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), jwidth, jheight);

                Thread thJulia = new Thread(julia_width);
                thJulia.setDaemon(true);
                thJulia.start();

                julia_width.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = julia_width.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, jwidth, jheight, getByteRgbInstance(), result1.getByteData(), 0 , jwidth*3);
                        //FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                double height1 = leftCanvas.getHeight();
                int height = (int)height1;
                double width1 = leftCanvas.getWidth();
                int width = (int) width1;
                ThreadTask width_resize = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), width, height);

                Thread th = new Thread(width_resize);
                th.setDaemon(true);
                th.start();

                width_resize.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = width_resize.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, width, height, getByteRgbInstance(), result1.getByteData(), 0 , width*3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });

                double jheight1 = rightCanvas.getHeight();
                int jheight = (int)height1;
                double jwidth1 = rightCanvas.getWidth();
                int jwidth = (int) width1;
                ThreadTaskJulia julia_width = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), jwidth, jheight);

                Thread thJulia = new Thread(julia_width);
                thJulia.setDaemon(true);
                thJulia.start();

                julia_width.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = julia_width.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, jwidth, jheight, getByteRgbInstance(), result1.getByteData(), 0 , jwidth*3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });
        // System.out.println(scene.heightProperty());
        iterations_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                //System.out.println(primaryStage.getWidth());
                int value = 128;
                if (!newValue.matches("\\d*")) {
                    iterations_field.setText(String.valueOf(value));
                }
                else if(newValue.isEmpty()){
                    value = Integer.valueOf(value);
                    iterations_field.setText(String.valueOf(value));
                }
                else value = Integer.valueOf(newValue);
                ThreadTask objectMandelIter = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), value,
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectMandelIter);
                th.setDaemon(true);
                th.start();

                objectMandelIter.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectMandelIter.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0 , img_width*3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaIter = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), value,
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaIter);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaIter.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaIter.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0 , img_width*3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        mandelbrotX_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    mandelbrotX_field.setText(String.valueOf(value));
                }
                else
                {
                    mandelbrotX_field.setText(newValue);
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectMandelX = new ThreadTask(value, Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectMandelX);
                th.setDaemon(true);
                th.start();

                objectMandelX.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectMandelX.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaMandelX = new ThreadTaskJulia(value, Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaMandelX);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaMandelX.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaMandelX.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        mandelbrotY_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    mandelbrotY_field.setText(String.valueOf(value));
                }
                else
                {
                    mandelbrotY_field.setText(String.valueOf(newValue));
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectMandelY = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), value,
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectMandelY);
                th.setDaemon(true);
                th.start();

                objectMandelY.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectMandelY.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaMandelY = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()), value,
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaMandelY);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaMandelY.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaMandelY.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        mandelbrotZoom_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    mandelbrotZoom_field.setText(String.valueOf(value));
                }
                else
                {
                    mandelbrotZoom_field.setText(newValue);
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectMandelZoom = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()),Double.valueOf(mandelbrotY_field.getText()),
                        value, Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectMandelZoom);
                th.setDaemon(true);
                th.start();

                objectMandelZoom.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectMandelZoom.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaMandelZoom = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        value, Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaMandelZoom);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaMandelZoom.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaMandelZoom.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        juliaX_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    juliaX_field.setText(String.valueOf(value));
                }
                else
                {
                    juliaX_field.setText(newValue);
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectJuliaX = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()), Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        value, Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectJuliaX);
                th.setDaemon(true);
                th.start();

                objectJuliaX.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaX.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaJuliaX = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()), Double.valueOf(mandelbrotZoom_field.getText()),
                        Integer.valueOf(iterations_field.getText()), value, Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaJuliaX);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaJuliaX.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaJuliaX.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        juliaY_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    juliaY_field.setText(String.valueOf(value));
                }
                else
                {
                    juliaY_field.setText(String.valueOf(newValue));
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectJuliaY = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), value,
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectJuliaY);
                th.setDaemon(true);
                th.start();

                objectJuliaY.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaY.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaJuliaY = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), value,
                        Double.valueOf(juliaZoom_field.getText()), colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaJuliaY);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaJuliaY.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaJuliaY.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

        juliaZoom_field.textProperty().addListener(new ChangeListener<String>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                double value = 0.0;
                if (!newValue.matches("^[+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+)$") || newValue.isEmpty()) {
                    juliaZoom_field.setText(String.valueOf(value));
                }
                else
                {
                    juliaZoom_field.setText(newValue);
                    value = Double.valueOf(newValue);
                }

                ThreadTask objectJuliaZoom = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        value, colourMode_box.getValue(), img_width, img_height);

                Thread th = new Thread(objectJuliaZoom);
                th.setDaemon(true);
                th.start();

                objectJuliaZoom.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaZoom.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });
                ThreadTaskJulia objectJuliaJuliaZoom = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        value, colourMode_box.getValue(), img_width, img_height);

                Thread thJulia = new Thread(objectJuliaJuliaZoom);
                thJulia.setDaemon(true);
                thJulia.start();

                objectJuliaJuliaZoom.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = objectJuliaJuliaZoom.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });
        colourMode_box.valueProperty().addListener(new ChangeListener<ColourModes>() {
            @Override
            public void changed(ObservableValue<? extends ColourModes> observableValue, ColourModes colourModes, ColourModes t1) {
                int img_width = (int)primaryStage.getWidth() / 3;
                int img_height = (int)primaryStage.getHeight();
                ThreadTask colourThread = new ThreadTask(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), t1, img_width, img_height);

                Thread th = new Thread(colourThread);
                th.setDaemon(true);
                th.start();

                colourThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = colourThread.getValue();
                        leftCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.MANDELBROT);
                    }
                });

                ThreadTaskJulia juliaColour = new ThreadTaskJulia(Double.valueOf(mandelbrotX_field.getText()),
                        Double.valueOf(mandelbrotY_field.getText()),
                        Double.valueOf(mandelbrotZoom_field.getText()), Integer.valueOf(iterations_field.getText()),
                        Double.valueOf(juliaX_field.getText()), Double.valueOf(juliaY_field.getText()),
                        Double.valueOf(juliaZoom_field.getText()), t1, img_width, img_height);

                Thread juliaThread = new Thread(juliaColour);
                juliaThread.setDaemon(true);
                juliaThread.start();

                juliaColour.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        SimpleImage result1;
                        result1 = juliaColour.getValue();
                        rightCanvas.getGraphicsContext2D().getPixelWriter().setPixels(0, 0, img_width, img_height, getByteRgbInstance(), result1.getByteData(), 0, img_width * 3);
                        FractalLogger.logDrawDone(FractalType.JULIA);
                    }
                });
            }
        });

    }
}