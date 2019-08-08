package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.common.MecanumDriveCalibration;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobot;

@Autonomous(name = "AutoTest", group = "Linear OpMode")
public class AutoTest extends TTOpMode {

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onStart() {
        TTRobot robot = getRobot();
        new MecanumDriveCalibration(robot).start();
    }

}
