package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

@Autonomous(name = "VerticalMovementCalibrator", group = "Linear OpMode")
public class VerticalMovementCalibrator extends SingletonOpMode {

    private static final double DISTANCE = 65.0;

    @Override
    protected void onInitialize() {
    }

    @Override
    protected void onStart() {
        telemetry.addData("Distance", DISTANCE);
        telemetry.update();
        Drive.driveVerticalDefinite(DISTANCE, 0.75);
    }

}
