package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Drive;

@Autonomous(name = "TitaniumTalonsAutonomousCraterSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousCraterSide extends AbstractTitaniumTalonsAuto {

    @Override
    protected void driveToDepot() {
        switch (goldLocation) {
            case LEFT:
                goldLeftDriveToDepot();
                break;
            case MIDDLE:
                goldMiddleDriveToDepot();
                break;
            case RIGHT:
                goldRightDriveToDepot();
                break;
        }
    }

    private void goldLeftDriveToDepot() {
        Drive.turnDefinite(30, 1.0);
        Drive.driveLateralDefinite(-20.0, 1.0);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(10.0, 1.0);
        Drive.driveVerticalDefinite(48.0, 1.0);
    }

    private void goldMiddleDriveToDepot() {
        Drive.driveVerticalDefinite(-8.0, 1.0);
        Drive.driveLateralDefinite(-40.0, 1.0);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(11.0, 1.0);
        Drive.driveVerticalDefinite(44.0, 1.0);
    }

    private void goldRightDriveToDepot() {
        Drive.driveVerticalDefinite(-8.0, 1.0);
        Drive.turnDefinite(-35, 1.0);
        Drive.driveLateralDefinite(-60.0, 1.0);
        Drive.turnDefinite(-135.0, 1.0);
        Drive.driveLateralDefinite(11.0, 1.0);
        Drive.driveVerticalDefinite(33.0, 1.0);
    }

}
