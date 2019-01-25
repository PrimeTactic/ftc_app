package teamcode.titaniumTalons.auto;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.MineralCriteria;
import teamcode.tensorFlow.TensorFlowManager;
import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.SingletonOpMode;

public abstract class AbstractTitaniumTalonsAuto extends SingletonOpMode {

    private TensorFlowManager tfManager;

    private static final MineralCriteria LEFT_MINERAL_CRITERIA = new MineralCriteria(0, 200, 25, 50);
    private static final MineralCriteria MIDDLE_MINERAL_CRITERIA = new MineralCriteria(500, 900, 10, 100);
    private static final MineralCriteria RIGHT_MINERAL_CRITERIA = new MineralCriteria(1000, 1280, 25, 50);

    @Override
    protected void onInitialize() {
        tfManager = new TensorFlowManager(hardwareMap);
        tfManager.initialize();
        Arm.status = Arm.ArmStatus.LATCHED;
    }

    /**
     * @return the location that the gold mineral was at
     */
    protected MineralLocation sample() {
        MineralLocation goldLoc = locateGold();

        switch (goldLoc) {
            case LEFT:
                Drive.turnDefinite(-30.0, 1.0);
                Drive.driveVerticalDefinite(30.0, 1.0);
                break;
            case MIDDLE:
                Drive.driveVerticalDefinite(28.0, 1.0);
                break;
            case RIGHT:
                Drive.turnDefinite(30.0, 1.0);
                Drive.driveVerticalDefinite(30.0, 1.0);
                break;
        }

        return goldLoc;
    }

    private MineralLocation locateGold() {
        return MineralLocation.MIDDLE;
//        for (Mineral mineral : tfManager.getRecognizedMinerals()) {
//            if (mineral.isGold()) {
//                if (mineral.matchesCriteria(LEFT_MINERAL_CRITERIA)) {
//                    return MineralLocation.LEFT;
//                } else if (mineral.matchesCriteria(MIDDLE_MINERAL_CRITERIA)) {
//                    return MineralLocation.MIDDLE;
//                } else if (mineral.matchesCriteria(RIGHT_MINERAL_CRITERIA)) {
//                    return MineralLocation.RIGHT;
//                }
//            }
//        }
//        return MineralLocation.RIGHT;
    }

    protected void releaseMarker() {
        HardwareManager.pinServo.setPosition(0.0);
    }

    protected enum MineralLocation {
        LEFT, MIDDLE, RIGHT
    }

}
