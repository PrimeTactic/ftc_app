package teamcode.common;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class TTDriveSystem {

    // correct ticks = current ticks * expected distance / actual distance-
    private static final double INCHES_TO_TICKS_VERTICAL = -43.4641507685;
    private static final double INCHES_TO_TICKS_LATERAL = 40;
    private static final double INCHES_TO_TICKS_DIAGONAL = 90.0;
    private static final double DEGREES_TO_TICKS = -9.39;
    private static final double TICKS_WITHIN_TARGET = 30.0;
    private static final int MAX_TICKS_PER_SECOND = 40;
    private static final double SPEED_ADJUST_WITH_ENCODERS_PERIOD = 0.1;
    private static final double DECELERATION_TICKS = 1000;
    private static final int ACCELERATION_TICKS = 1000;
    private static final double MINIMUM_ENCODERS_POWER = 0.1;

    private final DcMotor frontLeft, frontRight, backLeft, backRight;
    private final DcMotor[] motors;
    private BNO055IMU imu;

    public TTDriveSystem(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, BNO055IMU imu) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        motors = new DcMotor[4];
        motors[0] = frontLeft;
        motors[1] = frontRight;
        motors[2] = backLeft;
        motors[3] = backRight;
        correctDirections();
        this.imu = imu;
    }

    private void correctDirections() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(Vector2 path, double speed) {
        Vector2 velocity = path.normalized().multiply(speed);
        drive(velocity);
        while (!nearTarget()) ;
        brake();
    }

    public void drive(Vector2 velocity) {
        if (velocity.isZero()) {
            brake();
            return;
        }

        double direction = velocity.getDirection();
        double power = velocity.magnitude();
        double angle = -direction + Math.PI / 4;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double frontLeftPow = power * sin;
        double frontRightPow = power * cos;
        double backLeftPow = power * cos;
        double backRightPow = power * sin;

        frontLeft.setPower(frontLeftPow);
        frontRight.setPower(frontRightPow);
        backLeft.setPower(backLeftPow);
        backRight.setPower(backRightPow);
    }

    /**
     * @param degrees degrees to turn clockwise
     * @param speed   [0.0, 1.0]
     */
    public void turn(double degrees, double speed) {
        turn(speed);
        while (!nearTarget()) ;
        brake();
    }

    public void turn(double speed) {
        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);
    }

    public void brake() {
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

    private boolean nearTarget() {
        return true;
    }

    private void setRunMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior bahavior) {
        frontLeft.setZeroPowerBehavior(bahavior);
        frontRight.setZeroPowerBehavior(bahavior);
        backLeft.setZeroPowerBehavior(bahavior);
        backRight.setZeroPowerBehavior(bahavior);
    }

}
