package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TTArm {

    private static final double ELBOW_DEGREES_TO_TICKS = 3.506493506;
    private double MINIMUM_LIFT_DISTANCE_FROM_GROUND = 15.7;
    private double MAXIMUM_LIFT_INCHES_FROM_GROUND = 25;
    private static final double SERVO_OPEN_POSITION = 1.0;
    private static final double SERVO_CLOSED_POSITION = 0.0;

    private final DcMotor lift, elbow;
    private final DistanceSensor liftSensor;
    private final Servo claw;
    private ClawPosition clawPosition;

    public TTArm(DcMotor lift, DistanceSensor liftSensor, DcMotor elbow, Servo claw) {
        this.lift = lift;
        this.liftSensor = liftSensor;
        this.elbow = elbow;
        this.claw = claw;
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
     * Returns the number of inches that the lift is above its minimum position.
     */
    public double getLiftHeight() {
        return liftSensor.getDistance(DistanceUnit.INCH) - MINIMUM_LIFT_DISTANCE_FROM_GROUND;
    }

    public void rotate(int degrees, double power) {
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int ticks = (int) (degrees * ELBOW_DEGREES_TO_TICKS);
        elbow.setTargetPosition(ticks);
        elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elbow.setPower(power);
        while (elbow.isBusy()) ;
    }

    public void rotateContinuous(double power) {
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        elbow.setPower(power);
    }


    public void setClawPosition(ClawPosition clawPosition) {
        this.clawPosition = clawPosition;
        claw.setPosition(clawPosition.getServoPosition());
    }

    public ClawPosition getClawPosition() {
        return clawPosition;
    }

    public static enum ClawPosition {

        OPEN(SERVO_OPEN_POSITION), CLOSED(SERVO_CLOSED_POSITION);

        private double servoPosition;

        ClawPosition(double servoPosition) {
            this.servoPosition = servoPosition;
        }

        public double getServoPosition() {
            return servoPosition;
        }

    }

}
