package at.tugraz.oop2.shared;

import javafx.concurrent.Task;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ThreadTask extends Task<SimpleImage> {
    private int iterations;
    private double mandelbrotX;
    private double mandelbrotY;
    private double mandelbrotZoom;
    private double juliaX;
    private double juliaY;
    private double juliaZoom;
    private ColourModes colour;
    private int width;
    private int height;

    //FractalLogger.logArguments(mandelbrotXProperty(), mandelbrotYProperty(), mandelbrotZoomProperty(), iterationsProperty(), juliaXProperty(),
    // juliaYProperty(), juliaZoomProperty(), colourModeProperty());
    public ThreadTask(double mandelbrotX, double mandelbrotY, double mandelbrotZoom, int iterations, double juliaX, double juliaY,
                      double juliaZoom, ColourModes colour, int width, int height)
    {
        this.mandelbrotX = mandelbrotX;
        this.mandelbrotY = mandelbrotY;
        /*if (mandelbrotZoom < 1.00) {
            System.out.println("OoOOOOOOOOOOOOOOOOOOOOL"+this.mandelbrotZoom);
            this.mandelbrotZoom = 1.00;
        }
        else {
            this.mandelbrotZoom = mandelbrotZoom;
        }*/
        this.mandelbrotZoom = mandelbrotZoom;
        if (this.mandelbrotZoom < 1.00) {
        this.mandelbrotZoom = 1.00;
    }

        this.iterations = iterations;
        this.juliaX = juliaX;
        this.juliaY = juliaY;
        this.juliaZoom = juliaZoom;
        this.colour = colour;
        this.width = width;
        this.height = height;
    }
    @Override
    protected SimpleImage call() throws Exception {
        if (this.colour == ColourModes.BLACK_WHITE) {
            System.out.println("MANDEEEEEEEEEEL"+mandelbrotZoom);
            SimpleImage image = new SimpleImage(3, width, height);
            WritableImage image1 = new WritableImage(width, height);
            //inspired by: https://github.com/joni/fractals/blob/master/mandelbrot/MandelbrotBW.java
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    double c_re = (col - width / 2) * 4.0 / (width*mandelbrotZoom) + mandelbrotX;
                    double c_im = (row - height / 2) * 4.0 / (width*mandelbrotZoom)+ mandelbrotY;
                    double x = 0, y = 0;
                    int iterations_count = 0;
                    int iterations_max = iterations;

                    while (x * x + y * y < 4 && iterations_count < iterations_max) {
                        double x_new = x * x - y * y + c_re;
                        y = 2 * x * y + c_im;
                        x = x_new;
                        iterations_count++;
                    }
                    short[] black = {0,0,0};
                    short[] white = {255, 255, 255};

                    image.setPixel(col, row, white);

                    if(iterations_count == iterations_max) {
                        image.setPixel(col, row, black);
                    }
                }
            }
            FractalLogger.logRenderFinished(FractalType.MANDELBROT, image);
            return image;
        }
        else /*if (this.colour == ColourModes.COLOUR_FADE) */{
            SimpleImage image = new SimpleImage(width, height);
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    double c_re = (col - width / 2) * 4.0 / (width * mandelbrotZoom) + mandelbrotX;
                    double c_im = (row - height / 2) * 4.0 / (width * mandelbrotZoom) + mandelbrotY;
                    double x = 0, y = 0;
                    int iterations_count = 0;
                    int iterations_max = iterations;

                    while (x * x + y * y < 4 && iterations_count < iterations_max) {
                        double x_new = x * x - y * y + c_re;
                        y = 2 * x * y + c_im;
                        x = x_new;
                        iterations_count++;
                    }
                    double t = (double)iterations_count / (iterations_max - 1);
                    // short data_rgb[] = new short[3];
                    if (iterations_count == iterations_max) {
                        short[] black = {0,0,0};
                        image.setPixel(col, row, black);
                    } else {
                        double blue = 255.0 * (1 - t);
                        short sblue = (short) blue;
                        double red = 255.0 * t;
                        short sred = (short) red;
                        short[] colour = {sred, 0, sblue};
                        image.setPixel(col, row, colour);
                    }
                }
            }
            FractalLogger.logRenderFinished(FractalType.MANDELBROT, image);
            return image;
        }
    }
}
