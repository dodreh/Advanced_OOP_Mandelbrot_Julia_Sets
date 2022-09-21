package at.tugraz.oop2.shared;

import javafx.concurrent.Task;


public class ThreadTaskJulia extends Task<SimpleImage> {
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

    public ThreadTaskJulia(double mandelbrotX, double mandelbrotY, double mandelbrotZoom, int iterations, double juliaX, double juliaY,
                           double juliaZoom, ColourModes colour, int width, int height)
    {
        this.mandelbrotX = mandelbrotX;
        this.mandelbrotY = mandelbrotY;
        this.mandelbrotZoom = mandelbrotZoom;
        this.iterations = iterations;
        this.juliaX = juliaX;
        this.juliaY = juliaY;
        this.colour = colour;
        this.width = width;
        this.height = height;
        //if (juliaZoom < 1)
        this.juliaZoom = juliaZoom;
        if (this.juliaZoom < 1.00) {
            this.juliaZoom = 1.00;
        }
        // else
        //   this.juliaZoom = juliaZoom;
    }

    @Override
    protected SimpleImage call() throws Exception {
        if (this.colour == ColourModes.BLACK_WHITE)
        {
            SimpleImage image = new SimpleImage(3, width, height);
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    double zx =  (row - width / 2) / ( juliaZoom * width) + juliaX;
                    double zy = (col - height / 2) / ( juliaZoom * height) + juliaY;
                    int iterations_count = 0;
                    int iterations_max = iterations;
                    while (zx * zx + zy * zy < 4 && iterations_count < iterations_max) {
                        double tmp = zx * zx - zy * zy  + mandelbrotX;
                        zy = 2.0 * zx * zy + mandelbrotY;
                        zx = tmp;
                        iterations_count++;
                    }
                    short[] black = {0,0,0};
                    short[] white = {255, 255, 255};

                    image.setPixel(row, col, white);
                    if (iterations_count == iterations_max)
                        image.setPixel(row, col, black);
                }
            }
            FractalLogger.logRenderFinished(FractalType.JULIA, image);
            return image;
        }
        else
        {
            SimpleImage image = new SimpleImage(3, width, height);
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    double zx = (row - width / 2) / (juliaZoom * width) + juliaX;
                    double zy =  (col - height / 2) / (juliaZoom * height) + juliaY;
                    int iterations_count = 0;
                    int iterations_max = iterations;
                    while (zx * zx + zy * zy < 4 && iterations_count < iterations_max) {
                        double tmp = zx * zx - zy * zy  + mandelbrotX;
                        zy = 2.0 * zx * zy + mandelbrotY;
                        zx = tmp;
                        iterations_count++;
                    }
                    double t = (double)iterations_count / (iterations_max - 1);
                    short data_rgb[] = new short[3];
                    if (iterations_count < iterations_max) {
                        double blue = 255.0 * (1.0 - t);
                        short sblue = (short) blue;
                        double red = 255.0 * t;
                        short sred = (short) red;
                        data_rgb[0] = sred;
                        data_rgb[1] = 0;
                        data_rgb[2] = sblue;
                        image.setPixel(row, col, data_rgb);
                    } else {
                        data_rgb[0] = 0;
                        data_rgb[1] = 0;
                        data_rgb[2] = 0;
                        image.setPixel(row, col, data_rgb);
                    }
                }
            }
            FractalLogger.logRenderFinished(FractalType.JULIA, image);
            return image;
        }
    }
}
