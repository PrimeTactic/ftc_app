package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class Arm {

    private static final double DRIVE_MOTOR_TICKS_PER_INCH_COVERED = 20;
    // arbitrary value, need to change it.
    // 3in/20tick = MeasuredValue (measured) (in)/ calculated Tick (X)
    private static final double DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED = 3.506493506;

    private final DcMotor lift, elbow;

    public Arm(DcMotor lift, DcMotor elbow) {
        this.lift = lift;
        this.elbow = elbow;
    }

    public void rotate(int degrees, double power) {
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (degrees * DRIVE_MOTOR_TICKS_PER_DEGREE_COVERED);
        elbow.setTargetPosition(ticks);
        elbow.setPower(power);
        while (elbow.isBusy()) ;
    }

    public void rotateContinuous(double power) {
        elbow.setPower(power);
    }

    public void lift(double inches, double power) {
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCH_COVERED);
        lift.setTargetPosition(ticks);
        lift.setPower(power);

    }

    public void liftContinuous(double power) {
        lift.setPower(power);
    }

}
