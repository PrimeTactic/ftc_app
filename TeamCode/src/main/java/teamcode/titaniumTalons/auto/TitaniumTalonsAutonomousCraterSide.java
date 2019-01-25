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
        Drive.driveLateralDefinite(-5.0, 0.75);
        Arm.fullyRetract();
        Drive.driveLateralDefinite(5.0, 0.75);
        Drive.driveVerticalDefinite(-5.0, 0.75);

        goldLocation = super.sample();

        dropOffMarker();
        driveToCrater();
        Arm.extend();
    }

    private void dropOffMarker() {
        switch (goldLocation) {
            case LEFT:
                Drive.turnDefinite(-60.0, 0.75);
                break;
            case MIDDLE:
                Drive.driveVerticalDefinite(-8.0, 0.75);
                Drive.driveLateralDefinite(-48.0, 1.0);
                Drive.turnDefinite(-135.0, 0.75);
                Drive.driveLateralDefinite(6.0, 0.75);
                Drive.driveVerticalDefinite(36.0, 1.0);
                break;
            case RIGHT:
                break;
        }
        releaseMarker();
    }

    private void driveToCrater() {
        Drive.driveVerticalDefinite(-44.0, 1.0);
    }

}
