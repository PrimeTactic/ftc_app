package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * For use with mecanum wheels.
 */
public class MecanumDriveSystem extends FourWheelDriveSystem {

    public MecanumDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor) {
        super(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
    }

    @Override
    public void move(Vector2 velocity, double distance) {
        int flPos = 0;
        int frPos = 0;
        int blPos = 0;
        int brPos = 0;
        double flPow = 0.0;
        double frPow = 0.0;
        double blPow = 0.0;
        double brPow = 0.0;

        frontLeftMotor.setTargetPosition(flPos);
        frontRightMotor.setTargetPosition(frPos);
        backLeftMotor.setTargetPosition(blPos);
        backLeftMotor.setTargetPosition(brPos);

        frontLeftMotor.setPower(flPow);
        frontRightMotor.setPower(frPow);
        backLeftMotor.setPower(blPow);
        backRightMotor.setPower(brPow);
    }

    @Override
    public void moveContinuously(Vector2 velocity) {

    }

    @Override
    public void turn(double degrees, double speed) {

    }

    @Override
    public void turnContinuously(double speed) {

    }

}
