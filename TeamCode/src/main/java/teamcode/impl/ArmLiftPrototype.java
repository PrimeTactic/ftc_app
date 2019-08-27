package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import teamcode.common.Arm;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobotHardwareManager;
import teamcode.common.TTTimer;

import java.util.*;

@TeleOp(name = "ArmTest")
public class ArmLiftPrototype extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = 3.506493506;
    private Arm arm;

    @Override
    public void runOpMode() {
        TTRobotHardwareManager hardwareManager = new TTRobotHardwareManager(hardwareMap);
        arm = new Arm(hardwareManager.getArmLift(), hardwareManager.getArmElbow());

        waitForStart();

        while (opModeIsActive()) {
            update();
        }
    }

    private void update() {
        while (gamepad1.right_trigger > 0.3) {
            arm.liftContinuous(gamepad1.right_trigger);
        }
        arm.liftContinuous(0);

        while (gamepad1.left_trigger > 0.3) {
            arm.liftContinuous(-gamepad1.left_trigger);
        }
        arm.liftContinuous(0);

        if (gamepad1.dpad_down) {
            TimerTask ts = new TimerTask() {
                @Override
                public void run() {
                    DPadArmMovement(false);
                }
            };
            arm.rotate(-5, 1);
            TTTimer.schedule(ts, 100);
        } else if (gamepad1.dpad_up) {
            TimerTask ts = new TimerTask() {
                @Override
                public void run() {
                    DPadArmMovement(true);
                }
            };
            arm.rotate(5, 1);
            TTTimer.schedule(ts, 100);
        } else if (gamepad1.b) {
            arm.lift(3, 1);
            // arm.rotate(15,1);
        } else if (gamepad1.y) {
            arm.rotate(45, 1);
        } else if (gamepad1.a) {
            arm.rotate(-45, 1);
        } else if (gamepad1.x) {
            arm.rotate(-15, 1);
        }
    }


    public void DPadArmMovement(boolean up) {
        boolean down = !up;

        if (down && gamepad1.dpad_down) {
            while (gamepad1.dpad_down) {
                arm.rotateContinuous(-1);
            }
            arm.rotateContinuous(0);

        } else if (up && gamepad1.dpad_up) {
            while (gamepad1.dpad_up) {
                arm.rotateContinuous(1);
            }
            arm.rotateContinuous(0);
        }
    }
}
//linear lift lift is controlled by triggers
//arm rotation is DPad
//B Button moves 15 degrees and Y Button moves 45 degrees, opposite on the