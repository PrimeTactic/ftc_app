package teamcode.ttl3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "TTL3TeleOp", group = "Linear OpMode")
public class TTL3TeleOp extends LinearOpMode {

    /**
     * The minimum magnitude of game pad input on the x-axis of the left stick that must be reached for lateral translational movement on a single update.
     */
    private static final float MIN_DRIVE_X_INPUT = 0.25F;
    /**
     * The minimum magnitude of game pad input on the y-axis of the left stick that must be reached for vertical translational movement on a single update.
     */
    private static final float MIN_DRIVE_Y_INPUT = 0.25F;
    private static final double ARM_MOTOR_TICKS_PER_DEGREE = 6.0;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;

    /**
     * Used to schedule time-based tasks.
     */
    private Timer timer = new Timer();

    @Override
    public void runOpMode() {
        // initializes hardware components
        TTL3HardwareManager.initialize(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            driveInputUpdate();
            armInputUpdate();
            intakeUpdate();
        }
    }

    private boolean reducedSpeed;
    private boolean rightBumperDownLastUpdate;

    private void driveInputUpdate() {
        if (rightBumperDownLastUpdate) {
            if (!gamepad1.right_bumper) {
                rightBumperDownLastUpdate = false;
            }
        } else {
            if (gamepad1.right_bumper) {
                reducedSpeed = !reducedSpeed;
                rightBumperDownLastUpdate = true;
            }
        }
        telemetry.addData("reduced speed?", reducedSpeed);
        telemetry.update();
        float driveSpeedMultiplier = reducedSpeed ? 0.25f : 1.0f;

        if (Math.abs(gamepad1.right_stick_y) > MIN_DRIVE_Y_INPUT) {
            float driveVerticalPower = gamepad1.right_stick_y * driveSpeedMultiplier;
            driveVertical(driveVerticalPower);
        } else if (Math.abs(gamepad1.right_stick_x) > MIN_DRIVE_X_INPUT) {
            float driveLateralPower = gamepad1.right_stick_x * driveSpeedMultiplier;
            driveLateral(driveLateralPower);
        } else {
            float turnPower = gamepad1.left_stick_x * driveSpeedMultiplier;
            turn(turnPower);
        }
    }

    private boolean armIsFullyExtended = false;

    private void armInputUpdate() {
        if (gamepad1.a) {
            if (!armIsFullyExtended) {
                fullyExtendArm();
                armIsFullyExtended = true;
            }
        } else if (gamepad1.y) {
            if (armIsFullyExtended) {
                retractArmToNeutral();
                armIsFullyExtended = false;
            }
        }

        if (gamepad1.x) {
            // flip wrist back to drop off mineral
        } else if (gamepad1.b) {
            // revert wrist to default state
        }
    }

    private void intakeUpdate() {
        double intakePower;
        float rt = gamepad1.right_trigger;
        float lt = gamepad1.left_trigger;

        if (rt > 0.0f) {

        } else if (lt > 0.0f) {

        }
        if (gamepad1.left_bumper) {
            intakePower = 1.0;
        } else if (gamepad1.right_bumper) {
            intakePower = 0.0;
        } else {
            intakePower = 0.5;
        }
        TTL3HardwareManager.intakeServo.setPosition(intakePower);
    }

    private void driveVertical(double power) {
        TTL3HardwareManager.frontLeftDrive.setPower(power);
        TTL3HardwareManager.frontRightDrive.setPower(power);
        TTL3HardwareManager.backLeftDrive.setPower(power);
        TTL3HardwareManager.backRightDrive.setPower(power);
    }

    private void driveLateral(double power) {
        TTL3HardwareManager.frontLeftDrive.setPower(-power);
        TTL3HardwareManager.frontRightDrive.setPower(power);
        TTL3HardwareManager.backLeftDrive.setPower(power);
        TTL3HardwareManager.backRightDrive.setPower(-power);
    }

    private void turn(double power) {
        TTL3HardwareManager.frontLeftDrive.setPower(power);
        TTL3HardwareManager.frontRightDrive.setPower(-power);
        TTL3HardwareManager.backLeftDrive.setPower(power);
        TTL3HardwareManager.backRightDrive.setPower(-power);
    }

    protected void rotateArmBase(double degrees, double power) {
        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int ticks = (int) (degrees * ARM_MOTOR_TICKS_PER_DEGREE);
        TTL3HardwareManager.leftArmBaseMotor.setTargetPosition(ticks);
        TTL3HardwareManager.rightArmBaseMotor.setTargetPosition(ticks);

        TTL3HardwareManager.leftArmBaseMotor.setPower(power);
        TTL3HardwareManager.rightArmBaseMotor.setPower(power);

        while (opModeIsActive() && !liftMotorsNearTarget()) ;
        TTL3HardwareManager.leftArmBaseMotor.setPower(0.0);
        TTL3HardwareManager.rightArmBaseMotor.setPower(0.0);
    }

    private boolean liftMotorsNearTarget() {
        int targetLeftLiftMotorPos = TTL3HardwareManager.leftArmBaseMotor.getTargetPosition();
        int targetRightLiftMotorPos = TTL3HardwareManager.rightArmBaseMotor.getTargetPosition();

        int currentLeftLiftMotorPos = TTL3HardwareManager.leftArmBaseMotor.getCurrentPosition();
        int currentRightLiftMotorPos = TTL3HardwareManager.rightArmBaseMotor.getCurrentPosition();

        return Math.abs(currentLeftLiftMotorPos - targetLeftLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentRightLiftMotorPos - targetRightLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private void fullyExtendArm() {
        TTL3HardwareManager.leftArmElbowServo.setPosition(1.0);
        TTL3HardwareManager.rightArmElbowServo.setPosition(1.0);
        Timer timer = new Timer();
        TimerTask stopElbowServoTask = new TimerTask() {

            @Override
            public void run() {
                TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
                TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
            }

        };
        timer.schedule(stopElbowServoTask, 3625);

        TTL3HardwareManager.armWristServo.setPosition(0.1);
        rotateArmBase(85, 1.0);
    }

    private void retractArmToNeutral() {
        TTL3HardwareManager.leftArmElbowServo.setPosition(0.0);
        TTL3HardwareManager.rightArmElbowServo.setPosition(0.0);
        Timer timer = new Timer();
        TimerTask stopElbowServoTask = new TimerTask() {

            @Override
            public void run() {
                TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
                TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
            }

        };
        timer.schedule(stopElbowServoTask, 3625);

        TTL3HardwareManager.armWristServo.setPosition(0.95);
        rotateArmBase(-85, 1.0);
    }

}
