package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;

@Autonomous(name = "TitaniumTalonsAutonomousCraterSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousCraterSide extends AbstractTitaniumTalonsAuto {

    private MineralLocation goldLocation;

    @Override
    protected void onStart() {
        Arm.lowerFromLatch();
        Drive.driveLateralDefinite(-5, 1.0);
        goldLocation = super.sample();

//        dropOffMarker();
//        driveToCrater();
    }


    private void dropOffMarker() {
        switch (goldLocation) {
            case LEFT:
                Drive.turnDefinite(-60.0, 0.75);
                break;
            case MIDDLE:
                Drive.turnDefinite(-90.0, 0.75);
                Drive.driveVerticalDefinite(36.0, 1.0);
                break;
            case RIGHT:
                break;
        }
        Drive.turnDefinite(-45.0, 0.75);
        Drive.driveVerticalDefinite(48.0, 1.0);
        releaseMarker();
    }

    private void driveToCrater() {
        double inches = -25.0;
        Drive.driveVerticalDefinite(inches, 1.0);
    }

}
