package teamcode.tensorFlow;

import com.qualcomm.robotcore.hardware.DcMotor;

public class RobotUtils {

    public static boolean motorNearTarget(DcMotor motor, double ticksWithinTarget) {
        return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < ticksWithinTarget;
    }

}
