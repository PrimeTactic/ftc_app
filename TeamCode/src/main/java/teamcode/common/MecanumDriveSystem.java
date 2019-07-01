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
    public void move(double x, double y, double speed) {

    }

    @Override
    public void moveContinuously(double x, double y) {

    }

    @Override
    public void turn(double degrees, double speed) {

    }

    @Override
    public void turnContinuously(double speed) {

    }

}
