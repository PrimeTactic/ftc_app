package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class StandardDriveSystem extends FourWheelDriveSystem {

    public StandardDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor) {
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
