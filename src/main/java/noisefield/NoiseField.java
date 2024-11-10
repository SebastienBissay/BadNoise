package noisefield;

import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static processing.core.PApplet.floor;
import static processing.core.PApplet.pow;

public class NoiseField {

    private final float[][] noiseValues;

    public NoiseField(PApplet pApplet) {
        noiseValues = new float[256][256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                noiseValues[i][j] = pApplet.random(1);
            }
        }
    }

    private float noiseValue(float x, float y) {
        int floorX = floor(x);
        int floorY = floor(y);
        floorX %= 256;
        if (floorX < 0) {
            floorX += 256;
        }
        floorY %= 256;
        if (floorY < 0) {
            floorY += 256;
        }
        float value1 = smoothTransition(noiseValues[floorX][floorY],
                noiseValues[(floorX + 1) % 256][floorY],
                x - floor(x));
        float value2 = smoothTransition(noiseValues[floorX][(floorY + 1) % 256],
                noiseValues[(floorX + 1) % 256][(floorY + 1) % 256],
                x - floor(x));

        return smoothTransition(value1, value2, y - floor(y));
    }

    /**
     * Polynomial interpolation for smooth transitions
     * P such as P(a) = 0, P'(a) = 0, P(b) = 1, P'(b) = 0, P((a + b) / 2) = 0.5 and P'((a + b) / 2) = 0.5
     *
     * @param a Starting point
     * @param b Target point
     * @param t Weight (usually 0 <= t <= 1)
     * @return Interpolated value
     */
    private float smoothTransition(float a, float b, float t) {
        return 6 * (b - a) * pow(t, 5) + 15 * (a - b) * pow(t, 4) + 10 * (b - a) * pow(t, 3) + a;
    }

    private float fbm(PVector p) {
        return fbm(p.x, p.y);
    }

    /**
     * See https://thebookofshaders.com/13/ on Fractal Brownian Motion
     *
     * @param x X coordinate of the point to consider
     * @param y Y coordinate of the point to consider
     * @return Fractal Brownian Motion at that point
     */
    private float fbm(float x, float y) {
        float noise = 0;
        float frequency = FBM_STARTING_FREQUENCY;
        float amplitude = FBM_STARTING_AMPLITUDE;
        for (int k = 0; k < FBM_NUMBER_OF_WAVES; k++) {
            noise += amplitude * noiseValue(frequency * x, frequency * y);
            frequency *= FBM_FREQUENCY_MULTIPLIER;
            amplitude *= FBM_AMPLITUDE_MULTIPLIER;
        }
        return noise;
    }

    /**
     * See https://iquilezles.org/articles/warp/ on domain warping
     *
     * @param x X coordinate of the point to consider
     * @param y Y coordinate of the point to consider
     * @return FBM with domain warping at that point
     */
    public float noise(float x, float y) {
        PVector p = new PVector(x / WIDTH, y / HEIGHT);
        PVector q = new PVector(fbm(p), fbm(PVector.add(DOMAIN_WARPING_OFFSET_1, p)));
        PVector r = new PVector(fbm(PVector.add(DOMAIN_WARPING_OFFSET_2, p)
                .add(PVector.mult(q, DOMAIN_WARPING_MULTIPLIER_1))),
                fbm(PVector.add(DOMAIN_WARPING_OFFSET_3, p).add(PVector.mult(q, DOMAIN_WARPING_MULTIPLIER_2))));
        return fbm(PVector.add(p, PVector.mult(r, DOMAIN_WARPING_MULTIPLIER_3)));
    }
}
