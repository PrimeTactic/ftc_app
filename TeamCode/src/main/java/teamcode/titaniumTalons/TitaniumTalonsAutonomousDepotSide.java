package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TitaniumTalonsAutonomousDepotSide", group = "Linear OpMode")
public class TitaniumTalonsAutonomousDepotSide extends SingletonOpMode {

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onStart() {
        Arm.retract();
    }

}
