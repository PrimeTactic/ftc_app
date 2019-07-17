package teamcode.common;

public class Vector2 {

    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDirectionDegrees() {
        if (x == 0.0) {
            if (y > 0.0) {
                return 90.0;
            } else if (y < 0.0) {
                return 180.0;
            }
            // zero vectors don't have a direction
            return Double.NaN;
        }
        return Math.toDegrees(Math.atan(y / x));
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

}
