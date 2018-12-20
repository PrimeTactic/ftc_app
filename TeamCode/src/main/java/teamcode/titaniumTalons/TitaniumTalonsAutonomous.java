package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TitaniumTalonsAutonomous", group = "Linear OpMode")
public class TitaniumTalonsAutonomous extends SingletonOpMode {

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onStart() {
        Drive.turnDefinite(5.0, 1.0);
    }

}
