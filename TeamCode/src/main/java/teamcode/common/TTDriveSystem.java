package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;


import java.util.TimerTask;

import static fi.iki.elonen.NanoHTTPD.Method.HEAD;

public class TTDriveSystem {

    // correct ticks = current ticks * expected distance / actual distance-
    // Formula for determing correct ticks: current ticks * expected distance / actual distance
    // conversion constants
    private static final double INCHES_TO_TICKS_VERTICAL = -43.4641507685;
    private static final double INCHES_TO_TICKS_LATERAL = 40;
    private static final double INCHES_TO_TICKS_DIAGONAL = 90.0;
    private static final double DEGREES_TO_TICKS = -9.39;

    private static final double TICKS_WITHIN_TARGET = 30.0;
    private static final int MAX_TICKS_PER_SECOND = 40;
    private static final double SPEED_ADJUST_WITH_ENCODERS_PERIOD = 0.1;
    private static final double DECELERATION_TICKS = 1000;
    private static final int ACCELERATION_TICKS = 1000;
    private static final double MINIMUM_ENCODERS_POWER = 0.1;


    /**
     * Maximum number of ticks a motor's current position must be away from it's target for it to
     * be considered near its target.
     */
    private static final double TICK_ERROR = 30.0;

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor[] motors;

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
       // correctDirections();
    }

    private void correctDirections() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void continuous(Vector2 velocity, double turn) {
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double direction = velocity.getDirection();
        double power = velocity.magnitude();

        double angle = -direction + Math.PI / 4;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double frontLeftPow = power * sin - turn;
        double frontRightPow = power * cos + turn;
        double backLeftPow = power * cos - turn;
        double backRightPow = power * sin + turn;

        frontLeft.setPower(frontLeftPow);
        frontRight.setPower(frontRightPow);
        backLeft.setPower(backLeftPow);
        backRight.setPower(backRightPow);
    }

    public void vertical(double inches, double speed) {

        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + ticks);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);
        backRight.setTargetPosition(backRight.getCurrentPosition() + ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor motor : motors) {
            motor.setPower(speed);
        }

        while (!nearTarget()) ;
        brake();
    }

    public void vertical2(double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);
        for (DcMotor motor : motors) {
            motor.setTargetPosition(ticks);
            motor.setPower(speed);
        }
        while (!nearTarget()) ;
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void lateral(double inches, double speed) {
        int ticks = (int) (inches * INCHES_TO_TICKS_LATERAL);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - ticks);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + ticks);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);
        backRight.setTargetPosition(backRight.getCurrentPosition() - ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor motor : motors) {
            motor.setPower(speed);
        }

        while (!nearTarget()) ;
        brake();
    }

    /**
     * Drives at an angle whose reference angle is 45 degrees and lies in the specified quadrant.
     *
     * @param quadrant 0, 1, 2, or 3 corresponds to I, II, III, or IV respectively
     * @param inches   the inches to be travelled
     * @param speed    [0.0, 1.0]
     */
    public void diagonal(int quadrant, double inches, double speed) {
        int ticks = (int) (inches * INCHES_TO_TICKS_DIAGONAL);
        double[] powers = new double[4];

        switch (quadrant) {
            case 0:
                // forward right
                frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
                backRight.setTargetPosition(backRight.getCurrentPosition() + ticks);

                powers[0] = speed;
                powers[3] = speed;
                break;
            case 1:
                // forward left
                frontRight.setTargetPosition(frontRight.getCurrentPosition() + ticks);
                backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);

                powers[1] = speed;
                powers[2] = speed;
                break;
            case 2:
                // backward left
                frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - ticks);
                backRight.setTargetPosition(backRight.getCurrentPosition() - ticks);

                powers[0] = speed;
                powers[3] = speed;
                break;
            case 3:
                // backward right
                frontRight.setTargetPosition(frontRight.getCurrentPosition() - ticks);
                backLeft.setTargetPosition(backLeft.getCurrentPosition() - ticks);

                powers[1] = speed;
                powers[2] = speed;
                break;
            default:
                throw new IllegalArgumentException("quadrant must be 0, 1, 2, or 3");
        }
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (int i = 0; i < 4; i++) {
            DcMotor motor = motors[i];
            double power = powers[i];
            motor.setPower(power);
        }

        while (!nearTarget()) ;
        brake();
    }

    /**
     * @param degrees degrees to turn clockwise
     * @param speed   [0.0, 1.0]
     */
    public void turn(double degrees, double speed) {
        int ticks = (int) (degrees * DEGREES_TO_TICKS);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + ticks);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - ticks);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + ticks);
        backRight.setTargetPosition(backRight.getCurrentPosition() - ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor motor : motors) {
            motor.setPower(speed);
        }
        brake();
    }

    public void brake() {
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
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

        //TTTimer.scheduleAtFixedRate(speedAdjustTask, SPEED_ADJUST_WITH_ENCODERS_PERIOD);
        while (!nearTarget()) ;
        //speedAdjustTask.cancel();
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        return true;
    }

    private void adjustMotorPower(DcMotor motor, double maxPower) {
        int currentTicks = motor.getCurrentPosition();
        int targetTicks = motor.getTargetPosition();
        double nextPower = calculateNextPower(currentTicks, targetTicks, motor.getPower(), maxPower);
        motor.setPower(nextPower);
    }

    private double calculateNextPower(int currentTicks, int targetTicks, double currentPower, double maxPower) {
        currentTicks = Math.abs(currentTicks);
        targetTicks = Math.abs(targetTicks);
        TTOpMode.getOpMode().telemetry.addData("current power", currentPower);
        double nextPower;
        if (currentTicks < ACCELERATION_TICKS) {
            double acceleration = Math.pow(MAX_TICKS_PER_SECOND, 2) / (2 * ACCELERATION_TICKS);
            nextPower = currentPower + acceleration * SPEED_ADJUST_WITH_ENCODERS_PERIOD;
            TTOpMode.getOpMode().telemetry.addData("power", currentPower);
            if (nextPower < MINIMUM_ENCODERS_POWER) {
                nextPower = MINIMUM_ENCODERS_POWER;
            }
        } else if (currentTicks > targetTicks - DECELERATION_TICKS) {
            double deceleration = -Math.pow(MAX_TICKS_PER_SECOND, 2) / (DECELERATION_TICKS - 2 * TICKS_WITHIN_TARGET);
            TTOpMode.getOpMode().telemetry.addData("decceleration", deceleration);
            nextPower = currentPower + deceleration * SPEED_ADJUST_WITH_ENCODERS_PERIOD;
            //nextPower = MINIMUM_ENCODERS_POWER;
            TTOpMode.getOpMode().telemetry.addData("next power", nextPower);
            if (nextPower <= 0.2){
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


    private void setRunMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motors) {
            motor.setMode(mode);
        }
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        frontLeft.setZeroPowerBehavior(behavior);
        frontRight.setZeroPowerBehavior(behavior);
        backLeft.setZeroPowerBehavior(behavior);
        backRight.setZeroPowerBehavior(behavior);
    }

}
