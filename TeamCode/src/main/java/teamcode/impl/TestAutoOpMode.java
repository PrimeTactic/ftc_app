package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teamcode.common.IDriveSystem;
import teamcode.common.StandardDriveSystem;
import teamcode.common.Vector2;

/**
 * Makes the robot turn clockwise
 */
@TeleOp(name = "New Motor Test", group = "Linear OpMode")
public class TestAutoOpMode extends LinearOpMode {

    private TTRobot robot;

    @Override
    public void runOpMode() {
        robot = new TTRobot(hardwareMap);
        waitForStart();
        IDriveSystem driveSystem = robot.getDriveSystem();
        driveSystem.turnContinuously(0.5);
        while (opModeIsActive()) {
            update();
        }
    }

    private void update() {
        telemetry.addData("x", gamepad1.left_stick_x);
        telemetry.addData("y", gamepad1.left_stick_y);
        telemetry.update();
        if (gamepad1.left_stick_y != 0.0 && gamepad1.left_stick_x != 0.0) {
            Vector2 velocity = new Vector2(gamepad1.left_stick_x, gamepad1.left_stick_y);
            robot.getDriveSystem().moveContinuously(velocity);
        } else {
            robot.getDriveSystem().moveContinuously(Vector2.ZERO);
        }
    }

}
