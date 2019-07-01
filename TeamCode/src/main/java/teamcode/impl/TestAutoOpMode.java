package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import teamcode.common.IDriveSystem;

/**
 * Makes the robot turn clockwise
 */
@Autonomous(name = "TestAutoOpMode", group = "Linear OpMode")
public class TestAutoOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        TTRobot robot = new TTRobot(hardwareMap);
        waitForStart();
        telemetry.addData("Status", "The robot should be turning clockwise");
        IDriveSystem driveSystem = robot.getDriveSystem();
        driveSystem.turnContinuously(0.5);
    }

}
