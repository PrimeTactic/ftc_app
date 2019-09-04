package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.common.TTDriveSystem;
import teamcode.common.TTHardwareManager;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobot;

@Autonomous(name = "Drive System Test")
public class DriveSystemTest extends TTOpMode {

    @Override
    protected void onInitialize() {
        setHardwareRestriction(TTHardwareManager.TTHardwareRestriction.DRIVE_SYSTEM_ONLY);
    }

    @Override
    protected void onStart() {
        TTRobot robot = getRobot();
        TTDriveSystem driveSystem = robot.getDriveSystem();
        driveSystem.vertical(75, 1.0);
        driveSystem.turn(180 * 4, 1.0);
        driveSystem.vertical(75, 1.0);
        driveSystem.turn(90, 1.0);
    }

}
