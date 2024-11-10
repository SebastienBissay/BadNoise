package parameters;

import processing.core.PVector;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PConstants.TWO_PI;

public final class Parameters {
    public static final long SEED = 11;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final Color BACKGROUND_COLOR = new Color(240, 235, 230);
    public static final Color STROKE_COLOR = new Color(32, 12, 12, 7.65f);
    public static final int X_INCREMENT = 15;
    public static final int Y_INCREMENT = 15;
    public static final float GAUSSIAN_DEVIATION = 2.5f;
    public static final float HEADING_MULTIPLIER = 5120 / TWO_PI;
    public static final float NOISE2_MULTIPLIER = 4.5f;
    public static final int MAXIMUM_ITERATIONS = 10000;
    public static final int FBM_NUMBER_OF_WAVES = 3;
    public static final float FBM_STARTING_AMPLITUDE = 1;
    public static final float FBM_AMPLITUDE_MULTIPLIER = .5f;
    public static final float FBM_STARTING_FREQUENCY = 1;
    public static final float FBM_FREQUENCY_MULTIPLIER = 2;
    public static final PVector DOMAIN_WARPING_OFFSET_1 = new PVector(.52f, .13f);
    public static final PVector DOMAIN_WARPING_OFFSET_2 = new PVector(.17f, .92f);
    public static final PVector DOMAIN_WARPING_OFFSET_3 = new PVector(.83f, .28f);
    public static final float DOMAIN_WARPING_MULTIPLIER_1 = 4;
    public static final float DOMAIN_WARPING_MULTIPLIER_2 = 4;
    public static final float DOMAIN_WARPING_MULTIPLIER_3 = 4;

    /**
     * Helper method to extract the constants in order to save them to a json file
     *
     * @return a Map of the constants (name -> value)
     */
    public static Map<String, ?> toJsonMap() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = Parameters.class.getDeclaredFields();
        for(Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(Parameters.class));
        }

        return Collections.singletonMap(Parameters.class.getSimpleName(), map);
    }

    public record Color (float red, float green, float blue, float alpha) {
        public Color(float red, float green, float blue) {
            this(red, green, blue, 255);
        }

        public Color(float grayscale, float alpha) {
            this(grayscale, grayscale, grayscale, alpha);
        }

        public Color(float grayscale) {
            this(grayscale, 255);
        }
    }
}
