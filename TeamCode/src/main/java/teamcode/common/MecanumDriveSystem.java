package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.TimerTask;

public class MecanumDriveSystem {

    // correct ticks = current ticks * expected distance / actual distance

    private static final double INCHES_TO_TICKS_VERTICAL = -43.4641507685;
    private static final double INCHES_TO_TICKS_LATERAL = 40;
    private static final double INCHES_TO_TICKS_DIAGONAL = 90.0;
    private static final double DEGREES_TO_TICKS = -9.39;
    private static final double TICKS_WITHIN_TARGET = 15.0;
    private static final double SPEED_ADJUST_WITH_ENCODERS_PERIOD = 0.1;
    private static final double SPEED_PLATEAU_TICKS = 500.0;
    private static final double MINIMUM_SPEED_MANAGEMENT_POWER = 0.1;

    private final DcMotor frontLeft, frontRight, backLeft, backRight;
    private final DcMotor[] motors;

    public MecanumDriveSystem(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
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
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        double[] maxPowers = {speed, speed, speed, speed};
        manageSpeedWithEncoders(maxPowers);
    }

    public void lateral(double inches, double speed) {
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
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
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
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

    public void continuous(Vector2 velocity) {

    }

    /**
     * @param degrees degrees to turn clockwise
     * @param speed   [0.0, 1.0]
     */
    public void turn(double degrees, double speed) {
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (degrees * DEGREES_TO_TICKS);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);

        double[] maxPowers = {speed, speed, speed, speed};
        manageSpeedWithEncoders(maxPowers);
    }

    private void setDriveRunMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }

    private boolean nearTarget() {
        return motorNearTarget(frontLeft, TICKS_WITHIN_TARGET)
                && motorNearTarget(frontRight, TICKS_WITHIN_TARGET)
                && motorNearTarget(backLeft, TICKS_WITHIN_TARGET)
                && motorNearTarget(backRight, TICKS_WITHIN_TARGET);
    }


    public static boolean motorNearTarget(DcMotor motor, double ticksWithinTarget) {
        return Math.abs(motor.getTargetPosition() - motor.getCurrentPosition()) < ticksWithinTarget;
    }


    private boolean motorsBusy() {
        return frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy();
    }

    public void zeroPower() {
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

    /**
     * Forces the thread to wait until the motors reach their target.
     *
     * @param maxPowers must have a length of 4, each element represents the power of a motor that corresponds to that element's index
     */
    public void manageSpeedWithEncoders(final double[] maxPowers) {
        final TimerTask speedAdjustTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    DcMotor motor = motors[i];
                    double maxPower = maxPowers[i];
                    int currentTicks = motor.getCurrentPosition();
                    int targetTicks = motor.getTargetPosition();
                    double currentPower = calculateCurrentPower(maxPower, currentTicks, targetTicks);
                    motor.setPower(currentPower);
                }
            }
        };
        TTTimer.scheduleAtFixedRate(speedAdjustTask, SPEED_ADJUST_WITH_ENCODERS_PERIOD);
        while (!nearTarget()) ;
        zeroPower();
    }

    private double calculateCurrentPower(double maxPower, int currentTicks, int targetTicks) {
        currentTicks = Math.abs(currentTicks);
        targetTicks = Math.abs(targetTicks);
        int midpoint = targetTicks / 2;
        double currentPower;
        if (currentTicks < midpoint) {
            currentPower = currentTicks / SPEED_PLATEAU_TICKS * maxPower;
            if (currentPower < MINIMUM_SPEED_MANAGEMENT_POWER) {
                currentPower = MINIMUM_SPEED_MANAGEMENT_POWER;
            }
        } else {
            currentPower = (targetTicks - currentTicks) / SPEED_PLATEAU_TICKS * maxPower;
            if (currentPower <= MINIMUM_SPEED_MANAGEMENT_POWER) {
                currentPower = 0.0;
            }
        }
        if (currentPower > maxPower) {
            currentPower = maxPower;
        }
        return currentPower;
    }

}
