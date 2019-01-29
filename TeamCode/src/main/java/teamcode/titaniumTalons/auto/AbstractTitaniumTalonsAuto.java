package teamcode.titaniumTalons.auto;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.MineralCriteria;
import teamcode.tensorFlow.TensorFlowManager;
import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.SingletonOpMode;

public abstract class AbstractTitaniumTalonsAuto extends SingletonOpMode {

    // resolution is 720 x 1280
    private static final MineralCriteria LEFT_MINERAL_CRITERIA = new MineralCriteria(950, 1280, 75, 250);
    private static final MineralCriteria MIDDLE_MINERAL_CRITERIA = new MineralCriteria(250, 600, 75, 250);

    protected MineralLocation goldLocation;

    private TensorFlowManager tfManager;

    @Override
    protected void onInitialize() {
        tfManager = new TensorFlowManager(hardwareMap);
        tfManager.initialize();
        Arm.status = Arm.ArmStatus.LATCHED;
    }

    @Override
    protected void onStart() {
        Arm.lowerFromLatch();
        Drive.driveLateralDefinite(-5.0, 1.0);
        Drive.driveVerticalDefinite(2.0, 1.0);
        Drive.driveLateralDefinite(5.0, 1.0);

        goldLocation = locateGold();
        telemetry.addData("Gold Location", goldLocation);
        telemetry.update();

        sample();

        driveToDepot();
        releaseMarker();
        driveToCrater();
        Arm.extend();
        Arm.setIntakePower(-1.0);
    }

    protected void sample() {
        goldLocation = locateGold();

        switch (goldLocation) {
            case LEFT:
                Drive.turnDefinite(-30.0, 1.0);
                Drive.driveVerticalDefinite(27.0, 1.0);
                break;
            case MIDDLE:
                Drive.driveVerticalDefinite(25.0, 1.0);
                break;
            case RIGHT:
                Drive.turnDefinite(35.0, 1.0);
                Drive.driveVerticalDefinite(27.0, 1.0);
                break;
        }
    }

    private MineralLocation locateGold() {
        for (Mineral mineral : tfManager.getRecognizedMinerals()) {
            if (mineral.isGold()) {
                if (mineral.matchesCriteria(LEFT_MINERAL_CRITERIA)) {
                    return MineralLocation.LEFT;
                } else if (mineral.matchesCriteria(MIDDLE_MINERAL_CRITERIA)) {
                    return MineralLocation.MIDDLE;
                }
            }
        }
        // the default
        return MineralLocation.RIGHT;
    }

    protected abstract void driveToDepot();

    private void releaseMarker() {
        HardwareManager.pinServo.setPosition(0.0);
        sleep(500);
    }

    private void driveToCrater() {
        Drive.driveVerticalDefinite(-60.0, 1.0);
    }

    protected enum MineralLocation {
        LEFT, MIDDLE, RIGHT
    }

}
