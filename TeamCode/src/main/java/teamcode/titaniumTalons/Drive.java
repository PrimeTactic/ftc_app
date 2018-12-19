package teamcode.titaniumTalons;

import teamcode.Vector2;

// USEFUL INFORMATION: https://pmtischler-ftc-app.readthedocs.io/en/latest/tutorials/mecanum.html

/**
 * Contains methods pertaining to the drive system of the robot.
 */
public final class Drive {

    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL = 92.2;
    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL = 104.0;

    /**
     * Causes the robot to drive a definite, specified number of inches equal to the magnitude of
     * the specified vector and in the direction of the specified vector. The thread from which this
     * method is called is forced to wait until the robot finishes driving.
     *
     * @param v     the vector that the robot should use to drive
     * @param power the power that the drive motors should be set to
     */
    public static void driveDefinite(Vector2 v, double power) {

    }

    /**
     * Causes the robot to drive an indefinite distance in the direction of the specified vector
     * until the motors are explicitly told to do otherwise. The power of the motors are set to the
     * magnitude of the specified vector. The thread from which this method is called is NOT forced
     * to wait until the robot finishes driving.
     *
     * @param v the vector that the robot should use to drive
     */
    public static void driveIndefinite(Vector2 v) {
        double frontLeftPow;
        double frontRightPow;
        double backLeftPow;
        double backRightPow;

        if (v.isZero()) {
            frontLeftPow = 0.0;
            frontRightPow = 0.0;
            backLeftPow = 0.0;
            backRightPow = 0.0;
        } else {
            double direction = v.direction();
            double power = v.magnitude();
            frontLeftPow = power * Math.sin(-direction + Math.PI / 4);
            frontRightPow = power * Math.cos(-direction + Math.PI / 4);
            backLeftPow = power * Math.cos(-direction + Math.PI / 4);
            backRightPow = power * Math.sin(-direction + Math.PI / 4);
        }
        HardwareManager.frontLeftDrive.setPower(frontLeftPow);
        HardwareManager.frontRightDrive.setPower(frontRightPow);
        HardwareManager.backLeftDrive.setPower(backLeftPow);
        HardwareManager.backRightDrive.setPower(backRightPow);
    }

    /**
     * Causes the robot to turn a definite number of degrees at the specified power. The thread from which this method is
     * called is forced to wait until the robot finishes turning.
     *
     * @param degrees the degrees that the robot should turn
     * @param power   the power that the drive motors should be set to
     */
    public static void turnDefinite(double degrees, double power) {

    }

    public static void turnIndefinite(double power) {

    }

    private static boolean driveMotorsNearTarget() {
        return true;
    }

}
