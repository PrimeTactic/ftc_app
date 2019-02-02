package teamcode.titaniumTalons;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.TimerTask;

/**
 * Contains methods pertaining to the arm system of the robot.
 */
public final class Arm {

    private static final double BASE_MOTOR_TICKS_PER_DEGREE = 5.73333333333;
    private static final double ELBOW_MOTOR_TICKS_PER_DEGREE = 4.0;

    public static ArmStatus status;

    public enum ArmStatus {
        LATCHED, UNLATCHED, EXTENDED, PARTIALLY_RETRACTED, FULLY_RETRACTED, CRANE
    }

    private Arm() {
        // do not instantiate
    }

    public static void extend() {
        if (status == ArmStatus.PARTIALLY_RETRACTED) {
            setWristServoPos(0.4);
            lockElbow();
            rotateArmBaseDefinite(105.0, 1.0);
        } else if (status == ArmStatus.FULLY_RETRACTED) {
            setWristServoPos(0.4);
            TimerTask rotateElbowTask = new TimerTask() {
                @Override
                public void run() {
                    rotateElbowDefinite(-100, 0.5);
                }
            };
            RobotTimer.schedule(rotateElbowTask, 0.4);
            rotateArmBaseDefinite(165, 1.0);
        } else if (status == ArmStatus.UNLATCHED) {
            TimerTask rotateElbowTask = new TimerTask() {
                @Override
                public void run() {
                    rotateElbowDefinite(150, 1.0);
                }
            };
            RobotTimer.schedule(rotateElbowTask, 0.4);
            rotateArmBaseDefinite(100, 1.0);
            setWristServoPos(0.4);
        } else if (status == ArmStatus.CRANE) {
            throw new UnsupportedOperationException();
        } else {
            throw new IllegalStateException("Arm cannot be extended!");
        }
        status = ArmStatus.EXTENDED;
    }

    public static void partiallyRetract() {
        if (status != ArmStatus.EXTENDED) {
            throw new IllegalStateException("Arm cannot be partially retracted!");
        }
        lockElbow();
        rotateArmBaseDefinite(-105.0, 1.0);
        setWristServoPos(0.7);
        status = ArmStatus.PARTIALLY_RETRACTED;
    }

    public static void fullyRetract() {
        if (status != ArmStatus.PARTIALLY_RETRACTED) {
            throw new IllegalStateException("Arm cannot be fully retracted");
        }
        setWristServoPos(0.0);
        rotateElbowDefinite(250.0, 1.0);

        rotateArmBaseDefinite(-60.0, 0.5);
        status = ArmStatus.FULLY_RETRACTED;
    }

    public static void crane() {
        if (status == ArmStatus.PARTIALLY_RETRACTED) {
            rotateArmBaseDefinite(25.0, 1.0);
            rotateElbowDefinite(0.0, 1.0);
            setWristServoPos(0.0);
        }
    }

    public static void lowerFromLatch() {
        if (status != ArmStatus.LATCHED) {
            throw new IllegalStateException("Robot is already lowered from latch!");
        }
        HardwareManager.leftArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        HardwareManager.rightArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        HardwareManager.pinServo.setPosition(0.5);
        // rotateArmBaseDefinite(90.0,0.5);
        SingletonOpMode.instance.sleep(1500);
        HardwareManager.leftArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HardwareManager.rightArmBaseMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        status = ArmStatus.UNLATCHED;
    }

    public static void lockElbow() {
        HardwareManager.armElbowMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        HardwareManager.armElbowMotor.setPower(0.0);
    }

    /**
     * @param degrees make positive to extend, negative to partiallyRetract
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
     * @param power make positive to extend, negative to partiallyRetract
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
     * @param degrees make positive to extend, negative to partiallyRetract
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
        lockElbow();
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

    /**
     * @param power make negative to intake
     */
    public static void setIntakePower(double power) {
        HardwareManager.intakeMotor.setPower(power);
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
