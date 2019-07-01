package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * For use with standard wheels.
 */
public abstract class FourWheelDriveSystem implements IDriveSystem {

    private final DcMotor frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor;

    public FourWheelDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor) {
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backLeftMotor;
    }

    public DcMotor getFrontLeftMotor() {
        return frontLeftMotor;
    }

    public DcMotor getFrontRightMotor() {
        return frontRightMotor;
    }

    public DcMotor getBackLeftMotor() {
        return backLeftMotor;
    }

    public DcMotor getBackRightMotor() {
        return backRightMotor;
    }

}
