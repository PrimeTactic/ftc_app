package teamcode.ttl3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    private static final double MANUAL_ELBOW_SERVO_SPEED = 0.25;
    private static final double WRIST_SERVO_POS_DELTA = 0.01;

    @Override
    public void runOpMode() {
        TTL3HardwareManager.initialize(hardwareMap);
        lockWristServoPosition();
        waitForStart();
        while (opModeIsActive()) {
            driveInputUpdate();
            armInputUpdate();
            intakeUpdate();
        }
    }

    private void lockWristServoPosition() {
        TTL3HardwareManager.armWristServo.setPosition(0.05);
    }

    private boolean reducedDriveSpeed;
    private boolean driveSpeedAdjustButtonDownLastUpdate;

    private void driveInputUpdate() {
        if (driveSpeedAdjustButtonDownLastUpdate) {
            if (!gamepad1.right_stick_button) {
                driveSpeedAdjustButtonDownLastUpdate = false;
            }
        } else {
            if (gamepad1.right_stick_button) {
                reducedDriveSpeed = !reducedDriveSpeed;
                driveSpeedAdjustButtonDownLastUpdate = true;
            }
        }
        float driveSpeedMultiplier = reducedDriveSpeed ? 0.5f : 1.0f;

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

    private boolean armIsFullyExtended = true;
    private boolean reducedArmBaseSpeed = true;
    private boolean armBaseSpeedAdjustButtonDownLastUpdate;

    private void armInputUpdate() {
        if (armBaseSpeedAdjustButtonDownLastUpdate) {
            if (!gamepad1.left_stick_button) {
                armBaseSpeedAdjustButtonDownLastUpdate = false;
            }
        } else {
            if (gamepad1.left_stick_button) {
                reducedArmBaseSpeed = !reducedArmBaseSpeed;
                armBaseSpeedAdjustButtonDownLastUpdate = true;
            }
        }
        float armBaseMotorSpeed = reducedArmBaseSpeed ? 0.5f : 1.0f;

        if (gamepad1.a) {
            if (!armIsFullyExtended) {
                fullyExtendArm();
                armIsFullyExtended = true;
            }
        } else if (gamepad1.y) {
            if (armIsFullyExtended) {
                retractArmToScore();
                armIsFullyExtended = false;
            }
        }

        if (gamepad1.x) {
            TTL3HardwareManager.armWristServo.setPosition(0.05);
        } else if (gamepad1.b) {
            TTL3HardwareManager.armWristServo.setPosition(0.95);
        }

        if (gamepad1.dpad_left) {
            TTL3HardwareManager.leftArmElbowServo.setPosition(0.5 + MANUAL_ELBOW_SERVO_SPEED);
            TTL3HardwareManager.rightArmElbowServo.setPosition(0.5 + MANUAL_ELBOW_SERVO_SPEED);
        } else if (gamepad1.dpad_right) {
            TTL3HardwareManager.leftArmElbowServo.setPosition(0.5 - MANUAL_ELBOW_SERVO_SPEED);
            TTL3HardwareManager.rightArmElbowServo.setPosition(0.5 - MANUAL_ELBOW_SERVO_SPEED);
        } else {
            TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
            TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
        }

        if (gamepad1.dpad_up) {
            TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            TTL3HardwareManager.leftArmBaseMotor.setPower(-armBaseMotorSpeed);
            TTL3HardwareManager.rightArmBaseMotor.setPower(-armBaseMotorSpeed);
        } else if (gamepad1.dpad_down) {
            TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            TTL3HardwareManager.leftArmBaseMotor.setPower(armBaseMotorSpeed);
            TTL3HardwareManager.rightArmBaseMotor.setPower(armBaseMotorSpeed);
        } else {
            TTL3HardwareManager.leftArmBaseMotor.setPower(0.0);
            TTL3HardwareManager.rightArmBaseMotor.setPower(0.0);
        }

        if (gamepad1.left_bumper) {
            double wristPos = TTL3HardwareManager.armWristServo.getPosition() - WRIST_SERVO_POS_DELTA;
            TTL3HardwareManager.armWristServo.setPosition(wristPos);
        } else if (gamepad1.right_bumper) {
            double wristPos = TTL3HardwareManager.armWristServo.getPosition() + WRIST_SERVO_POS_DELTA;
            TTL3HardwareManager.armWristServo.setPosition(wristPos);
        }
    }

    private void intakeUpdate() {
        double intakePower;
        if (gamepad1.left_trigger > 0.0f) {
            intakePower = 1.0;
        } else if (gamepad1.right_trigger > 0.0f) {
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

    protected void rotateArmBase(double degrees, double power, boolean makeThreadWaitForMotors) {
        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int ticks = (int) (degrees * ARM_MOTOR_TICKS_PER_DEGREE);
        TTL3HardwareManager.leftArmBaseMotor.setTargetPosition(ticks);
        TTL3HardwareManager.rightArmBaseMotor.setTargetPosition(ticks);

        TTL3HardwareManager.leftArmBaseMotor.setPower(power);
        TTL3HardwareManager.rightArmBaseMotor.setPower(power);

        if (makeThreadWaitForMotors) {
            while (opModeIsActive() && !liftMotorsNearTarget()) ;
            TTL3HardwareManager.leftArmBaseMotor.setPower(0.0);
            TTL3HardwareManager.rightArmBaseMotor.setPower(0.0);
        } else {
            new Thread() {

                @Override
                public void run() {
                    while (opModeIsActive() && !liftMotorsNearTarget()) ;
                    TTL3HardwareManager.leftArmBaseMotor.setPower(0.0);
                    TTL3HardwareManager.rightArmBaseMotor.setPower(0.0);
                }

            }.start();
        }
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
        TTL3HardwareManager.armWristServo.setPosition(0.05);
        rotateArmBase(85, 1.0, true);
    }

    private void retractArmToScore() {
        rotateArmBase(-95, 1.0, false);
        sleep(1500);
        new Thread() {

            @Override
            public void run() {
                TTL3HardwareManager.leftArmElbowServo.setPosition(0.75);
                TTL3HardwareManager.rightArmElbowServo.setPosition(0.75);

                TTL3TeleOp.this.sleep(500);
                TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
                TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
            }

        };
        TTL3HardwareManager.armWristServo.setPosition(0.65);
        while (opModeIsActive() && !liftMotorsNearTarget()) ;
    }

}
