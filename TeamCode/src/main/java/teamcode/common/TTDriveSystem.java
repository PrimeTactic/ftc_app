package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

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
    private static final double P = 2.5;
    private static final double I = 0.1;
    private static final double D = 0.2;

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
        setPID();
        correctDirections();
    }

    private void setPID() {
        PIDCoefficients coefficients = new PIDCoefficients();
        coefficients.i = I;
        coefficients.p = P;
        coefficients.d = D;
        for (DcMotor motor : motors) {
            DcMotorEx ex = (DcMotorEx) motor;
            ex.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, coefficients);
        }
    }

    private void correctDirections() {
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void continuous(Vector2 velocity, double turnSpeed) {
        if (velocity.isZero()) {
            brake();
            return;
        }
        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double direction = velocity.getDirection();
        double power = velocity.magnitude();

        double angle = -direction + Math.PI / 4;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double frontLeftPow = power * sin - turnSpeed;
        double frontRightPow = power * cos + turnSpeed;
        double backLeftPow = power * cos - turnSpeed;
        double backRightPow = power * sin + turnSpeed;

        frontLeft.setPower(frontLeftPow);
        frontRight.setPower(frontRightPow);
        backLeft.setPower(backLeftPow);
        backRight.setPower(backRightPow);
    }

    public void vertical(double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        for (DcMotor motor : motors) {
            motor.setPower(speed);
        }
        while (!nearTarget()) ;
        brake();
    }

    public void lateral(double inches, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int ticks = (int) (inches * INCHES_TO_TICKS_LATERAL);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
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
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!nearTarget()) ;
    }

    /**
     * @param degrees degrees to turn clockwise
     * @param speed   [0.0, 1.0]
     */
    public void turn(double degrees, double speed) {
        setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int ticks = (int) (degrees * DEGREES_TO_TICKS);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);
        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        double[] maxPowers = {speed, speed, speed, speed};
        while (!nearTarget()) ;
        brake();
    }

    public void brake() {
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
