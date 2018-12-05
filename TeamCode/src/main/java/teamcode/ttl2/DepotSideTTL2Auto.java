package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "DepotSideTTL2Auto", group = "Linear OpMode")
public class DepotSideTTL2Auto extends AbstractTTL2Auto {

    @Override
    protected void run() {
        lowerRobotFromLatch();
        driveLateral(5, 0.75);
        approachGold();
        //driveVertical(100, 0.75);
        // rotateArm(-LIFT_MOTOR_TICKS_TO_LOWER, -LOWER_POWER);
        //approachGold();
        // releaseMarker();
        //driveVertical(-25, 0.75);
        //turn(-0.75 * Math.PI);
        //driveVertical(200, 1.0);
    }

}
