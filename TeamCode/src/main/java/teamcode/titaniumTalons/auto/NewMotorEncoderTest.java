package teamcode.titaniumTalons.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

@Autonomous(name = "New Motor Encoder Test", group = "Linear OpMode")
public class NewMotorEncoderTest extends SingletonOpMode {

    @Override
    public void onInitialize() {
    }

    @Override
    public void onStart() {
        Drive.driveLateralDefinite(40, 0.4);
        //  Drive.turnDefinite(-360 * 3, 1.0);
    }

}
