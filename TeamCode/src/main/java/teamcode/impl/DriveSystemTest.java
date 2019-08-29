package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.common.TTDriveSystem;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobot;

@Autonomous(name = "Drive System Test", group = "Linear OpMode")
public class DriveSystemTest extends TTOpMode {

    @Override
    protected void onInitialize() {
    }

    @Override
    protected void onStart() {
        TTRobot robot = getRobot();
        TTDriveSystem driveSystem = robot.getDriveSystem();
        driveSystem.vertical(80.0, 1.0);
    }

}
