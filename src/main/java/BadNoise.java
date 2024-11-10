import noisefield.NoiseField;
import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class BadNoise extends PApplet {
    private NoiseField noiseField1;
    private NoiseField noiseField2;

    public static void main(String[] args) {
        PApplet.main(BadNoise.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);

        noiseField1 = new NoiseField(this);
        noiseField2 = new NoiseField(this);
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        noLoop();
    }

    @Override
    public void draw() {
        for (int i = 0; i <= width; i += X_INCREMENT) {
            for (int j = 0; j <= height; j += Y_INCREMENT) {
                PVector p = new PVector(i + GAUSSIAN_DEVIATION * randomGaussian(),
                        j + GAUSSIAN_DEVIATION * randomGaussian());
                int iter = 0;
                do {
                    iter++;
                    point(p.x, p.y);
                    PVector q = PVector.sub(p, new PVector(width / 2f, height / 2f));
                    float n = noiseField1.noise(q.mag(), HEADING_MULTIPLIER * q.heading());
                    p.add(PVector.fromAngle(TWO_PI * n).mult(NOISE2_MULTIPLIER
                            * noiseField2.noise(q.mag(), HEADING_MULTIPLIER * q.heading())
                            * randomGaussian()));
                }
                while (p.x > 0
                        && p.x < width
                        && p.y > 0
                        && p.y < height
                        && iter < MAXIMUM_ITERATIONS);
            }
        }

        saveSketch(this);
    }
}
