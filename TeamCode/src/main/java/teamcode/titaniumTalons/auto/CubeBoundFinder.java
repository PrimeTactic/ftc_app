package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.TensorFlowManager;
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
                telemetry.addData("Center", mineral.getVerticalCenter());
                telemetry.addData("Width", mineral.getVerticalWidth());
                telemetry.update();
            }
        }
    }

}
