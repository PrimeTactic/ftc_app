package teamcode.kkl2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.List;

import teamcode.examples.Helper;
import teamcode.examples.Mineral;
import teamcode.examples.TensorFlowManager;

@Autonomous(name = "KKL2Auto", group = "Linear OpMode")
public class KKL2Auto extends LinearOpMode {
    private static final double DRIVE_TICKS_PER_CENTIMETER_COVERED = -55.0;
    private static final double TURN_TICKS_PER_RADIAN_COVERED = 1066.15135303;
    private static final int DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;
    private static final int LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD = 10;
    private static final double TURN_POWER = 0.5;

    private TensorFlowManager tfManager;

    @Override
    public void runOpMode() {
        KKL2HardwareManager.initialize(this);
        this.initialize();

        waitForStart();
        resetDriveEncoders();

        unlatch();

        approach();

        while (opModeIsActive());
    }

    private void drive(double centimeters, double power) {
        zeroDriveMotorPower();

        KKL2HardwareManager.driveLMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));
        KKL2HardwareManager.driveRMotor.setTargetPosition((int) (centimeters * DRIVE_TICKS_PER_CENTIMETER_COVERED));

        KKL2HardwareManager.driveLMotor.setPower(power);
        KKL2HardwareManager.driveRMotor.setPower(power);

        while (opModeIsActive() && !driveNearTarget()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void turn(int degrees) {
        double radians = (degrees / 180.0) * Math.PI;
        turn(radians);
    }

    private void turn(double radians) {
        zeroDriveMotorPower();

        int posL = (int) (radians * -TURN_TICKS_PER_RADIAN_COVERED);
        int posR = (int) (radians * TURN_TICKS_PER_RADIAN_COVERED);

        KKL2HardwareManager.driveLMotor.setTargetPosition(posL);
        KKL2HardwareManager.driveRMotor.setTargetPosition(posR);
        KKL2HardwareManager.driveLMotor.setPower(TURN_POWER);
        KKL2HardwareManager.driveRMotor.setPower(TURN_POWER);

        while (opModeIsActive() && !driveNearTarget()) ;
        zeroDriveMotorPower();
        resetDriveEncoders();
    }

    private void resetDriveEncoders() {
        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.driveLMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        KKL2HardwareManager.driveRMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void zeroDriveMotorPower() {
        KKL2HardwareManager.driveLMotor.setPower(0.0);
        KKL2HardwareManager.driveRMotor.setPower(0.0);
    }

    private boolean driveNearTarget() {
        int targetDriveLMotorPos = KKL2HardwareManager.driveLMotor.getTargetPosition();
        int targetDriveRMotorPos = KKL2HardwareManager.driveRMotor.getTargetPosition();

        int currentDriveLMotorPos = KKL2HardwareManager.driveLMotor.getCurrentPosition();
        int currentDriveRMotorPos = KKL2HardwareManager.driveRMotor.getCurrentPosition();

        boolean nearTarget = Math.abs(currentDriveLMotorPos - targetDriveLMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD
                && Math.abs(currentDriveRMotorPos - targetDriveRMotorPos) < DRIVE_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;

/*
        telemetry.addData("rightTarget", targetDriveRMotorPos);
        telemetry.addData("rightCurrent", currentDriveRMotorPos);
        telemetry.addData("leftTarget", targetDriveLMotorPos);
        telemetry.addData("leftCurrent", currentDriveLMotorPos);

        telemetry.update();
*/
        return nearTarget;
    }

    private void resetLiftEncoders() {
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void zeroLiftMotorPower() {
        KKL2HardwareManager.liftBaseMotor.setPower(0.0);
    }

    private boolean liftNearTarget() {

        int targetLiftMotorPos = KKL2HardwareManager.liftBaseMotor.getTargetPosition();

        int currentLiftMotorPos = KKL2HardwareManager.liftBaseMotor.getCurrentPosition();

        boolean nearTarget = Math.abs(currentLiftMotorPos - targetLiftMotorPos) < LIFT_MOTOR_TICKS_AWAY_FROM_TARGET_THRESHOLD;

        telemetry.addData("liftTarget", targetLiftMotorPos);
        telemetry.addData("liftCurrent", currentLiftMotorPos);

        telemetry.update();

        return nearTarget;
    }

    private void unlatch() {

        // lower the robot using the servo
        KKL2HardwareManager.liftLockServo.setPosition(1.0);
        sleep(9500);
        KKL2HardwareManager.liftLockServo.setPosition(0.5);

        // push arm backwards

        double liftBaseMotorPower = 1.0;
        double ticksPerDegree = Helper.REV_CORE_HEX_MOTOR_TICKS_PER_ROTATION / 360.0 * 7.0;
        int ticks = (int)(ticksPerDegree * 5);
        /*
        KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);
        while (opModeIsActive() && !liftNearTarget()) ;

        zeroLiftMotorPower();
        resetLiftEncoders();
        */

        // unlatch the lift arm
        KKL2HardwareManager.liftLatchServo.setPosition(0);
        sleep(500);
        KKL2HardwareManager.liftLatchServo.setPosition(1);
        sleep(500);
        KKL2HardwareManager.liftLatchServo.setPosition(0);
        sleep(2000);

        turn(7);

        // move arm back down
        resetLiftEncoders();
        ticks = (int)(ticksPerDegree * -70);
        KKL2HardwareManager.liftBaseMotor.setTargetPosition(ticks);
        KKL2HardwareManager.liftBaseMotor.setPower(liftBaseMotorPower);
        while (opModeIsActive() && !liftNearTarget()) ;
        zeroLiftMotorPower();
        resetLiftEncoders();
    }

    private void approach() {
        boolean completed = false;
        List<Mineral> minerals = null;
        double power = 1.0;

        while (opModeIsActive() && !completed) {
            minerals = this.tfManager.getRecognizedMinerals();

            if (minerals != null) {
                for (Mineral mineral : minerals) {
                    if (mineral.isGold()) {
                        calculateAngle(mineral);
                        telemetry.addData("Angle (deg)", mineral.getAngle());
                        telemetry.addData("Horizontal (cm)", mineral.getA());
                        telemetry.addData("Vertical (cm)", mineral.getB());
                        double degrees = mineral.getAngle();
                        if (degrees > 10 || degrees < -10) {
                            // turn towards the gold
                            turn(degrees * Math.PI / 180);
                        } else {
                            telemetry.addData("Facing Gold", "No Turn, Move Forward");
                            // Move to knock the gold
                            drive(mineral.getC() + 45, power);
                            completed = true;
                        }

                        telemetry.update();
                    }
                }
            }
            else {
                telemetry.addData("Can't find minerals", "Move back");
                telemetry.update();
                // drive back to try and detect objects
                drive(-3, power);
            }
        }
    }

    private void initialize() {
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        KKL2HardwareManager.liftBaseMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.tfManager = new TensorFlowManager(this.hardwareMap);
        this.tfManager.initialize();
    }

    private void addTelemetry(Mineral mineral, int num) {
        if (mineral != null) {
            telemetry.addData(
                    "Mineral" + num + " isGold left bottom",
                    "%s %f %f",
                    mineral.isGold(),
                    mineral.getLeft(),
                    mineral.getBottom());
        }
    }

    public double getCentimetersFromPixels(double pixels) {
        return ((pixels - 300) / -100) * 30.48; // centimeters
    }

    private void calculateAngle(Mineral m) {
        float target_x = 640;
        float center_y = (m.getLeft() + m.getRight()) / 2;
        float center_x = (m.getBottom() + m.getTop()) / 2;
        float height = m.getRight() - m.getLeft();

        double c = getCentimetersFromPixels(height); // centimeters
        float error = target_x - center_x; // adjacent side in pixels
        double a = c * error / Helper.KK_CAMERA_DISTANCE; // centimeters
        double b = Math.sqrt((c*c) - (a*a)); // centimeters
        double radians = Math.asin(a / c);
        double degrees = Math.toDegrees(radians);
        m.setAngle(degrees);
        m.setA(a);
        m.setB(b);
        m.setC(c);
    }
}
