package teamcode.titaniumTalons;

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
    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = -22.7111913357;
    private static final double ARM_MOTOR_TICKS_PER_DEGREE = 6.0;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    private static final double LOWER_POWER = 0.25;
    private static final double TURN_POWER = 0.5;
    /**
     * In pixels.
     */
    private static final float MINERAL_OUT_OF_BOUNDS_THRESHOLD = 200;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        initialize();
        clenchArm();
        clenchMarkerClaw();
        waitForStart();
        run();
    }

    private void initialize() {
        HardwareManager.initialize(hardwareMap);
        resetDriveEncoders();
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
    }

    protected void driveVertical(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL);

        HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        HardwareManager.frontRightDrive.setTargetPosition(ticks);
        HardwareManager.backLeftDrive.setTargetPosition(ticks);
        HardwareManager.backRightDrive.setTargetPosition(ticks);

        HardwareManager.frontLeftDrive.setPower(power);
        HardwareManager.frontRightDrive.setPower(power);
        HardwareManager.backLeftDrive.setPower(power);
        HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    protected void driveLateral(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL);

        HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        HardwareManager.backRightDrive.setTargetPosition(ticks);

        HardwareManager.frontLeftDrive.setPower(power);
        HardwareManager.frontRightDrive.setPower(power);
        HardwareManager.backLeftDrive.setPower(power);
        HardwareManager.backRightDrive.setPower(power);

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

        HardwareManager.frontLeftDrive.setTargetPosition(-ticks);
        HardwareManager.frontRightDrive.setTargetPosition(ticks);
        HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        HardwareManager.backRightDrive.setTargetPosition(ticks);

        HardwareManager.frontLeftDrive.setPower(TURN_POWER);
        HardwareManager.frontRightDrive.setPower(TURN_POWER);
        HardwareManager.backLeftDrive.setPower(TURN_POWER);
        HardwareManager.backRightDrive.setPower(TURN_POWER);

        while (opModeIsActive() && !driveMotorsNearTarget()) ;
        resetDriveEncoders();
        zeroDriveMotorPower();
    }

    protected void lowerRobotFromLatch() {
        rotateArmBase(85.0, LOWER_POWER);
        driveLateral(5.0, 0.5);
        driveVertical(2, 0.5);
        driveLateral(-5.0, 0.5);
    }

    protected void rotateArmBase(double degrees, double power) {
        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int ticks = (int) (degrees * ARM_MOTOR_TICKS_PER_DEGREE);
        HardwareManager.leftArmBaseMotor.setTargetPosition(ticks);
        HardwareManager.rightArmBaseMotor.setTargetPosition(ticks);

        HardwareManager.leftArmBaseMotor.setPower(power);
        HardwareManager.rightArmBaseMotor.setPower(power);

        while (opModeIsActive() && !liftMotorsNearTarget()) ;
        HardwareManager.leftArmBaseMotor.setPower(0.0);
        HardwareManager.rightArmBaseMotor.setPower(0.0);
    }

    protected void zeroDriveMotorPower() {
        HardwareManager.frontLeftDrive.setPower(0.0);
        HardwareManager.frontRightDrive.setPower(0.0);
        HardwareManager.backLeftDrive.setPower(0.0);
        HardwareManager.backRightDrive.setPower(0.0);
    }

    protected void resetDriveEncoders() {
        HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HardwareManager.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        HardwareManager.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private boolean driveMotorsNearTarget() {
        int targetFrontLeftDriveMotorPos = HardwareManager.frontLeftDrive.getTargetPosition();
        int targetFrontRightDriveMotorPos = HardwareManager.frontRightDrive.getTargetPosition();
        int targetBackLeftDriveMotorPos = HardwareManager.backLeftDrive.getTargetPosition();
        int targetBackRightDriveMotorPos = HardwareManager.backRightDrive.getTargetPosition();


        int currentFrontLeftDriveMotorPos = HardwareManager.frontLeftDrive.getCurrentPosition();
        int currentFrontRightDriveMotorPos = HardwareManager.frontRightDrive.getCurrentPosition();
        int currentBackLeftDriveMotorPos = HardwareManager.backLeftDrive.getCurrentPosition();
        int currentBackRightDriveMotorPos = HardwareManager.backRightDrive.getCurrentPosition();

        return Math.abs(currentFrontLeftDriveMotorPos - targetFrontLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentFrontRightDriveMotorPos - targetFrontRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackLeftDriveMotorPos - targetBackLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackRightDriveMotorPos - targetBackRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    protected void fullyExtendArm() {
        HardwareManager.armWristServo.setPosition(0.05);
        rotateArmBase(85, 1.0);
    }

    private boolean liftMotorsNearTarget() {
        int targetLeftLiftMotorPos = HardwareManager.leftArmBaseMotor.getTargetPosition();
        int targetRightLiftMotorPos = HardwareManager.rightArmBaseMotor.getTargetPosition();

        int currentLeftLiftMotorPos = HardwareManager.leftArmBaseMotor.getCurrentPosition();
        int currentRightLiftMotorPos = HardwareManager.rightArmBaseMotor.getCurrentPosition();

        return Math.abs(currentLeftLiftMotorPos - targetLeftLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentRightLiftMotorPos - targetRightLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private void clenchArm() {
        HardwareManager.armWristServo.setPosition(0.95);
    }

    protected void clenchMarkerClaw() {
        HardwareManager.markerClawServo.setPosition(1.0);
        waitForMarkerClawServo();
    }

    protected void releaseMarker() {
        HardwareManager.markerClawServo.setPosition(0.0);
        waitForMarkerClawServo();
    }

    private void waitForMarkerClawServo() {
        sleep(750);
    }

    protected boolean goldMineralIsStraightAhead() {
        // sometimes the recognized minerals list is null or inaccurate, so must be checked many times
        for (int i = 0; i < 500000; i++) {
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
