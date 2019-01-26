package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;

@Autonomous(name = "TitaniumTalonsAutonomousCraterSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousCraterSide extends AbstractTitaniumTalonsAuto {

    @Override
    protected void driveToDepot() {
        switch (goldLocation) {
            case LEFT:
                Drive.turnDefinite(-60.0, 1.0);
                break;
            case MIDDLE:
                Drive.driveVerticalDefinite(-8.0, 1.0);
                Drive.driveLateralDefinite(-84.0, 1.0);
                Drive.turnDefinite(-135.0, 1.0);
                Drive.driveVerticalDefinite(12.0, 1.0);
                break;
            case RIGHT:
                break;
        }
    }

    @Override
    protected void driveToCrater() {
        Drive.driveVerticalDefinite(-60.0, 1.0);
    }

}
