package teamcode.common;

public interface IDriveSystem {

    /**
     * Moves the robot a specified distance.
     *
     * @param velocity the velocity
     * @param distance the distance to be travelled
     */
    void move(Vector2 velocity, double distance);

    /**
     * Moves the robot continuously in the direction specified by the vector components.
     *
     * @param velocity the velocity
     */
    void moveContinuously(Vector2 velocity);

    /**
     * Turns the robot clockwise over a specified angle (or counterclockwise if the angle is negative).
     *
     * @param degrees the number of degrees that define the angle, determines direction of turn
     * @param speed   the speed at which the robot should turn, does not determine direction of turn (the absolute value is used)
     */
    void turn(double degrees, double speed);

    /**
     * Turns the robot clockwise continuously (or counterclockwise if the speed is negative).
     *
     * @param speed the speed at which the robot should turn, also determines direction of turn
     */
    void turnContinuously(double speed);

}
