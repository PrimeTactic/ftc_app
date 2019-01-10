package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import teamcode.Vector2;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

public class DriveInputListener {

    private static final double LOWER_DRIVE_SPEED_MULTIPLIER = 0.5;
    private static final double LOWER_TURN_SPEED_MULTIPLIER = 0.25;

    private Gamepad gamepad1;

    private boolean lowerDriveSpeed;
    private int driveSpeedToggle;
    private boolean lowerTurnSpeed;
    private int turnSpeedToggle;

    DriveInputListener() {
        gamepad1 = SingletonOpMode.instance.gamepad1;
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    update();
                }
            }

        }.start();
    }

    private void update() {
        speedAdjustUpdate();
        driveUpdate();
    }


   private void speedAdjustUpdate(){
        if (turnSpeedToggle == 0 && gamepad1.left_stick_button) {
            turnSpeedToggle++;
        } else if (turnSpeedToggle == 1 && !gamepad1.left_stick_button) {
            turnSpeedToggle++;
        } else if (turnSpeedToggle == 2) {
            lowerTurnSpeed = !lowerTurnSpeed;
            turnSpeedToggle = 0;
        }

        if (driveSpeedToggle == 0 && gamepad1.right_stick_button) {
            driveSpeedToggle++;
        } else if (driveSpeedToggle == 1 && !gamepad1.right_stick_button) {
            driveSpeedToggle++;
        } else if (driveSpeedToggle == 2) {
            lowerDriveSpeed = !lowerDriveSpeed;
            driveSpeedToggle = 0;
        }
    }

    private void driveUpdate(){
        double driveSpeedMultiplier = lowerDriveSpeed ? LOWER_DRIVE_SPEED_MULTIPLIER : 1.0;
        double turnSpeedMultiplier = lowerTurnSpeed ? LOWER_TURN_SPEED_MULTIPLIER : 1.0;
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        Vector2 driveVector = new Vector2(-driveX, -driveY);
        driveVector = driveVector.multiply(driveSpeedMultiplier);
        double turnSpeed = gamepad1.left_stick_x * turnSpeedMultiplier;
        Drive.driveIndefinite(driveVector, turnSpeed);
    }
}
