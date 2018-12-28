package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

import teamcode.Vector2;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    private static final double MANUAL_ARM_BASE_MOTOR_SPEED = 0.5;
    private static final double LOWER_SPEED_MULTIPLIER = 0.25;
    private static final double WRIST_SERVO_ADJUST_DELTA = 0.05;

    private Timer timer;

    @Override
    protected void onInitialize() {
        timer = new Timer();
    }

    @Override
    protected void onStart() {
        Arm.setWristServoPos(0.65);
        while (opModeIsActive()) {
            driveInputUpdate();
            armInputUpdate();
        }
    }

    private boolean lowerSpeed = false;
    private boolean lowerSpeedButtonDownLastUpdate = false;

    private void driveInputUpdate() {
        if (lowerSpeedButtonDownLastUpdate) {
            if (!gamepad1.right_stick_button) {
                lowerSpeedButtonDownLastUpdate = false;
            }
        } else {
            if (gamepad1.right_stick_button) {
                lowerSpeedButtonDownLastUpdate = true;
                lowerSpeed = !lowerSpeed;
            }
        }

        double speedMultiplier = lowerSpeed ? LOWER_SPEED_MULTIPLIER : 1.0;
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        Vector2 driveVector = new Vector2(-driveX, driveY);
        double turnSpeed = gamepad1.left_stick_x;
        Drive.driveIndefinite(driveVector.multiply(speedMultiplier), turnSpeed * speedMultiplier);
    }

    private boolean intakeGateOpened = false;
    private boolean gateOnCooldown = false;
    private boolean armBaseRotatedLastUpdate = false;
    private boolean wristAdjustButtonDownLastUpdate = false;

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

        if (gamepad1.dpad_up) {
            Arm.rotateArmBaseIndefinite(-MANUAL_ARM_BASE_MOTOR_SPEED);
            armBaseRotatedLastUpdate = true;
        } else if (gamepad1.dpad_down) {
            Arm.rotateArmBaseIndefinite(MANUAL_ARM_BASE_MOTOR_SPEED);
            armBaseRotatedLastUpdate = true;
        } else if (armBaseRotatedLastUpdate) {
            Arm.lockBaseMotors();
            armBaseRotatedLastUpdate = false;
        }

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

        if (gamepad1.right_trigger > gamepad1.left_trigger) {
            // intake
            Arm.setIntakePower(gamepad1.right_trigger);
        } else {
            // outtake
            Arm.setIntakePower(-gamepad1.left_trigger);
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
