package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import teamcode.titaniumTalons.Drive;
import teamcode.titaniumTalons.SingletonOpMode;

@Autonomous(name = "LateralMovementCalibrator", group = "Linear OpMode")
public class LateralMovementCalibrator extends SingletonOpMode {

    private static final double DISTANCE = 20.0;

    @Override
    protected void onInitialize() {
    }

    @Override
    protected void onStart() {
        telemetry.addData("Distance", DISTANCE);
        telemetry.update();
        Drive.driveLateralDefinite(DISTANCE, 0.1);
    }

}
