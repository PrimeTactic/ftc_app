package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDriveSystem {

    private static final double INCHES_TO_TICKS_VERTICAL = -10.82;
    private static final double INCHES_TO_TICKS_LATERAL = 46.17;
    private static final double INCHES_TO_TICKS_DIAGONAL = 30.0;
    private static final double DEGREES_TO_TICKS = -9.39;
    private static final double TICKS_WITHIN_TARGET = 10.0;

    private final DcMotor frontLeft, frontRight, backLeft, backRight;

    public MecanumDriveSystem(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    public void vertical(double inches, double speed) {
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_VERTICAL);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (TTOpMode.getOpMode().opModeIsActive() && !nearTarget()) ;
        zeroPower();
    }

    public void lateral(double inches, double speed) {
        setDriveRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * INCHES_TO_TICKS_LATERAL);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(-ticks);

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (TTOpMode.getOpMode().opModeIsActive() && !nearTarget()) ;
        zeroPower();
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

        switch (quadrant) {
            case 0:
                // forward right
                frontLeft.setTargetPosition(ticks);
                backRight.setTargetPosition(ticks);

                frontLeft.setPower(speed);
                backRight.setPower(speed);
                break;
            case 1:
                // forward left
                frontRight.setTargetPosition(ticks);
                backLeft.setTargetPosition(ticks);

                frontRight.setPower(speed);
                backLeft.setPower(speed);
                break;
            case 2:
                // backward left
                frontLeft.setTargetPosition(-ticks);
                backRight.setTargetPosition(-ticks);

                frontLeft.setPower(speed);
                backRight.setPower(speed);
                break;
            case 3:
                // backward right
                frontRight.setTargetPosition(-ticks);
                backLeft.setTargetPosition(-ticks);

                frontRight.setPower(speed);
                backLeft.setPower(speed);
                break;
            default:
                throw new IllegalArgumentException("quadrant must be 0, 1, 2, or 3");
        }

        while (TTOpMode.getOpMode().opModeIsActive() && !nearTarget()) ;
        zeroPower();
    }

    public void moveContinuous(Vector2 velocity) {

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

        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (TTOpMode.getOpMode().opModeIsActive() && !nearTarget()) ;
        zeroPower();
    }

    private void setDriveRunMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }

    private boolean nearTarget() {
        return Utils.motorNearTarget(frontLeft, TICKS_WITHIN_TARGET)
                && Utils.motorNearTarget(frontRight, TICKS_WITHIN_TARGET)
                && Utils.motorNearTarget(backLeft, TICKS_WITHIN_TARGET)
                && Utils.motorNearTarget(backRight, TICKS_WITHIN_TARGET);
    }

    public void zeroPower() {
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

}
