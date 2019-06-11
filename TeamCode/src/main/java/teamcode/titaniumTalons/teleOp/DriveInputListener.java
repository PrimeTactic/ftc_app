package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.hardware.Gamepad;

import teamcode.utils.Vector2;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

/**
 * Handles drive-related input during teleoperation.
 */
public class DriveInputListener {

    private static final double LOWER_DRIVE_SPEED_MULTIPLIER = 0.5;
    private static final double LOWER_TURN_SPEED_MULTIPLIER = 0.25;

    private Gamepad gamepad1;
    private Gamepad gamepad2;

    private boolean lowerDriveSpeedButtonDownLastUpdate;
    private boolean lowerTurnSpeedButtonDownLastUpdate;
    private boolean lowerDriveSpeed;
    private boolean lowerTurnSpeed;

    DriveInputListener() {
        gamepad1 = SingletonOpMode.instance.gamepad1;
        gamepad2 = SingletonOpMode.instance.gamepad2;
        new Thread() {

            @Override
            public void run() {
                while (SingletonOpMode.instance.opModeIsActive()) {
                    update();
                }
            }

        }.start();
    }

    private void update() {
        speedAdjustUpdate();
        driveUpdate();
    }


    private void speedAdjustUpdate() {
        boolean leftStickDown = gamepad1.left_stick_button || gamepad2.left_stick_button;
        if (leftStickDown) {
            if (!lowerTurnSpeedButtonDownLastUpdate) {
                lowerTurnSpeed = !lowerTurnSpeed;
            }
            lowerTurnSpeedButtonDownLastUpdate = true;
        } else {
            lowerTurnSpeedButtonDownLastUpdate = false;
        }

        boolean rightStickDown = gamepad1.right_stick_button || gamepad2.right_stick_button;
        if (rightStickDown) {
            if (!lowerDriveSpeedButtonDownLastUpdate) {
                lowerDriveSpeed = !lowerDriveSpeed;
            }
            lowerDriveSpeedButtonDownLastUpdate = true;
        } else {
            lowerDriveSpeedButtonDownLastUpdate = false;
        }
    }

    private void driveUpdate() {
        double driveSpeedMultiplier = lowerDriveSpeed ? LOWER_DRIVE_SPEED_MULTIPLIER : 1.0;
        double turnSpeedMultiplier = lowerTurnSpeed ? LOWER_TURN_SPEED_MULTIPLIER : 1.0;
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        driveX = 0.0f;
        Vector2 driveVector = new Vector2(-driveX, -driveY);
        driveVector = driveVector.multiply(driveSpeedMultiplier);
        double turnSpeed = gamepad1.left_stick_x * turnSpeedMultiplier;
        Drive.driveIndefinite(driveVector, turnSpeed);
    }
}
