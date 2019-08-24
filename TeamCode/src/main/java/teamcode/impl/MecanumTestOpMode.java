package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.common.MecanumDriveSystem;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobot;

@Autonomous(name = "Mecanum Test", group = "Linear OpMode")
public class MecanumTestOpMode extends TTOpMode {

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onStart() {
        TTRobot robot = getRobot();
        MecanumDriveSystem driveSystem = robot.getDriveSystem();
        driveSystem.vertical(50.0, 0.5);
    }

}
