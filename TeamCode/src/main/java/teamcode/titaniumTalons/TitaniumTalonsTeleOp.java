package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

import teamcode.Vector2;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    private static final double MANUAL_ARM_BASE_MOTOR_SPEED = 0.5;
    private static final double MANUAL_ELBOW_MOTOR_SPEED = 0.5;
    private static final double LOWER_DRIVE_SPEED_MULTIPLIER = 0.5;
    private static final double LOWER_TURN_SPEED_MULTIPLIER = 0.25;
    private static final double WRIST_SERVO_ADJUST_DELTA = 0.05;

    private Timer timer;

    private boolean lowerDriveSpeed = false;
    private int driveSpeedToggle = 0;
    private boolean lowerTurnSpeed = false;
    private int turnSpeedToggle = 0;

    private boolean intakeGateOpened = false;
    private boolean gateOnCooldown = false;
    private boolean wristAdjustButtonDownLastUpdate = false;

    @Override
    protected void onInitialize() {
        timer = new Timer();
    }

    @Override
    protected void onStart() {
        Arm.lockElbow();
        Arm.setWristServoPos(0.3);
        while (opModeIsActive()) {
            driveInputUpdate();
            armInputUpdate();
            armBaseInputUpdate();
            elbowInputUpdate();
            wristInputUpdate();
            intakeInputUpdate();
            gateInputUpdate();
        }
    }

    private void driveInputUpdate() {
        if (turnSpeedToggle == 0 && gamepad1.left_stick_button) {
            turnSpeedToggle++;
        } else if (turnSpeedToggle == 1 && !gamepad1.left_stick_button) {
            turnSpeedToggle++;
        } else if (turnSpeedToggle == 2) {
            lowerTurnSpeed = !lowerTurnSpeed;
            turnSpeedToggle = 0;
        }

        if (driveSpeedToggle == 0 && gamepad1.right_stick_button) {
            driveSpeedToggle++;
        } else if (driveSpeedToggle == 1 && !gamepad1.right_stick_button) {
            driveSpeedToggle++;
        } else if (driveSpeedToggle == 2) {
            lowerDriveSpeed = !lowerDriveSpeed;
            driveSpeedToggle = 0;
        }

        double driveSpeedMultiplier = lowerDriveSpeed ? LOWER_DRIVE_SPEED_MULTIPLIER : 1.0;
        double turnSpeedMultiplier = lowerTurnSpeed ? LOWER_TURN_SPEED_MULTIPLIER : 1.0;
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        Vector2 driveVector = new Vector2(-driveX, -driveY);
        driveVector = driveVector.multiply(driveSpeedMultiplier);
        double turnSpeed = gamepad1.left_stick_x * turnSpeedMultiplier;
        Drive.driveIndefinite(driveVector, turnSpeed);
    }

    private void armInputUpdate() {
        if (gamepad1.y) {
            if (Arm.getStatus() == Arm.ArmStatus.EXTENDED) {
                Arm.retract();
            }
        } else if (gamepad1.a) {
            if (Arm.getStatus() == Arm.ArmStatus.RETRACTED) {
                Arm.extend();
            }
        }
    }

    private void armBaseInputUpdate() {
        if (gamepad1.dpad_up) {
            Arm.rotateArmBaseIndefinite(-MANUAL_ARM_BASE_MOTOR_SPEED);
        } else if (gamepad1.dpad_down) {
            Arm.rotateArmBaseIndefinite(MANUAL_ARM_BASE_MOTOR_SPEED);
        } else {
            Arm.lockBaseMotors();
        }
    }

    private void elbowInputUpdate() {
        if (gamepad1.dpad_left) {
            Arm.rotateElbowIndefinite(MANUAL_ELBOW_MOTOR_SPEED);
        } else if (gamepad1.dpad_right) {
            Arm.rotateElbowIndefinite(-MANUAL_ELBOW_MOTOR_SPEED);
        } else {
            Arm.lockElbow();
        }
    }

    private void wristInputUpdate() {
        if (!wristAdjustButtonDownLastUpdate) {
            if (gamepad1.left_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos - WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            } else if (gamepad1.right_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos + WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            }
        } else {
            if (!gamepad1.left_bumper && !gamepad1.right_bumper) {
                wristAdjustButtonDownLastUpdate = false;
            }
        }
    }

    private void intakeInputUpdate() {
        if (gamepad1.right_trigger > gamepad1.left_trigger) {
            // intake
            Arm.setIntakePower(gamepad1.right_trigger);
        } else {
            // outtake
            Arm.setIntakePower(-gamepad1.left_trigger);
        }
    }

    private void gateInputUpdate() {
        if (gamepad1.x) {
            if (!gateOnCooldown) {
                if (intakeGateOpened) {
                    Arm.closeIntakeGate();
                    startGateCooldown();
                } else {
                    Arm.openIntakeGate();
                    startGateCooldown();
                }
                intakeGateOpened = !intakeGateOpened;
            }
        }
    }

    private void startGateCooldown() {
        gateOnCooldown = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gateOnCooldown = false;
            }
        };
        timer.schedule(task, 500);
    }

}
