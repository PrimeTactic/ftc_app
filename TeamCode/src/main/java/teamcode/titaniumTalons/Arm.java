package teamcode.titaniumTalons;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Contains methods pertaining to the arm system of the robot.
 */
public final class Arm {

    private static final double BASE_MOTOR_TICKS_PER_DEGREE = 5.73333333333;
    private static final double ELBOW_MOTOR_TICKS_PER_DEGREE = 0.8;

    public static ArmStatus status;

    private RobotTimer timer;

    public enum ArmStatus {
        LATCHED, EXTENDED, RETRACTED
    }

    public static void extend() {
        if (status != ArmStatus.RETRACTED) {
            throw new IllegalStateException("Arm cannot be extended!");
        }
        closeIntakeGate();
        setWristServoPos(0.4);
        lockElbow();
        rotateArmBaseDefinite(100.0, 1.0);
        status = ArmStatus.EXTENDED;
    }

    public static void retract() {
        if (status != ArmStatus.EXTENDED) {
            throw new IllegalStateException("Arm cannot be retracted!");
        }
        lockElbow();
        rotateArmBaseDefinite(-100.0, 1.0);
        setWristServoPos(0.7);
        status = ArmStatus.RETRACTED;
    }

    public static void lowerFromLatch() {
        if (status != ArmStatus.LATCHED) {
            throw new IllegalStateException("Robot is already lowered from latch!");
        }
        HardwareManager.leftArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        HardwareManager.rightArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        HardwareManager.pinServo.setPosition(0.5);
        // rotateArmBaseDefinite(90.0,0.5);
        SingletonOpMode.instance.sleep(5000);
        HardwareManager.leftArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HardwareManager.rightArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        status = ArmStatus.EXTENDED;
    }

    public static void lockElbow() {
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        HardwareManager.armElbowMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HardwareManager.armElbowMotor.setPower(0.0);
    }

    /**
     * @param degrees make positive to extend, negative to retract
     * @param power
     */
    public static void rotateArmBaseDefinite(double degrees, double power) {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int pos = (int) (degrees * BASE_MOTOR_TICKS_PER_DEGREE);
        HardwareManager.leftArmBaseMotor.setTargetPosition(pos);
        HardwareManager.rightArmBaseMotor.setTargetPosition(pos);
        HardwareManager.leftArmBaseMotor.setPower(power);
        HardwareManager.rightArmBaseMotor.setPower(power);
        while (SingletonOpMode.active() &&
                (HardwareManager.leftArmBaseMotor.isBusy() ||
                        HardwareManager.rightArmBaseMotor.isBusy())) ;
    }

    /**
     * @param power make positive to extend, negative to retract
     */
    public static void rotateArmBaseIndefinite(double power) {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        HardwareManager.leftArmBaseMotor.setPower(power);
        HardwareManager.rightArmBaseMotor.setPower(power);
    }

    public static void lockBaseMotors() {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        HardwareManager.leftArmBaseMotor.setPower(0.0);
        HardwareManager.rightArmBaseMotor.setPower(0.0);

        HardwareManager.leftArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HardwareManager.rightArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * @param degrees make positive to extend, negative to retract
     * @param power
     */
    public static void rotateElbowDefinite(double degrees, double power) {
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int pos = (int) (degrees * ELBOW_MOTOR_TICKS_PER_DEGREE);
        HardwareManager.armElbowMotor.setTargetPosition(pos);
        HardwareManager.armElbowMotor.setPower(power);
        while (SingletonOpMode.active() &&
                HardwareManager.armElbowMotor.isBusy()) ;
    }

    public static void rotateElbowIndefinite(double power) {
        HardwareManager.armElbowMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        HardwareManager.armElbowMotor.setPower(power);
    }

    public static double getWristServoPos() {
        return HardwareManager.leftArmWristServo.getPosition();
    }

    /**
     * @param pos [0.0, 1.0] make higher to extend
     */
    public static void setWristServoPos(double pos) {
        HardwareManager.leftArmWristServo.setPosition(pos);
        HardwareManager.rightArmWristServo.setPosition(pos);
    }

    /**
     * Opens the intake gate, allowing balls to exit the intake chamber.
     */
    public static void openIntakeGate() {
        HardwareManager.intakeGateServo.setPosition(0.5);
    }

    /**
     * Closes the intake gate, trapping balls inside the intake chamber.
     */
    public static void closeIntakeGate() {
        HardwareManager.intakeGateServo.setPosition(0.05);
    }

    public static void setIntakePower(final double power) {
        HardwareManager.intakeMotor.setPower(power);
//        HardwareManager.intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        HardwareManager.intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        new Thread(){
//            @Override
//            public void run(){
//                HardwareManager.intakeMotor.setTargetPosition(0);
//                HardwareManager.intakeMotor.setPower(power);
//            }
//        }.start();
    }

    /**
     * Locks the arm's position and closes the intake gate.
     */
    public static void initialize() {
        lockBaseMotors();
        lockElbow();
        closeIntakeGate();
        HardwareManager.pinServo.setPosition(1.0);
    }

}
