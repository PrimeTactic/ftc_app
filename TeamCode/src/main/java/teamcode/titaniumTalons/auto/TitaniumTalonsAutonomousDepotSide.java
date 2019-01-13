package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;

//@Autonomous(name = "TitaniumTalonsAutonomousDepotSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousDepotSide extends AbstractTitaniumTalonsAuto {

    private MineralLocation goldLocation;

    @Override
    protected void onStart() {
        Arm.lowerFromLatch();
        goldLocation = super.sample();
        dropOffMarker();
        driveToCrater();
    }


    private void dropOffMarker() {
        switch (goldLocation) {
            case LEFT:
                break;
            case MIDDLE:
                break;
            case RIGHT:
                break;
        }
    }

    private void driveToCrater() {
        double inches = 25.0;
        Drive.driveVerticallyDefinite(inches, 1.0);
    }

}
