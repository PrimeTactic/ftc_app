package teamcode.obsolete.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "KKL2TeleOp", group = "Linear OpMode")
public class KKL2TeleOp extends LinearOpMode {

    private static final double LIFT_BASE_MOTOR_POWER_EXTEND = 0.3;
    private static final double LIFT_BASE_MOTOR_POWER_RETRACT = -1.0;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        waitForStart();
        while (opModeIsActive()) {
            driveUpdate();
            liftUpdate();
            liftServo();
        }
    }

    private void driveUpdate() {
        float drive = gamepad1.left_stick_y;
        float steer = gamepad1.left_stick_x;
        double powerL = drive + steer;
        double powerR = drive - steer;
        KKL2HardwareManager.driveLMotor.setPower(powerL);
        KKL2HardwareManager.driveRMotor.setPower(powerR);
    }

    private void liftServo() {
        if (gamepad1.right_bumper) {
            KKL2HardwareManager.liftLockServo.setPosition(0.0);
        } else if (gamepad1.left_bumper) {
            KKL2HardwareManager.liftLockServo.setPosition(1.0);
        } else {
            KKL2HardwareManager.liftLockServo.setPosition(0.5);
        }
    }

    private void liftUpdate() {
        double liftBaseMotorPower;
        if (gamepad1.a) {
            // extend
            liftBaseMotorPower = LIFT_BASE_MOTOR_POWER_EXTEND;
        } else if (gamepad1.y) {
            // retract
            liftBaseMotorPower = LIFT_BASE_MOTOR_POWER_RETRACT;
        } else {
            liftBaseMotorPower = 0.0;
        }
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);

        if (gamepad1.b) {
            KKL2HardwareManager.liftLatchServo.setPosition(1.0);
        }
        if (gamepad1.x) {
            KKL2HardwareManager.liftLatchServo.setPosition(-1.0);
        }
    }

}