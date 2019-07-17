package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class StandardDriveSystem extends FourWheelDriveSystem {

    private final double wheelDiameterInches;

    public StandardDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor, double wheelDiameterInches) {
        super(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
        this.wheelDiameterInches = wheelDiameterInches;
    }

    public void move(Vector2 velocity, double distance) {

    }

    public void moveContinuously(Vector2 velocity) {

    }

    public void turn(double degrees, double speed) {

    }

    public void turnContinuously(double speed) {

    }

}
