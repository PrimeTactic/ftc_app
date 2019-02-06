package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.TimerTask;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.RobotTimer;
import teamcode.titaniumTalons.SingletonOpMode;

/**
 * Handles arm-related input during teleoperation.
 */
class ArmInputListener {

    private static final double MANUAL_ARM_BASE_MOTOR_SPEED = 0.5;
    private static final double WRIST_SERVO_ADJUST_DELTA = 0.05;

    private Gamepad gamepad1;
    private Gamepad gamepad2;

    private boolean intakeGateOpened = false;
    // whether intake is automatically enabled
    private boolean autoIntake = false;
    private boolean gateOnCooldown = false;
    private boolean wristAdjustButtonDownLastUpdate = false;

    ArmInputListener() {
        gamepad1 = SingletonOpMode.instance.gamepad1;
        gamepad2 = SingletonOpMode.instance.gamepad2;
        new Thread() {

            @Override
            public void run() {
                while (SingletonOpMode.instance.opModeIsActive()) {
                    armUpdate();
                }
            }

        }.start();
        new Thread() {
            @Override
            public void run() {
                while (SingletonOpMode.instance.opModeIsActive()) {
                    intakeInputUpdate();
                }
            }
        }.start();
    }

    private void armUpdate() {
        presetUpdate();
        baseInputUpdate();
        elbowInputUpdate();
        wristInputUpdate();
        gateInputUpdate();
    }

    private void presetUpdate() {
        if (gamepad1.y) {
            if (Arm.status == Arm.ArmStatus.EXTENDED) {
                autoIntake = true;
                Arm.setIntakePower(-1.0);
                Arm.partiallyRetract();
                TimerTask disableAutoIntakeTask = new TimerTask() {
                    @Override
                    public void run() {
                        autoIntake = false;
                    }
                };
                RobotTimer.schedule(disableAutoIntakeTask, 1.0);
            }
        } else if (gamepad1.a) {
            if (Arm.status == Arm.ArmStatus.PARTIALLY_RETRACTED) {
                Arm.extend();
                Arm.closeIntakeGate();
            }
        }
    }

    private void baseInputUpdate() {
        if (gamepad1.dpad_up) {
            Arm.rotateArmBaseIndefinite(-MANUAL_ARM_BASE_MOTOR_SPEED);
        } else if (gamepad1.dpad_down) {
            Arm.rotateArmBaseIndefinite(MANUAL_ARM_BASE_MOTOR_SPEED);
        } else if (gamepad1.left_bumper) {
            Arm.rotateArmBaseIndefinite(1.0);
        } else if (gamepad1.right_bumper) {
            Arm.rotateArmBaseIndefinite(-1.0);
        } else {
            Arm.lockBaseMotors();
        }
    }

    private void elbowInputUpdate() {
        if (gamepad2.dpad_left) {
            Arm.rotateElbowIndefinite(0.75);
        } else if (gamepad2.dpad_right) {
            Arm.rotateElbowIndefinite(-0.75);
        } else if (gamepad2.dpad_down) {
            Arm.rotateElbowIndefinite(0.5);
        } else if (gamepad2.dpad_up) {
            Arm.rotateElbowIndefinite(-0.5);
        } else if (gamepad2.b) {
            //in
            Arm.rotateElbowDefinite(-45.0, 1.0);
        } else if (gamepad2.y) {
            //out
            Arm.rotateElbowDefinite(45.0, 1.0);
        } else {
            Arm.lockElbow();
        }
    }

    private void wristInputUpdate() {
        if (!wristAdjustButtonDownLastUpdate) {
            if (gamepad2.right_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos + WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            } else if (gamepad2.left_bumper) {
                double currentWristPos = Arm.getWristServoPos();
                Arm.setWristServoPos(currentWristPos - WRIST_SERVO_ADJUST_DELTA);
                wristAdjustButtonDownLastUpdate = true;
            }
        } else {
            if (!gamepad2.right_bumper && !gamepad2.left_bumper) {
                wristAdjustButtonDownLastUpdate = false;
            }
        }
    }

    private void intakeInputUpdate() {
        if (autoIntake) {
            return;
        }
        if (gamepad1.left_trigger > 0.0f || gamepad1.right_trigger > 0.0f) {
            if (gamepad1.right_trigger > gamepad1.left_trigger) {
                // intake
                Arm.setIntakePower(-gamepad1.right_trigger);
            } else {
                // outtake
                Arm.setIntakePower(gamepad1.left_trigger);
            }
        } else if (gamepad2.left_trigger > 0.0f || gamepad2.right_trigger > 0.0f) {
            if (gamepad2.right_trigger > gamepad2.left_trigger) {
                // intake
                Arm.setIntakePower(-0.5 * gamepad2.right_trigger);
            } else {
                // outtake
                Arm.setIntakePower(0.5 * gamepad2.left_trigger);
            }
        } else {
            Arm.setIntakePower(0.0);
        }
    }

    private void gateInputUpdate() {
        if (gamepad2.x) {
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
        RobotTimer.schedule(task, 0.5);
    }

}