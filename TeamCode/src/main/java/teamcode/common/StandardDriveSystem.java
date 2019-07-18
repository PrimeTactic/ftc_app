package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class StandardDriveSystem extends FourWheelDriveSystem {

    /**
     * Used for converting degrees to motor ticks when turning the robot.
     */
    private double degreesToTicks;
    /**
     * Used for converting inches to motor ticks when the robot moves forward and backward.
     */
    private double inchesToTicks;

    public StandardDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor, double wheelDiameterInches) {
        super(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
        degreesToTicks = 1.0;
        inchesToTicks = 1.0;

    }

    /**
     * The ticks per degrees is calculated automatically based on the wheel diameter of the drive system. If the robot isn't turning the desired angle, this value may need to be adjusted manually using this setter.
     */
    public void setDegreesToTicks(double degreesToTicks) {
        this.degreesToTicks = degreesToTicks;
    }

    /**
     * The ticks per inches is calculated automatically based on the wheel diameter of the drive system. If the robot isn't driving the desired distance, this value may need to be adjusted manually using this setter.
     */
    public void setInchesToTicks(double inchesToTicks) {
        this.inchesToTicks = inchesToTicks;
    }

    public void move(Vector2 velocity, double distance) {
        double speed = velocity.magnitude();
        turn(velocity.getDirectionDegrees(), speed);
    }

    public void moveContinuously(Vector2 velocity) {
        if (velocity.isZero()) {
            zeroMotorPower();
            return;
        }
        setMotorRunModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double speed = velocity.getY();
        frontLeftMotor.setPower(speed);
        frontRightMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        backRightMotor.setPower(speed);
    }

    public void turn(double degrees, double speed) {
        double ticks = degrees * degreesToTicks;

        frontLeftMotor.setPower(speed);

    }

    public void turnContinuously(double speed) {
        frontLeftMotor.setPower(speed);
        backLeftMotor.setPower(speed);
        frontRightMotor.setPower(-speed);
        backRightMotor.setPower(-speed);
    }

    private void zeroMotorPower() {
        frontLeftMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
    }

    private void setMotorRunModes(DcMotor.RunMode mode) {
        frontLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        backRightMotor.setMode(mode);
    }

}
