package teamcode.titaniumTalons.auto;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.TensorFlowManager;
import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.SingletonOpMode;

public abstract class AbstractTitaniumTalonsAuto extends SingletonOpMode {

    private TensorFlowManager tfManager;

    @Override
    protected void onInitialize() {
        tfManager = new TensorFlowManager(hardwareMap);
        tfManager.initialize();
        Arm.status = Arm.ArmStatus.RETRACTED;
    }

    /**
     * @return the location that the gold mineral was at
     */
    protected MineralLocation sample() {
        if (goldAhead()) {
            double driveDist = 25.0;
            Drive.driveVerticallyDefinite(driveDist, 0.75);
            return MineralLocation.MIDDLE;
        }

        Drive.turnDefinite(-30.0, 0.5);
        if (goldAhead()) {
            double driveDist = 25.0;
            Drive.driveVerticallyDefinite(driveDist, 0.75);
            return MineralLocation.LEFT;
        }

        Drive.turnDefinite(60, 0.5);
        double driveDist = 25.0;
        Drive.driveVerticallyDefinite(driveDist, 0.75);
        return MineralLocation.RIGHT;
    }

    private boolean goldAhead() {
        for (Mineral mineral : tfManager.getRecognizedMinerals()) {
            if (mineral.isGold()) {
                return true;
            }
        }
        return false;
    }

    protected void releaseMarker() {
        HardwareManager.pinServo.setPosition(0.0);
    }

    protected enum MineralLocation {
        LEFT, MIDDLE, RIGHT
    }

}
