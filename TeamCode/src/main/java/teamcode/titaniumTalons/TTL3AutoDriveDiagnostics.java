package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TTL3AutoDriveDiagnostics", group = "Linear OpMode")
public class TTL3AutoDriveDiagnostics extends AbstractTTL3Auto {

    @Override
    protected void run() {
        setTelemetryStatus("driving forward");
        driveVertical(10, 0.5);
        setTelemetryStatus("drive backward");
        driveVertical(-10, 0.5);
        setTelemetryStatus("drive right");
        driveLateral(10, 0.5);
        setTelemetryStatus("drive left");
        driveLateral(-10, 0.5);
        setTelemetryStatus("turning clockwise");
        turn(360);
    }

    private void setTelemetryStatus(String status) {
        telemetry.addData("status", status);
        telemetry.update();
    }

}
