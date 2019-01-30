package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Drive;

@Autonomous(name = "DepotSideAuto", group = "Linear OpMode")
public class DepotSideAuto extends AbstractAuto {

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
        Drive.turnDefinite(30.0, 1.0);
        Drive.driveVerticalDefinite(18.0, 1.0);
        Drive.turnDefinite(45.0, 1.0);
        Drive.driveVerticalDefinite(12.0, 1.0);
        Drive.driveVerticalDefinite(-12.0, 1.0);
    }

    private void goldMiddleDriveToDepot() {
        Drive.driveVerticalDefinite(24.0, 1.0);
        Drive.turnDefinite(45.0, 1.0);
        Drive.driveLateralDefinite(-12.0, 1.0);
        Drive.driveVerticalDefinite(-12.0, 1.0);
    }

    private void goldRightDriveToDepot() {
        Drive.turnDefinite(-35.0, 1.0);
        Drive.driveVerticalDefinite(36.0, 1.0);
        Drive.turnDefinite(45.0, 1.0);
        Drive.driveLateralDefinite(-12.0, 1.0);
        Drive.driveVerticalDefinite(-12.0, 1.0);
    }

}
