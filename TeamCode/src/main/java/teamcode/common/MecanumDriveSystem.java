package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * For use with mecanum wheels.
 */
public class MecanumDriveSystem extends FourWheelDriveSystem {

    public MecanumDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor, double wheelDiameter) {
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
        if (velocity.isZero()) {
            zeroMotorPower();
            return;
        }
        setMotorRunModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double direction = velocity.getDirectionRadians();
        double power = velocity.magnitude();

        double angle = -direction + Math.PI / 4;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double frontLeftPow = power * sin;
        double frontRightPow = power * cos;
        double backLeftPow = power * cos;
        double backRightPow = power * sin;

        frontLeftMotor.setPower(frontLeftPow);
        frontRightMotor.setPower(frontRightPow);
        backLeftMotor.setPower(backLeftPow);
        backRightMotor.setPower(backRightPow);
    }

    @Override
    public void turn(double degrees, double speed) {

    }

    @Override
    public void turnContinuously(double speed) {

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
