package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.TensorFlowManager;
import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

@Autonomous(name = "CubeBoundFinder", group = "Linear OpMode")
public class CubeBoundFinder extends SingletonOpMode {

    private TensorFlowManager tfManager;

    @Override
    protected void onInitialize() {
        tfManager = new TensorFlowManager(hardwareMap);
        tfManager.initialize();
    }

    @Override
    protected void onStart() {
        while (opModeIsActive()) {
            locateGold();
        }
    }


    private void locateGold() {
        for (Mineral mineral : tfManager.getRecognizedMinerals()) {
            if (mineral.isGold()) {
                telemetry.addData("Left", mineral.getLeft());
                telemetry.addData("Right", mineral.getRight());
                telemetry.addData("Bottom", mineral.getBottom());
                telemetry.addData("Top", mineral.getTop());
                telemetry.update();
            }
        }
    }

}
