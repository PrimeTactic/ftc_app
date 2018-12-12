package teamcode.ttl3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterSideTTL3Auto", group = "Linear OpMode")
public class CraterSideTTL3Auto extends AbstractTTL3Auto {

    @Override
    public void run() {
        lowerRobotFromLatch();
        knockGold();

        driveLateral(20, 0.75);
        driveVertical(100, 0.75);
    }

    private void knockGold() {
        if (goldMineralIsStraightAhead()) {
            // gold is in center
            driveVertical(22.0, 1.0);
            driveVertical(-10.0, 0.5);
            driveLateral(-46.0, 1.0);
        } else {
            turn(-30.0);
            driveLateral(3.0, 0.5);
            if (goldMineralIsStraightAhead()) {
                // gold is on left side
                driveLateral(-3.0, 1.0);
                driveVertical(24, 1.0);
                turn(30.0);
                driveVertical(-10.0, 0.5);
                driveLateral(-34, 1.0);
            } else {
                // gold was not detected or is on right side
                driveLateral(-3.0, 0.5);
                turn(60.0);
                driveVertical(24.0, 1.0);
                turn(-60.0);
                driveVertical(-10.0, 0.5);
                driveLateral(-68, 1.0);
            }
        }
        turn(-135.0);
    }

}
