package teamcode.ttl3;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import teamcode.tensorFlow.Mineral;
import teamcode.tensorFlow.TensorFlowManager;

public abstract class AbstractTTL3Auto extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL = 92.2;
    private static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL = 104.0;
    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = -23.3;
    private static final double ARM_MOTOR_TICKS_PER_DEGREE = 6.0;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    private static final double LOWER_POWER = 0.25;
    private static final double TURN_POWER = 0.5;
    /**
     * In pixels.
     */
    private static final float MINERAL_OUT_OF_BOUNDS_THRESHOLD = 500;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        initialize();
        clenchArm();
        clenchMarkerClaw();
        waitForStart();
        run();
        while (opModeIsActive()) ;
    }

    private void initialize() {
        TTL3HardwareManager.initialize(hardwareMap);
        resetDriveEncoders();
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
    }

    protected void driveVertical(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL);

        TTL3HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL3HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL3HardwareManager.backLeftDrive.setTargetPosition(ticks);
        TTL3HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL3HardwareManager.frontLeftDrive.setPower(power);
        TTL3HardwareManager.frontRightDrive.setPower(power);
        TTL3HardwareManager.backLeftDrive.setPower(power);
        TTL3HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    protected void driveLateral(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL);

        TTL3HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL3HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        TTL3HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL3HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL3HardwareManager.frontLeftDrive.setPower(power);
        TTL3HardwareManager.frontRightDrive.setPower(power);
        TTL3HardwareManager.backLeftDrive.setPower(power);
        TTL3HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    /**
     * @param degrees degrees to turn clockwise
     */
    protected void turn(double degrees) {
        zeroDriveMotorPower();
        int ticks = (int) (degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);

        TTL3HardwareManager.frontLeftDrive.setTargetPosition(-ticks);
        TTL3HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL3HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL3HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL3HardwareManager.frontLeftDrive.setPower(TURN_POWER);
        TTL3HardwareManager.frontRightDrive.setPower(TURN_POWER);
        TTL3HardwareManager.backLeftDrive.setPower(TURN_POWER);
        TTL3HardwareManager.backRightDrive.setPower(TURN_POWER);

        while (opModeIsActive() && !driveMotorsNearTarget()) ;
        resetDriveEncoders();
        zeroDriveMotorPower();
    }

    protected void lowerRobotFromLatch() {
        rotateArmBase(85.0, LOWER_POWER);
        turn(10);
        driveLateral(5.0, 0.5);
        driveVertical(2, 0.5);
        driveLateral(-5.0, 0.5);
    }

    protected void rotateArmBase(double degrees, double power) {
        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL3HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int ticks = (int) (degrees * ARM_MOTOR_TICKS_PER_DEGREE);
        TTL3HardwareManager.leftArmBaseMotor.setTargetPosition(ticks);
        TTL3HardwareManager.rightArmBaseMotor.setTargetPosition(ticks);

        TTL3HardwareManager.leftArmBaseMotor.setPower(power);
        TTL3HardwareManager.rightArmBaseMotor.setPower(power);

        while (opModeIsActive() && !liftMotorsNearTarget()) ;
        TTL3HardwareManager.leftArmBaseMotor.setPower(0.0);
        TTL3HardwareManager.rightArmBaseMotor.setPower(0.0);
    }

    protected void zeroDriveMotorPower() {
        TTL3HardwareManager.frontLeftDrive.setPower(0.0);
        TTL3HardwareManager.frontRightDrive.setPower(0.0);
        TTL3HardwareManager.backLeftDrive.setPower(0.0);
        TTL3HardwareManager.backRightDrive.setPower(0.0);
    }

    protected void resetDriveEncoders() {
        TTL3HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL3HardwareManager.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL3HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL3HardwareManager.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private boolean driveMotorsNearTarget() {
        int targetFrontLeftDriveMotorPos = TTL3HardwareManager.frontLeftDrive.getTargetPosition();
        int targetFrontRightDriveMotorPos = TTL3HardwareManager.frontRightDrive.getTargetPosition();
        int targetBackLeftDriveMotorPos = TTL3HardwareManager.backLeftDrive.getTargetPosition();
        int targetBackRightDriveMotorPos = TTL3HardwareManager.backRightDrive.getTargetPosition();


        int currentFrontLeftDriveMotorPos = TTL3HardwareManager.frontLeftDrive.getCurrentPosition();
        int currentFrontRightDriveMotorPos = TTL3HardwareManager.frontRightDrive.getCurrentPosition();
        int currentBackLeftDriveMotorPos = TTL3HardwareManager.backLeftDrive.getCurrentPosition();
        int currentBackRightDriveMotorPos = TTL3HardwareManager.backRightDrive.getCurrentPosition();

        return Math.abs(currentFrontLeftDriveMotorPos - targetFrontLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentFrontRightDriveMotorPos - targetFrontRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackLeftDriveMotorPos - targetBackLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackRightDriveMotorPos - targetBackRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    protected void fullyExtendArm() {
        TTL3HardwareManager.leftArmElbowServo.setPosition(1.0);
        TTL3HardwareManager.rightArmElbowServo.setPosition(1.0);
        Timer timer = new Timer();
        TimerTask stopElbowServoTask = new TimerTask() {

            @Override
            public void run() {
                TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
                TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
            }

        };
        timer.schedule(stopElbowServoTask, 3625);

        TTL3HardwareManager.armWristServo.setPosition(0.05);
        rotateArmBase(85, 1.0);
    }

    private boolean liftMotorsNearTarget() {
        int targetLeftLiftMotorPos = TTL3HardwareManager.leftArmBaseMotor.getTargetPosition();
        int targetRightLiftMotorPos = TTL3HardwareManager.rightArmBaseMotor.getTargetPosition();

        int currentLeftLiftMotorPos = TTL3HardwareManager.leftArmBaseMotor.getCurrentPosition();
        int currentRightLiftMotorPos = TTL3HardwareManager.rightArmBaseMotor.getCurrentPosition();

        return Math.abs(currentLeftLiftMotorPos - targetLeftLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentRightLiftMotorPos - targetRightLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private void clenchArm() {
        TTL3HardwareManager.leftArmElbowServo.setPosition(0.5);
        TTL3HardwareManager.rightArmElbowServo.setPosition(0.5);
        TTL3HardwareManager.armWristServo.setPosition(0.95);
    }

    protected void clenchMarkerClaw() {
        TTL3HardwareManager.markerClawServo.setPosition(1.0);
        waitForMarkerClawServo();
    }

    protected void releaseMarker() {
        TTL3HardwareManager.markerClawServo.setPosition(0.0);
        waitForMarkerClawServo();
    }

    private void waitForMarkerClawServo() {
        sleep(750);
    }

    protected boolean goldMineralIsStraightAhead() {
        // sometimes the recognized minerals list is null, so must be checked many times
        while (true) {
            List<Mineral> minerals = this.tfManager.getRecognizedMinerals();
            if (minerals != null) {
                for (Mineral mineral : minerals) {
                    if (mineral.isGold()) {
                        // vertical and horizontal values are swapped due to landscape orientation
                        float leftBound = mineral.getTop();
                        float rightBound = mineral.getBottom();
                        if (boundingBoxIsInCenterOfView(leftBound, rightBound)) {
                            return true;
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    private boolean boundingBoxIsInCenterOfView(float leftBound, float rightBound) {
        float boundingBoxCenter = leftBound + rightBound / 2;
        float bbCenterOffsetFromFOVCenter = Math.abs(1280 / 2 - boundingBoxCenter);
        return bbCenterOffsetFromFOVCenter < MINERAL_OUT_OF_BOUNDS_THRESHOLD;
    }

    /**
     * Invoked immediately after start.
     */
    protected abstract void run();

}
