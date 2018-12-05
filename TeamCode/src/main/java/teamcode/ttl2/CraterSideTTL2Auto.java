package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CraterSideTTL2Auto", group = "Linear OpMode")
public class CraterSideTTL2Auto extends AbstractTTL2Auto{

    @Override
    public void run(){
        lowerRobotFromLatch();
        driveLateral(20, 0.75);
        driveVertical(100, 0.75);
    }

}
