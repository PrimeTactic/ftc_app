package teamcode.impl;

import teamcode.common.TTDriveSystem;
import teamcode.common.TTHardwareManager;
import teamcode.common.TTOpMode;
import teamcode.common.TTRobot;
import teamcode.common.Vector2;

public class TTTeleOp extends TTOpMode {

    private TTDriveSystem driveSystem;

    @Override
    protected void onInitialize() {
        setHardwareRestriction(TTHardwareManager.TTHardwareRestriction.DRIVE_SYSTEM_ONLY);
    }

    @Override
    protected void onStart() {
        TTRobot robot = getRobot();
        driveSystem = robot.getDriveSystem();

        while (opModeIsActive()) {
            update();
        }
    }

    private void update() {
        double vertical = gamepad1.right_stick_y;
        double horizontal = gamepad1.right_stick_x;
        double turn = gamepad1.left_stick_x;
        Vector2 velocity = new Vector2(vertical, horizontal);

        driveSystem.continuous(velocity, turn);
    }

}
