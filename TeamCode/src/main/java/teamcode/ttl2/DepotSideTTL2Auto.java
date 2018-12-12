package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotSideTTL2Auto", group = "Linear OpMode")
public class DepotSideTTL2Auto extends AbstractTTL2Auto {

    @Override
    protected void run() {
        lowerRobotFromLatch();
        knockGold();

        driveVertical(28.0, 1.0);
        releaseMarker();
        driveVertical(-5.0, 1.0);
        driveVertical(5.0, 1.0);
        driveVertical(-48, 1.0);
        fullyExtendArm();
    }

}
