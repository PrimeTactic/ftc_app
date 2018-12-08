package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "DriveMotorIdentifier", group = "Linear OpMode")
public class DriveMotorIdentifier extends LinearOpMode {

    @Override
    public void runOpMode() {
        TTL2HardwareManager.initialize(hardwareMap);
        updateTelemetry();
        waitForStart();
        TTL2HardwareManager.frontLeftDrive.setPower(1.0);
        updateTelemetry();
        sleep(5000);
        TTL2HardwareManager.frontRightDrive.setPower(1.0);
        updateTelemetry();
        sleep(5000);
        TTL2HardwareManager.backLeftDrive.setPower(1.0);
        updateTelemetry();
        sleep(5000);
        TTL2HardwareManager.backRightDrive.setPower(1.0);
        updateTelemetry();
        sleep(5000);
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
