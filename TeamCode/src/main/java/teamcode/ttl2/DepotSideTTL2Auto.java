package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotSideTTL2Auto", group = "Linear OpMode")
public class DepotSideTTL2Auto extends AbstractTTL2Auto {

    @Override
    protected void run() {
        lowerRobotFromLatch();
        driveLateral(-3.0, 0.5);
        driveVertical(5, 0.5);
        approachGold();
    }

}
