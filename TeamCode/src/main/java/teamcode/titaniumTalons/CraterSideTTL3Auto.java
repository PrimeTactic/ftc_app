package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterSideTTL3Auto", group = "Linear OpMode")
public class CraterSideTTL3Auto extends AbstractTTL3Auto {

    @Override
    protected void run() {
        lowerRobotFromLatch();
        knockGold();
        turn(-135.0);
        driveLateral(8.0, 0.75);
        driveVertical(48.0, 1.0);
        releaseMarker();
        driveVertical(-60, 1.0);
        fullyExtendArm();
        HardwareManager.intakeMotor.setPower(-1.0);
    }

    private void knockGold() {
        // allows the robot to stabilize
        sleep(500);
        if (goldMineralIsStraightAhead()) {
            // gold is in center
            driveVertical(22.0, 1.0);
            driveVertical(-8.0, 1.0);
            driveLateral(-35.0, 1.0);
        } else {
            turn(-20.0);
            // allows the robot to stabilize
            sleep(500);
            if (goldMineralIsStraightAhead()) {
                // gold is on left side
                driveVertical(24, 1.0);
                turn(20.0);
                driveVertical(-8.0, 0.75);
                driveLateral(-20, 1.0);
            } else {
                // gold was not detected or is on right side
                turn(50.0);
                driveVertical(26.0, 1.0);
                turn(-30.0);
                driveVertical(-8.0, 0.75);
                driveLateral(-48, 1.0);
            }
        }
    }
}
