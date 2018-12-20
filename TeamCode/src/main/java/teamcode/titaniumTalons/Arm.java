package teamcode.titaniumTalons;

/**
 * Contains methods pertaining to the arm system of the robot.
 */
public final class Arm {

    private static ArmStatus status;

    public enum ArmStatus {
        EXTENDED, RETRACTED
    }

    public static ArmStatus getArmStatus() {
        return status;
    }

    public static void extend() {
        if (status == ArmStatus.EXTENDED) {
            throw new IllegalStateException("Arm is already extended!");
        }
    }

    public static void retract() {
        if (status == ArmStatus.RETRACTED) {
            throw new IllegalStateException("Arm is already retracted!");
        }

    }

    public void rotateArmBaseDefinite(double degrees, double power){

    }

    public void rotateArmBaseIndefinite(double power){

    }

}
