package at.tugraz.oop2.gui;

import at.tugraz.oop2.shared.*;
import javafx.beans.property.*;

public class Main {
    static public int getIterations()
    {
        return iterations.get();
    }

    public void setIterations(int iterations) {
        this.iterations.set(iterations);
    }

    static public IntegerProperty iterationsProperty() {
        return iterations;
    }

    static public double getMandelbrotX() {
        return MandelbrotX.get();
    }

    static public DoubleProperty mandelbrotXProperty() {
        return MandelbrotX;
    }

    public void setMandelbrotX(double mandelbrotX) {
        this.MandelbrotX.set(mandelbrotX);
    }

    static public double getMandelbrotY() {
        return MandelbrotY.get();
    }

    static public DoubleProperty mandelbrotYProperty() {
        return MandelbrotY;
    }

    public void setMandelbrotY(double mandelbrotY) {
        this.MandelbrotY.set(mandelbrotY);
    }

    static public double getMandelbrotZoom() {
        return MandelbrotZoom.get();
    }

    static public DoubleProperty mandelbrotZoomProperty() {
        return MandelbrotZoom;
    }

    public void setMandelbrotZoom(double mandelbrotZoom) {
        this.MandelbrotZoom.set(mandelbrotZoom);
    }

    static public double getJuliaX() {
        return JuliaX.get();
    }

    static public DoubleProperty juliaXProperty() {
        return JuliaX;
    }

    public void setJuliaX(double juliaX) {
        this.JuliaX.set(juliaX);
    }

    static public double getJuliaY() {
        return JuliaY.get();
    }

    static public DoubleProperty juliaYProperty() {
        return JuliaY;
    }

    public void setJuliaY(double juliaY) {
        this.JuliaY.set(juliaY);
    }

    static public double getJuliaZoom() {
        return JuliaZoom.get();
    }

    static public DoubleProperty juliaZoomProperty() {
        return JuliaZoom;
    }

    public void setJuliaZoom(double juliaZoom) {
        this.JuliaZoom.set(juliaZoom);
    }

    static public ColourModes getColourMode() {
        if(colourMode.equals(ColourModes.BLACK_WHITE))
            return ColourModes.BLACK_WHITE;
        else
            return ColourModes.COLOUR_FADE;
    }

    static public Property<ColourModes> colourModeProperty() {
        return colourMode;
    }

    public void setColourMode(ColourModes colourMode) {
        this.colourMode.setValue(colourMode);
    }

    static public int getTasksPerWorker() {
        return tasksPerWorker.get();
    }

    public void setTasksPerWorker(int tasksPerWorker) {
        this.tasksPerWorker.set(tasksPerWorker);
    }

    static public IntegerProperty tasksPerWorkerProperty() {
        return tasksPerWorker;
    }

    static public RenderMode getRenderMode() {
        if (renderMode.equals(RenderMode.LOCAL))
            return RenderMode.LOCAL;
        else
            return RenderMode.DISTRIBUTED;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode.setValue(renderMode);
    }

    static public Property<RenderMode> renderModeProperty() {
        return renderMode;
    }

    static public String getConnection() {
        return connection.get();
    }

    public void setConnection(String connection) {
        this.connection.set(connection);
    }

    static public StringProperty connectionProperty() {
        return connection;
    }

    private static IntegerProperty iterations;
    private static DoubleProperty MandelbrotX;
    private static DoubleProperty MandelbrotY;
    private static DoubleProperty MandelbrotZoom;
    private static DoubleProperty JuliaX;
    private static DoubleProperty JuliaY;
    private static DoubleProperty JuliaZoom;
    private static Property<ColourModes> colourMode;
    // new params
    private static IntegerProperty tasksPerWorker;
    private static Property<RenderMode> renderMode;
    private static StringProperty connection;

    public Main()
    {
        iterations = new SimpleIntegerProperty(this, "iterations", 128);
        MandelbrotX = new SimpleDoubleProperty(this, "MandelbrotX", 0.0);
        MandelbrotY = new SimpleDoubleProperty(this, "MandelbrotY", 0.0);
        MandelbrotZoom = new SimpleDoubleProperty(this, "MandelbrotZoom", 0.0);
        JuliaX = new SimpleDoubleProperty(this, "JuliaX", 0.0);
        JuliaY = new SimpleDoubleProperty(this, "JuliaY", 0.0);
        JuliaZoom = new SimpleDoubleProperty(this, "JuliaZoom", 0.0);
        colourMode = new SimpleObjectProperty<>(this, "colourMode", ColourModes.BLACK_WHITE);
        // New parameters.
        tasksPerWorker = new SimpleIntegerProperty(this, "tasksPerWorker", 5);
        renderMode = new SimpleObjectProperty<>(this, "renderMode", RenderMode.LOCAL);
        connection = new SimpleStringProperty(this, "connection", "localhost:8010");

    }

    public static void main(String[] args) {
        Main mainObject = new Main();
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("--iterations="))
            {
                args[i] = args[i].replace("--iterations=","");
                mainObject.setIterations(Integer.valueOf(args[i]));
            }

            else if (args[i].contains("--MandelbrotX="))
            {
                args[i] = args[i].replace("--MandelbrotX=","");
                mainObject.setMandelbrotX(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--MandelbrotY="))
            {
                args[i] = args[i].replace("--MandelbrotY=","");
                mainObject.setMandelbrotY(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--MandelbrotZoom="))
            {
                args[i] = args[i].replace("--MandelbrotZoom=","");
                mainObject.setMandelbrotZoom(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--JuliaX="))
            {
                args[i] = args[i].replace("--JuliaX=","");
                mainObject.setJuliaX(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--JuliaY="))
            {
                args[i] = args[i].replace("--JuliaY=","");
                mainObject.setJuliaY(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--JuliaZoom="))
            {
                args[i] = args[i].replace("--JuliaZoom=","");
                mainObject.setJuliaZoom(Double.valueOf(args[i]));
            }

            else if (args[i].contains("--colourMode="))
            {
                String check = "COLOUR_FADE";
                args[i] = args[i].replace("--colourMode=","");
                Boolean equal = check.equals(args[i]);
                if (equal == true)
                {
                    mainObject.setColourMode(ColourModes.COLOUR_FADE);
                }
            }
            else if (args[i].contains("--tasksPerWorker="))
            {
                args[i] = args[i].replace("--tasksPerWorker=","");
                mainObject.setTasksPerWorker(Integer.valueOf(args[i]));
            }
            else if (args[i].contains("--renderMode="))
            {
                String check = "DISTRIBUTED";
                args[i] = args[i].replace("--renderMode=","");
                Boolean equal = check.equals(args[i]);
                if (equal == true)
                {
                    mainObject.setRenderMode(RenderMode.DISTRIBUTED);
                }
            }
            else if (args[i].contains("--connection="))
            {
                args[i] = args[i].replace("--connection=","");
                mainObject.setConnection(String.valueOf(args[i]));
            }
        }

        FractalLogger.logArguments(mandelbrotXProperty(), mandelbrotYProperty(), mandelbrotZoomProperty(), iterationsProperty(), juliaXProperty(),
                juliaYProperty(), juliaZoomProperty(), colourModeProperty());
        FractalApplication.launch(FractalApplication.class, args);
    }
}
