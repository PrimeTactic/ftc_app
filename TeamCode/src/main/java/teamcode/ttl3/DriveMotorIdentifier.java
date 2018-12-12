package teamcode.ttl3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "DriveMotorIdentifier", group = "Linear OpMode")
public class DriveMotorIdentifier extends LinearOpMode {

    private static final double POWER = 0.25;
    private static final long SLEEP_TIME = 5000;

    @Override
    public void runOpMode() {
        TTL3HardwareManager.initialize(hardwareMap);
        updateTelemetry();
        waitForStart();
        TTL3HardwareManager.frontLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL3HardwareManager.frontRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL3HardwareManager.backLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL3HardwareManager.backRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        updateTelemetry();
    }

    private void updateTelemetry() {
        telemetry.addData("front left power", TTL3HardwareManager.frontLeftDrive.getPower());
        telemetry.addData("front right power", TTL3HardwareManager.frontRightDrive.getPower());
        telemetry.addData("back left power", TTL3HardwareManager.backLeftDrive.getPower());
        telemetry.addData("back right power", TTL3HardwareManager.backRightDrive.getPower());
        telemetry.update();
    }

}
