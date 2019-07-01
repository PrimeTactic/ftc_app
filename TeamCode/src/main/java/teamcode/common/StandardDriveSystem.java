package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class StandardDriveSystem extends FourWheelDriveSystem {

    private final double WHEEL_DIAMETER = 0.0;
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
    private final double ANGLE_FOR_TEN_TICKS = 0.0;
    private final double TICKS_FOR_ONE_ROTATION_OF_ROBOT = 360 / ANGLE_FOR_TEN_TICKS * 10;

    public StandardDriveSystem(DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor backLeftMotor, DcMotor backRightMotor) {
        super(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor);
    }

    @Override
    public void move(double x, double y, double speed) {

    }

    @Override
    public void moveContinuously(double x, double y) {

    }

    @Override
    public void turn(double degrees, double speed) {

        double absOfSpeed = Math.abs(speed);
        int ticksToTurn = (int)((360 / degrees) * TICKS_FOR_ONE_ROTATION_OF_ROBOT);
        setMotorsToRunToPosition();
        
        frontLeftMotor.setTargetPosition(ticksToTurn);
        backLeftMotor.setTargetPosition(ticksToTurn);
        frontRightMotor.setTargetPosition(-ticksToTurn);
        backRightMotor.setTargetPosition(-ticksToTurn);

        //If turning counterclockwise, change power to negative
        if(ticksToTurn < 0) absOfSpeed = -absOfSpeed;

        pointTurnAtSpecifiedPower(absOfSpeed);

    }

    @Override
    public void turnContinuously(double speed) {

        setMotorsToRunWithoutEncoder();

        pointTurnAtSpecifiedPower(speed);

    }

    public void setMotorsToRunToPosition()
    {

        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    public void setMotorsToRunWithoutEncoder()
    {

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void pointTurnAtSpecifiedPower(double power)
    {

        frontLeftMotor.setPower(power);
        backLeftMotor.setPower(power);
        frontRightMotor.setPower(-power);
        backRightMotor.setPower(-power);

    }

}
