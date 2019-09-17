package teamcode.common;

/**
 * Represents an immutable 2-dimensional vector.
 */
public final class Vector2 {

    public static final Vector2 FORWARD = new Vector2(0.0, 1.0);
    public static final Vector2 BACKWARD = new Vector2(0.0, -1.0);
    public static final Vector2 LEFT = new Vector2(-1.0, 0.0);
    public static final Vector2 RIGHT = new Vector2(1.0, 0.0);
    public static final Vector2 ZERO = new Vector2(0.0, 0.0);

    private final double x;
    private final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalized() {
        double magnitude = magnitude();
        return new Vector2(x / magnitude, y / magnitude);
    }

    public double dotProduct(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * @return the angle in radians
     */
    public double angleBetween(Vector2 other) {
        return Math.acos(this.dotProduct(other) / (this.magnitude() * other.magnitude()));
    }

    public boolean isZero() {
        return x == 0.0 && y == 0.0;
    }

    /**
     * @return the angle in radians (0 if this vector has no magnitude)
     */
    public double getDirection() {
        if (y > 0) {
            if (x > 0) {
                return Math.atan(y / x);
            } else if (x < 0) {
                return Math.PI + Math.atan(y / x);
            } else { // x == 0
                return Math.PI / 2;
            }
        } else if (y < 0) {
            if (x > 0) {
                return 2 * Math.PI + Math.atan(y / x);
            } else if (x < 0) {
                return Math.PI + Math.atan(y / x);
            } else { // x == 0
                return 3 * Math.PI / 2;
            }
        } else { // y == 0
            if (x < 0) {
                return Math.PI;
            } else if (x > 0) {
                return 0;
            } else { // x == 0
                return 0;
            }
        }
    }

}