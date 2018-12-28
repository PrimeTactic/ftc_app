package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.tensorFlow.TensorFlowManager;

@Autonomous(name = "TitaniumTalonsAutonomousDepotSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousDepotSide extends SingletonOpMode {

    private TensorFlowManager tf;

    @Override
    protected void onInitialize() {
        tf = new TensorFlowManager(hardwareMap);
        tf.initialize();
}

    @Override
    protected void onStart() {

    }

}
