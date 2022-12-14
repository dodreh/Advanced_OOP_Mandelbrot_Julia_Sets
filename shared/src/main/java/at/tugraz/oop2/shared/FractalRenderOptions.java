package at.tugraz.oop2.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class FractalRenderOptions {
    protected double centerX;
    protected double centerY;
    protected int width;
    protected int height;
    protected double zoom;
    protected int iterations;
    protected FractalType type;
    protected ColourModes mode;

}
