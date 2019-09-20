package teamcode.impl;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Position;

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
        //driveSystem.vertical(75, 1);
        //sleep(250);
        //driveSystem.turn(180 * 5, 1);
        //sleep(250);
        //driveSystem.vertical(100, 1);
        //sleep(250);
        //driveSystem.turn(90, 1);
        driveSystem.vertical(10, 0.4);
        driveSystem.vertical(-10, 0.4);
    }

}
