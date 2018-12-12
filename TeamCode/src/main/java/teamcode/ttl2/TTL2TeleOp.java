package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "TTL2TeleOp", group = "Linear OpMode")
public class TTL2TeleOp extends LinearOpMode {

    /**
     * The minimum magnitude of game pad input on the x-axis of the left stick that must be reached for lateral translational movement on a single update.
     */
    private static final float MIN_DRIVE_X_INPUT = 0.25F;
    /**
     * The minimum magnitude of game pad input on the y-axis of the left stick that must be reached for vertical translational movement on a single update.
     */
    private static final float MIN_DRIVE_Y_INPUT = 0.25F;
    /**
     * Value representing the speed at which the lift mechanism operates.
     */
    private static final double LIFT_SPEED = 1.0;

    /**
     * Used to make sure that the robot does not translate and rotate on the same update.
     */
    private boolean translationalMovementThisUpdate;
    /**
     * Used to schedule time-based tasks.
     */
    private Timer timer = new Timer();

    @Override
    public void runOpMode() {
        // initializes hardware components
        TTL2HardwareManager.initialize(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            driveUpdate();
            armUpdate();
//            if (gamepad1.y) {
//                // raise lift mechanism
//                setArmBasePower(-LIFT_SPEED);
//            } else if (gamepad1.a) {
//                // lower lift mechanism
//                setArmBasePower(0.5 * LIFT_SPEED);
//            } else {
//                setArmBasePower(0);
//            }
        }
    }

    private void driveUpdate() {
        float driveYPower = gamepad1.left_stick_y;
        float driveXPower = gamepad1.left_stick_x;
        if (driveYPower > MIN_DRIVE_Y_INPUT) {
            driveVertical(driveYPower);
        } else if (driveXPower > MIN_DRIVE_X_INPUT) {
            driveLateral(driveXPower);
        } else {
            float turnPower = gamepad1.right_stick_x;
            turn(turnPower);
        }
    }

    private void armUpdate() {
        if (gamepad1.a) {
            int armBaseMotorPos = 0;

            TTL2HardwareManager.leftArmBaseMotor.setTargetPosition(armBaseMotorPos);
            TTL2HardwareManager.rightArmBaseMotor.setTargetPosition(armBaseMotorPos);

            double elbowServoPower = 0.0;
            TTL2HardwareManager.leftArmElbowServo.setPosition(elbowServoPower);
            TTL2HardwareManager.rightArmElbowServo.setPosition(elbowServoPower);

            int wristExtendTimeInMilis = 0;
            TimerTask extendWristTask = new TimerTask() {

                @Override
                public void run() {
                    TTL2HardwareManager.leftArmElbowServo.setPosition(0.0);
                    TTL2HardwareManager.rightArmElbowServo.setPosition(0.0);
                }

            };
            timer.schedule(extendWristTask, wristExtendTimeInMilis);
        } else if (gamepad1.y) {
            // extend
        }

        if (gamepad1.x) {
            // flip wrist back to drop off mineral
        } else if (gamepad1.b) {
            // revert wrist to default state
        }
    }

    private void driveVertical(double power) {

    }

    private void driveLateral(double power){
        
    }

    private void drive(float x, float y) {
        float frontLeftPow;
        float frontRightPow;
        float backLeftPow;
        float backRightPow;
        if (Math.abs(x) > MIN_DRIVE_X_INPUT) {
            // side to side
            frontLeftPow = -x;
            frontRightPow = x;
            backLeftPow = x;
            backRightPow = -x;
            translationalMovementThisUpdate = true;
        } else if (Math.abs(y) > MIN_DRIVE_Y_INPUT) {
            // forward backward
            frontLeftPow = y;
            frontRightPow = y;
            backLeftPow = y;
            backRightPow = y;
            translationalMovementThisUpdate = true;
        } else {
            frontLeftPow = 0.0F;
            frontRightPow = 0.0F;
            backLeftPow = 0.0F;
            backRightPow = 0.0F;
            translationalMovementThisUpdate = false;
        }
        TTL2HardwareManager.frontLeftDrive.setPower(frontLeftPow);
        TTL2HardwareManager.frontRightDrive.setPower(frontRightPow);
        TTL2HardwareManager.backLeftDrive.setPower(backLeftPow);
        TTL2HardwareManager.backRightDrive.setPower(backRightPow);
    }

    private void turn(float turn) {
        TTL2HardwareManager.frontLeftDrive.setPower(-turn);
        TTL2HardwareManager.frontRightDrive.setPower(turn);
        TTL2HardwareManager.backLeftDrive.setPower(-turn);
        TTL2HardwareManager.backRightDrive.setPower(turn);
    }

    private void setArmBasePower(double power) {
        TTL2HardwareManager.leftArmBaseMotor.setPower(power);
        TTL2HardwareManager.rightArmBaseMotor.setPower(power);
    }

}
