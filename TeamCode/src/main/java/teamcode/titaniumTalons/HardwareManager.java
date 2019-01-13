package teamcode.titaniumTalons;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * A class that provides references to the hardware components of the robot.
 */
public final class HardwareManager {

    private static final String FRONT_LEFT_DRIVE_NAME = "FrontLeftDrive";
    private static final String FRONT_RIGHT_DRIVE_NAME = "FrontRightDrive";
    private static final String BACK_LEFT_DRIVE_NAME = "BackLeftDrive";
    private static final String BACK_RIGHT_DRIVE_NAME = "BackRightDrive";
    private static final String LEFT_ARM_BASE_MOTOR_NAME = "LeftArmBaseMotor";
    private static final String RIGHT_ARM_BASE_MOTOR_NAME = "RightArmBaseMotor";
    private static final String ARM_ELBOW_MOTOR_NAME = "ArmElbowMotor";
    private static final String LEFT_ARM_WRIST_SERVO_NAME = "LeftArmWristServo";
    private static final String RIGHT_ARM_WRIST_SERVO_NAME = "RightArmWristServo";
    private static final String INTAKE_MOTOR_NAME = "IntakeMotor";
    private static final String INTAKE_GATE_SERVO_NAME = "IntakeGateServo";
    private static final String PIN_SERVO_NAME = "PinServo";

    // drive hardware
    public static DcMotor frontLeftDrive;
    public static DcMotor frontRightDrive;
    public static DcMotor backLeftDrive;
    public static DcMotor backRightDrive;

    // arm hardware
    public static DcMotor leftArmBaseMotor;
    public static DcMotor rightArmBaseMotor;
    public static DcMotor armElbowMotor;
    public static Servo leftArmWristServo;
    public static Servo rightArmWristServo;
    public static DcMotor intakeMotor;
    public static Servo intakeGateServo;
    public static Servo pinServo;

    public static void initialize(HardwareMap hardwareMap) {
        frontLeftDrive = hardwareMap.get(DcMotor.class, FRONT_LEFT_DRIVE_NAME);
        frontRightDrive = hardwareMap.get(DcMotor.class, FRONT_RIGHT_DRIVE_NAME);
        backLeftDrive = hardwareMap.get(DcMotor.class, BACK_LEFT_DRIVE_NAME);
        backRightDrive = hardwareMap.get(DcMotor.class, BACK_RIGHT_DRIVE_NAME);

        leftArmBaseMotor = hardwareMap.get(DcMotor.class, LEFT_ARM_BASE_MOTOR_NAME);
        rightArmBaseMotor = hardwareMap.get(DcMotor.class, RIGHT_ARM_BASE_MOTOR_NAME);
        armElbowMotor = hardwareMap.get(DcMotor.class, ARM_ELBOW_MOTOR_NAME);
        leftArmWristServo = hardwareMap.get(Servo.class, LEFT_ARM_WRIST_SERVO_NAME);
        rightArmWristServo = hardwareMap.get(Servo.class, RIGHT_ARM_WRIST_SERVO_NAME);
        intakeMotor = hardwareMap.get(DcMotor.class, INTAKE_MOTOR_NAME);
        intakeGateServo = hardwareMap.get(Servo.class, INTAKE_GATE_SERVO_NAME);
        pinServo = hardwareMap.get(Servo.class, PIN_SERVO_NAME);

        correctDirections();
    }

    private static void correctDirections() {
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftArmWristServo.setDirection(Servo.Direction.REVERSE);
    }

}
