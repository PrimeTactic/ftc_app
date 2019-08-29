package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TTArm {

    private static final double LIFT_INCHES_TO_TICKS = 20;
    private static final double ELBOW_DEGREES_TO_TICKS = 3.506493506;

    private final DcMotor lift, elbow;

    public TTArm(DcMotor lift, DcMotor elbow) {
        this.lift = lift;
        this.elbow = elbow;
    }

    public void rotate(int degrees, double power) {
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (degrees * ELBOW_DEGREES_TO_TICKS);
        elbow.setTargetPosition(ticks);
        elbow.setPower(power);
        while (elbow.isBusy()) ;
    }

    public void rotateContinuous(double power) {
        elbow.setPower(power);
    }

    public void lift(double inches, double power) {
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int ticks = (int) (inches * LIFT_INCHES_TO_TICKS);
        lift.setTargetPosition(ticks);
        lift.setPower(power);
        while (lift.isBusy()) ;
    }

    public void liftContinuous(double power) {
        lift.setPower(power);
    }

}
