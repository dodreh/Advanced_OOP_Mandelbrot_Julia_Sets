package at.tugraz.oop2.shared;

public interface FractalAlgorithm<T extends FractalRenderOptions> {
    FractalRenderResult render(T options);
}
