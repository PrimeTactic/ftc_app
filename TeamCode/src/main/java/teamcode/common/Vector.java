package teamcode.common;

public class Vector {

    private double direction;
    private double magnitude;

    public double getMagnitude() {

        return magnitude;

    }

    public double getDirection() {

        return direction;

    }

    public Vector(double x, double y)
    {

        this.magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if(y == 0 && x > 0) this.direction = 0.0;
        else if(y == 0 && x < 0) this.direction = 180.0;
        else this.direction = Math.toDegrees(Math.atan(x / y));

    }

}
