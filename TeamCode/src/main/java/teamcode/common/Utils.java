package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Utils {

    public static boolean motorNearTarget(DcMotor motor, double ticksWithinTarget) {
        return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < ticksWithinTarget;
    }

}
