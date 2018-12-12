package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "DriveMotorIdentifier", group = "Linear OpMode")
public class DriveMotorIdentifier extends LinearOpMode {

    private static final double POWER = 0.25;
    private static final long SLEEP_TIME = 5000;

    @Override
    public void runOpMode() {
        TTL2HardwareManager.initialize(hardwareMap);
        updateTelemetry();
        waitForStart();
        TTL2HardwareManager.frontLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL2HardwareManager.frontRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL2HardwareManager.backLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        TTL2HardwareManager.backRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        updateTelemetry();
    }

    private void updateTelemetry() {
        telemetry.addData("front left power", TTL2HardwareManager.frontLeftDrive.getPower());
        telemetry.addData("front right power", TTL2HardwareManager.frontRightDrive.getPower());
        telemetry.addData("back left power", TTL2HardwareManager.backLeftDrive.getPower());
        telemetry.addData("back right power", TTL2HardwareManager.backRightDrive.getPower());
        telemetry.update();
    }

}
