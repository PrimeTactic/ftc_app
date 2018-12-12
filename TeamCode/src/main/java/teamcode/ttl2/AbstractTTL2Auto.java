package teamcode.ttl2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;

public abstract class AbstractTTL2Auto extends LinearOpMode {

    protected static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL = 92.2;
    protected static final double DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL = 104.0;
    protected static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = -23.3;
    private static final double ARM_MOTOR_TICKS_PER_DEGREE = 6.0;
    protected static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    protected static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 25;
    protected static final double LOWER_POWER = 0.25;
    protected static final double TURN_POWER = 0.5;
    /**
     * In pixels.
     */
    private static final float MINERAL_OUT_OF_BOUNDS_THRESHOLD = 750;

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
        TTL2HardwareManager.initialize(hardwareMap);
        resetDriveEncoders();
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
    }

    private void clenchArm() {
        TTL2HardwareManager.leftArmElbowServo.setPosition(0.5);
        TTL2HardwareManager.rightArmElbowServo.setPosition(0.5);
        TTL2HardwareManager.armWristServo.setPosition(1.0);
    }

    protected void knockGold() {
        if (goldMineralIsStraightAhead()) {
            // gold is in center
            driveVertical(22.0, 1.0);
            driveVertical(-10.0, 0.5);
            driveLateral(-46.0, 1.0);
        } else {
            turn(-30.0);
            driveLateral(3.0, 0.5);
            if (goldMineralIsStraightAhead()) {
                // gold is on left side
                driveLateral(-3.0, 1.0);
                driveVertical(24, 1.0);
                turn(30.0);
                driveVertical(-10.0, 0.5);
                driveLateral(-34, 1.0);
            } else {
                // gold was not detected or is on right side
                driveLateral(-3.0, 0.5);
                turn(60.0);
                driveVertical(24.0, 1.0);
                turn(-60.0);
                driveVertical(-10.0, 0.5);
                driveLateral(-68, 1.0);
            }
        }
        turn(-135.0);
    }

    private boolean goldMineralIsStraightAhead() {
        // sometimes the recognized minerals list is null, so must be checked many times
        for (int i = 0; i < 10000; i++) {
            telemetry.addData("gold located?", false);
            telemetry.update();

            List<Mineral> minerals = this.tfManager.getRecognizedMinerals();
            if (minerals != null) {
                for (Mineral mineral : minerals) {
                    if (mineral.isGold()) {
                        telemetry.addData("gold located?", true);
                        telemetry.update();
                        // vertical and horizontal values are swapped due to landscape orientation
                        float leftBound = mineral.getTop();
                        float rightBound = mineral.getBottom();
                        if (boundingBoxIsInCenterOfView(leftBound, rightBound)) {
                            return true;
                        }
                    }
                }
                telemetry.addData("gold located?", false);
                telemetry.update();
            }
        }
        return false;
    }

    private boolean boundingBoxIsInCenterOfView(float leftBound, float rightBound) {
        float boundingBoxCenter = leftBound + rightBound / 2;
        float bbCenterOffsetFromFOVCenter = Math.abs(1280 / 2 - boundingBoxCenter);
        return bbCenterOffsetFromFOVCenter < MINERAL_OUT_OF_BOUNDS_THRESHOLD;
    }

    protected void clenchMarkerClaw() {
        TTL2HardwareManager.markerClawServo.setPosition(1.0);
        waitForMarkerClawServo();
    }

    protected void releaseMarker() {
        TTL2HardwareManager.markerClawServo.setPosition(0.0);
        waitForMarkerClawServo();
    }

    private void waitForMarkerClawServo() {
        sleep(750);
    }


    protected void driveVertical(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_VERTICAL);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) {
            printDriveTelemetry();
        }
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    protected void driveLateral(double inches, double power) {
        zeroDriveMotorPower();
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCHES_COVERED_LATERAL);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(power);
        TTL2HardwareManager.frontRightDrive.setPower(power);
        TTL2HardwareManager.backLeftDrive.setPower(power);
        TTL2HardwareManager.backRightDrive.setPower(power);

        while (!driveMotorsNearTarget() && opModeIsActive()) {
            printDriveTelemetry();
        }
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    /**
     * @param degrees degrees to turn clockwise
     */
    protected void turn(double degrees) {
        zeroDriveMotorPower();
        int ticks = (int) (degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);

        TTL2HardwareManager.frontLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.frontRightDrive.setTargetPosition(ticks);
        TTL2HardwareManager.backLeftDrive.setTargetPosition(-ticks);
        TTL2HardwareManager.backRightDrive.setTargetPosition(ticks);

        TTL2HardwareManager.frontLeftDrive.setPower(TURN_POWER);
        TTL2HardwareManager.frontRightDrive.setPower(TURN_POWER);
        TTL2HardwareManager.backLeftDrive.setPower(TURN_POWER);
        TTL2HardwareManager.backRightDrive.setPower(TURN_POWER);

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
        TTL2HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL2HardwareManager.leftArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.rightArmBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        int ticks = (int) (degrees * ARM_MOTOR_TICKS_PER_DEGREE);
        TTL2HardwareManager.leftArmBaseMotor.setTargetPosition(ticks);
        TTL2HardwareManager.rightArmBaseMotor.setTargetPosition(ticks);

        TTL2HardwareManager.leftArmBaseMotor.setPower(power);
        TTL2HardwareManager.rightArmBaseMotor.setPower(power);

        while (opModeIsActive() && !liftMotorsNearTarget()) ;

        telemetry.addData("status", "arm lowered");
        telemetry.update();

        TTL2HardwareManager.leftArmBaseMotor.setPower(0.0);
        TTL2HardwareManager.rightArmBaseMotor.setPower(0.0);
    }

    protected void fullyExtendArm() {
        TTL2HardwareManager.leftArmElbowServo.setPosition(1.0);
        TTL2HardwareManager.rightArmElbowServo.setPosition(1.0);
        Timer timer = new Timer();
        TimerTask stopElbowServoTask = new TimerTask() {

            @Override
            public void run() {
                TTL2HardwareManager.leftArmElbowServo.setPosition(0.5);
                TTL2HardwareManager.rightArmElbowServo.setPosition(0.5);
            }

        };
        timer.schedule(stopElbowServoTask, 3625);

        TTL2HardwareManager.armWristServo.setPosition(0.1);
        rotateArmBase(85, 1.0);
        TTL2HardwareManager.intakeServo.setPosition(-1.0);
    }

    protected void resetDriveEncoders() {
        TTL2HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        TTL2HardwareManager.backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        TTL2HardwareManager.frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TTL2HardwareManager.backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    protected void zeroDriveMotorPower() {
        TTL2HardwareManager.frontLeftDrive.setPower(0.0);
        TTL2HardwareManager.frontRightDrive.setPower(0.0);
        TTL2HardwareManager.backLeftDrive.setPower(0.0);
        TTL2HardwareManager.backRightDrive.setPower(0.0);
    }

    private boolean driveMotorsNearTarget() {
        int targetFrontLeftDriveMotorPos = TTL2HardwareManager.frontLeftDrive.getTargetPosition();
        int targetFrontRightDriveMotorPos = TTL2HardwareManager.frontRightDrive.getTargetPosition();
        int targetBackLeftDriveMotorPos = TTL2HardwareManager.backLeftDrive.getTargetPosition();
        int targetBackRightDriveMotorPos = TTL2HardwareManager.backRightDrive.getTargetPosition();


        int currentFrontLeftDriveMotorPos = TTL2HardwareManager.frontLeftDrive.getCurrentPosition();
        int currentFrontRightDriveMotorPos = TTL2HardwareManager.frontRightDrive.getCurrentPosition();
        int currentBackLeftDriveMotorPos = TTL2HardwareManager.backLeftDrive.getCurrentPosition();
        int currentBackRightDriveMotorPos = TTL2HardwareManager.backRightDrive.getCurrentPosition();

        return Math.abs(currentFrontLeftDriveMotorPos - targetFrontLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentFrontRightDriveMotorPos - targetFrontRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackLeftDriveMotorPos - targetBackLeftDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentBackRightDriveMotorPos - targetBackRightDriveMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    private boolean liftMotorsNearTarget() {
        int targetLeftLiftMotorPos = TTL2HardwareManager.leftArmBaseMotor.getTargetPosition();
        int targetRightLiftMotorPos = TTL2HardwareManager.rightArmBaseMotor.getTargetPosition();

        int currentLeftLiftMotorPos = TTL2HardwareManager.leftArmBaseMotor.getCurrentPosition();
        int currentRightLiftMotorPos = TTL2HardwareManager.rightArmBaseMotor.getCurrentPosition();

        return Math.abs(currentLeftLiftMotorPos - targetLeftLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentRightLiftMotorPos - targetRightLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;
    }

    protected void printDriveTelemetry() {
//        telemetry.addData("frontLeft", "%s %s", TTL2HardwareManager.frontLeftDrive.getCurrentPosition(),
//                TTL2HardwareManager.frontLeftDrive.getDirection());
//        telemetry.addData("backLeft", "%s %s", TTL2HardwareManager.backLeftDrive.getCurrentPosition(),
//                TTL2HardwareManager.backLeftDrive.getDirection());
//        telemetry.addData("frontRight", "%s %s", TTL2HardwareManager.frontRightDrive.getCurrentPosition(),
//                TTL2HardwareManager.frontRightDrive.getDirection());
//        telemetry.addData("backRight", "%s %s", TTL2HardwareManager.backRightDrive.getCurrentPosition(),
//                TTL2HardwareManager.backRightDrive.getDirection());
//        telemetry.update();
    }

    /**
     * Invoked immediately after start.
     */
    protected abstract void run();

}
