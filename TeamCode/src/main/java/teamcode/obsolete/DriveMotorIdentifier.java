package teamcode.obsolete;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import teamcode.titaniumTalons.HardwareManager;

// @Autonomous(name = "DriveMotorIdentifier", group = "Linear OpMode")
public class DriveMotorIdentifier extends LinearOpMode {

    private static final double POWER = 0.25;
    private static final long SLEEP_TIME = 5000;

    @Override
    public void runOpMode() {
        HardwareManager.initialize(hardwareMap);
        updateTelemetry();
        waitForStart();
        HardwareManager.frontLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        HardwareManager.frontRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        HardwareManager.backLeftDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        HardwareManager.backRightDrive.setPower(POWER);
        updateTelemetry();
        sleep(SLEEP_TIME);
        updateTelemetry();
    }

    private void updateTelemetry() {
        telemetry.addData("front left power", HardwareManager.frontLeftDrive.getPower());
        telemetry.addData("front right power", HardwareManager.frontRightDrive.getPower());
        telemetry.addData("back left power", HardwareManager.backLeftDrive.getPower());
        telemetry.addData("back right power", HardwareManager.backRightDrive.getPower());
        telemetry.update();
    }

}
