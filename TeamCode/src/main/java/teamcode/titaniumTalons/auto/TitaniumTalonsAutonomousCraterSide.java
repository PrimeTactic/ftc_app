package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;
import teamcode.utils.Vector2;

@Autonomous(name = "TitaniumTalonsAutonomousCraterSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousCraterSide extends AbstractTitaniumTalonsAuto {

    private MineralLocation goldLocation;

    @Override
    protected void onStart() {
        Arm.lowerFromLatch();
        Drive.driveLateralDefinite(-5, 1.0);
        Drive.driveDefinite(new Vector2(-12, 12), 0.5, 1.0);
        releaseMarker();
//        goldLocation = super.sample();
//        dropOffMarker();
//        driveToCrater();
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
        double inches = -25.0;
        Drive.driveVerticallyDefinite(inches, 1.0);
    }

}
