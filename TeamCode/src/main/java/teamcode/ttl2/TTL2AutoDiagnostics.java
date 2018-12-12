package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "TTL2AutoDiagnostics", group = "Linear OpMode")
public class TTL2AutoDiagnostics extends AbstractTTL2Auto {

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
