package teamcode;

/**
 * Represents an immutable 2-dimensional vector.
 */
public final class Vector2 {

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

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalized() {
        double magnitude = magnitude();
        return new Vector2(x / magnitude, y / magnitude);
    }

    public double direction() {
        if (x == 0.0) {
            if (y == 0.0) {
                throw new RuntimeException("Vector is zero and therefore has no direction.");
            } else if (y > 0.0) {
                return 0.5 * Math.PI;
            }
            return 1.5 * Math.PI;
        } else if (y == 0.0) {
            if (x > 0) {
                return 0.0;
            }
            return Math.PI;
        }

        double referenceAngle = Math.atan(y / x);
        if (x > 0.0 && y > 0.0) {
            // Q I
            return referenceAngle;
        } else if (x < 0.0 && y > 0.0) {
            // Q II
            return Math.PI + referenceAngle;
        } else if (x < 0.0 && y < 0.0) {
            // Q III
            return Math.PI + referenceAngle;
        } else {
            // Q IV
            return referenceAngle;
        }
    }

    public boolean isZero() {
        return x == 0.0 && y == 0.0;
    }

}
