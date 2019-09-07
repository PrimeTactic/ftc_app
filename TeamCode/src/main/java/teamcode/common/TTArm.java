package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TTArm {

    private static final double ELBOW_DEGREES_TO_TICKS = 3.506493506;
    private double MINIMUM_LIFT_DISTANCE_FROM_GROUND = 15.7;
    private double MAXIMUM_LIFT_INCHES_FROM_GROUND = 25;

    private final DcMotor lift, elbow;
    private final DistanceSensor liftSensor;

    public TTArm(DcMotor lift, DistanceSensor liftSensor, DcMotor elbow) {
        this.lift = lift;
        this.liftSensor = liftSensor;
        this.elbow = elbow;
    }

    /**
     * @param inches how far above minimum position
     */
    public void setLiftHeight(double inches, double power) {
        if (inches < 0.0) {
            inches = 0.0;
        } else if (inches > MAXIMUM_LIFT_INCHES_FROM_GROUND - MINIMUM_LIFT_DISTANCE_FROM_GROUND) {
            inches = MAXIMUM_LIFT_INCHES_FROM_GROUND - MINIMUM_LIFT_DISTANCE_FROM_GROUND;
        }
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double startingHeight = getLiftHeight();
        if (inches < startingHeight) {
            lift.setPower(-Math.abs(power));
            while (getLiftHeight() > inches && TTOpMode.getOpMode().opModeIsActive()) {
                TTOpMode.getOpMode().telemetry.addData("current height", getLiftHeight());
                TTOpMode.getOpMode().telemetry.update();
            }
        } else if (inches > startingHeight) {
            lift.setPower(Math.abs(power));
            while (getLiftHeight() < inches && TTOpMode.getOpMode().opModeIsActive()) {
                TTOpMode.getOpMode().telemetry.addData("current height", getLiftHeight());
                TTOpMode.getOpMode().telemetry.update();
            }
        }
        lift.setPower(0.0);
    }

    public void liftContinuous(double power) {
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setPower(power);
    }

    /**
     * Returns the number of inches that the lift is above its minimum height.
     */
    public double getLiftHeight() {
        return liftSensor.getDistance(DistanceUnit.INCH) - MINIMUM_LIFT_DISTANCE_FROM_GROUND;
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
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elbow.setPower(power);
    }


}
