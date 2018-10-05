package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;


@Autonomous(name="Auto Test", group="Auto Modes")


public class AutoTest extends LinearOpMode {

    static final double     COUNTS_PER_MOTOR_REV    = 540 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);


    private DcMotor upRightMotor;
    private DcMotor upLeftMotor;
    private DcMotor downLeftMotor;
    private DcMotor downRightMotor;


    @Override
    public void runOpMode() {

        this.upLeftMotor = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        telemetry.addData("UL Motor pos", upLeftMotor.getCurrentPosition());
        telemetry.addData("UR Motor Pos", upRightMotor.getCurrentPosition());
        telemetry.addData("DR Motor Pos", downRightMotor.getCurrentPosition());
        telemetry.addData("DL Motor Pos", downLeftMotor.getCurrentPosition());

        waitForStart();

        //COMMAND LINE BELOW HERE

        move("F", 2000, 0.4);
        move("B", 2000, 0.4);

    }


    public void move(String direction, int distance, double speed){

        /*
        all motors point clockwise by default
         */
        upRightMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upLeftMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downRightMotor.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downLeftMotor.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        upRightMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        upLeftMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);
        downRightMotor.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        downLeftMotor.setMode   (DcMotor.RunMode.RUN_TO_POSITION);

        if(direction == "F") {//forwards movement

            upLeftMotor.setTargetPosition   (distance);
            upRightMotor.setTargetPosition  (-distance);
            downRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition (distance);
        }

        if(direction == "B") {//backwards

            upLeftMotor.setTargetPosition   (-distance);
            upRightMotor.setTargetPosition  (distance);
            downLeftMotor.setTargetPosition (-distance);
            downRightMotor.setTargetPosition(distance);
        }

        if(direction == "R"){//right

            upLeftMotor.setTargetPosition   (distance);
            upRightMotor.setTargetPosition  (distance);
            downLeftMotor.setTargetPosition (-distance);
            downRightMotor.setTargetPosition(-distance);
        }

        if(direction == "L"){//left

            upLeftMotor.setTargetPosition   (-distance);
            upRightMotor.setTargetPosition  (-distance);
            downLeftMotor.setTargetPosition (distance);
            downRightMotor.setTargetPosition(distance);
        }

        upLeftMotor.setPower(0);
        upRightMotor.setPower(0);
        downRightMotor.setPower(0);
        downLeftMotor.setPower(0);

        double currentSpeed = (speed / 4);

        while((upRightMotor.isBusy() || upLeftMotor.isBusy() || downLeftMotor.isBusy() || downRightMotor.isBusy()) && opModeIsActive()) {

            //Loop body can be empty
            telemetry.update();

            if(currentSpeed < speed){

                currentSpeed = currentSpeed + 0.005;

            }

            upLeftMotor.setPower(currentSpeed);
            upRightMotor.setPower(currentSpeed);
            downRightMotor.setPower(currentSpeed);
            downLeftMotor.setPower(currentSpeed);

        }

        upLeftMotor.setPower(0);
        upRightMotor.setPower(0);
        downRightMotor.setPower(0);
        downLeftMotor.setPower(0);
    }

    public void turn(String direction, int distance, double speed){

        upRightMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upLeftMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downRightMotor.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downLeftMotor.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        upRightMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        upLeftMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);
        downRightMotor.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        downLeftMotor.setMode   (DcMotor.RunMode.RUN_TO_POSITION);

        upRightMotor.setTargetPosition  (-distance);
        upLeftMotor.setTargetPosition   (-distance);
        downLeftMotor.setTargetPosition (-distance);
        downRightMotor.setTargetPosition(-distance);


        if(direction == "L") {// left

            upLeftMotor.setPower        (speed);
            upRightMotor.setPower   (speed);
            downRightMotor.setPower (speed);
            downLeftMotor.setPower  (speed);

        }

        if(direction == "R"){

            upLeftMotor.setPower    (-speed);
            upRightMotor.setPower   (-speed);
            downRightMotor.setPower (-speed);
            downLeftMotor.setPower  (-speed);

        }

            while((upRightMotor.isBusy() || upLeftMotor.isBusy() || downLeftMotor.isBusy() || downRightMotor.isBusy()) && opModeIsActive()) {

                //Loop body can be empty
                telemetry.update();

            }

        upLeftMotor.setPower    (0);
        upRightMotor.setPower   (0);
        downRightMotor.setPower (0);
        downLeftMotor.setPower  (0);

    }
}
