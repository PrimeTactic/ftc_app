package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.TimerTask;

public class TTDriveSystem {

    // correct ticks = current ticks * expected distance / actual distance-
    private static final double INCHES_TO_TICKS_VERTICAL = -43.4641507685;
    private static final double INCHES_TO_TICKS_LATERAL = 40;
    private static final double INCHES_TO_TICKS_DIAGONAL = 90.0;
    private static final double DEGREES_TO_TICKS = -9.39;
    /**
     * Maximum number of ticks a motor's current position must be away from it's target for it to
     * be considered near its target.
     */
    private static final double TICK_ERROR = 30.0;
    private static final int MAX_TICKS_PER_SECOND = 40;
    private static final double POWER_ADJUST_WITH_ENCODERS_PERIOD = 0.1;
    private static final double DECELERATION_TICKS = 1000;
    private static final int ACCELERATION_TICKS = 1000;
    private static final double MINIMUM_ENCODERS_POWER = 0.1;

    private final DcMotor frontLeft, frontRight, backLeft, backRight;
    private final DcMotor[] motors;

    public TTDriveSystem(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        motors = new DcMotor[4];
        motors[0] = frontLeft;
        motors[1] = frontRight;
        motors[2] = backLeft;
        motors[3] = backRight;
        correctDirections();
    }

    private void correctDirections() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void vertical(double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        double[] maxPowers = {speed, speed, speed, speed};
        manageSpeedWithEncoders(maxPowers);
    }

    public void lateral(double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_LATERAL);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);

        double[] maxPowers = {speed, speed, speed, speed};
        manageSpeedWithEncoders(maxPowers);
    }

    /**
     * Drives at an angle whose reference angle is 45 degrees and lies in the specified quadrant.
     *
     * @param quadrant 0, 1, 2, or 3 corresponds to I, II, III, or IV respectively
     * @param inches   the inches to be travelled
     * @param speed    [0.0, 1.0]
     */
    public void diagonal(int quadrant, double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_DIAGONAL);
        double[] maxPowers = new double[4];

        switch (quadrant) {
            case 0:
                // forward right
                frontLeft.setTargetPosition(ticks);
                backRight.setTargetPosition(ticks);

                maxPowers[0] = speed;
                maxPowers[3] = speed;
                break;
            case 1:
                // forward left
                frontRight.setTargetPosition(ticks);
                backLeft.setTargetPosition(ticks);

                maxPowers[1] = speed;
                maxPowers[2] = speed;
                break;
            case 2:
                // backward left
                frontLeft.setTargetPosition(-ticks);
                backRight.setTargetPosition(-ticks);

                maxPowers[0] = speed;
                maxPowers[3] = speed;
                break;
            case 3:
                // backward right
                frontRight.setTargetPosition(-ticks);
                backLeft.setTargetPosition(-ticks);

                maxPowers[1] = speed;
                maxPowers[2] = speed;
                break;
            default:
                throw new IllegalArgumentException("quadrant must be 0, 1, 2, or 3");
        }

        manageSpeedWithEncoders(maxPowers);
    }

    /**
     * @param degrees degrees to turn clockwise
     * @param speed   [0.0, 1.0]
     */
    public void turn(double degrees, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (degrees * DEGREES_TO_TICKS);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);

        double[] maxPowers = {speed, speed, speed, speed};
        manageSpeedWithEncoders(maxPowers);
        brake();
    }

    public void brake() {
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

    private void manageSpeedWithEncoders(final double[] maxPowers) {
        final TimerTask speedAdjustTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    DcMotor motor = motors[i];
                    double maxPower = maxPowers[i];
                    int currentTicks = motor.getCurrentPosition();
                    int targetTicks = motor.getTargetPosition();
                    double nextPower = calculateNextPower(currentTicks, targetTicks, motor.getPower(), maxPower);
                    motor.setPower(nextPower);
                }
                TTOpMode.getOpMode().telemetry.update();
            }
        };
        TTTimer.scheduleAtFixedRate(speedAdjustTask, POWER_ADJUST_WITH_ENCODERS_PERIOD);
    }

    private double calculateNextPower(int currentTicks, int targetTicks, double currentPower, double maxPower) {
        currentTicks = Math.abs(currentTicks);
        targetTicks = Math.abs(targetTicks);
        TTOpMode.getOpMode().telemetry.addData("current power", currentPower);
        double nextPower;
        if (currentTicks < ACCELERATION_TICKS) {
            double acceleration = Math.pow(MAX_TICKS_PER_SECOND, 2) / (2 * ACCELERATION_TICKS);
            nextPower = currentPower + acceleration * POWER_ADJUST_WITH_ENCODERS_PERIOD;
            TTOpMode.getOpMode().telemetry.addData("power", currentPower);
            if (nextPower < MINIMUM_ENCODERS_POWER) {
                nextPower = MINIMUM_ENCODERS_POWER;
            }
        } else if (currentTicks > targetTicks - DECELERATION_TICKS) {
            double deceleration = -Math.pow(MAX_TICKS_PER_SECOND, 2) / (DECELERATION_TICKS - 2 * TICK_ERROR);
            TTOpMode.getOpMode().telemetry.addData("decceleration", deceleration);
            nextPower = currentPower + deceleration * POWER_ADJUST_WITH_ENCODERS_PERIOD;
            //nextPower = MINIMUM_ENCODERS_POWER;
            TTOpMode.getOpMode().telemetry.addData("next power", nextPower);
            if (nextPower <= 0.2) {
                nextPower = 0.2;
            }

        } else {
            nextPower = 0.75;
        }
        if (nextPower > maxPower) {
            nextPower = 0.75;
        }
        return nextPower;
    }

    private boolean nearTarget() {
        for (DcMotor motor : motors) {
            int targetPosition = motor.getTargetPosition();
            int currentPosition = motor.getCurrentPosition();
            double ticksFromTarget = Math.abs(targetPosition - currentPosition);
            if (ticksFromTarget > TICK_ERROR) {
                return false;
            }
        }
        return true;
    }

    private void setRunMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior bahavior) {
        frontLeft.setZeroPowerBehavior(bahavior);
        frontRight.setZeroPowerBehavior(bahavior);
        backLeft.setZeroPowerBehavior(bahavior);
        backRight.setZeroPowerBehavior(bahavior);
    }

}
