package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

import javax.net.ssl.SSLPeerUnverifiedException;


@Autonomous(name="Auto Test", group="Auto Modes")


public class AutoTest extends LinearOpMode {

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // Andymark Neverest 40
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.1415));//879.645943005

    /*
    roughly 880 steps to move a wheel an inch, but this is untested
     */


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


        //this comment made by romanesque architecture gang

        waitForStart();

        //COMMAND LINE BELOW HERE

        turn("L", 5000, 1.0);
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

        if(direction == "UL"){//up left diagonal

            upLeftMotor.setTargetPosition(0);
            upRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(distance);
            downRightMotor.setTargetPosition(0);

        }

        if(direction == "UR"){//up right diagonal

            upLeftMotor.setTargetPosition(distance);
            upRightMotor.setTargetPosition(0);
            downRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(0);

        }

        if(direction == "DR"){//down right diagonal

            upLeftMotor.setTargetPosition(0);
            upRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(0);

        }

        if(direction == "DL") {//down left diagonal

            upLeftMotor.setTargetPosition(-distance);
            upRightMotor.setTargetPosition(0);
            downRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(0);

        }



        double currentSpeed = (speed);

        while((upRightMotor.isBusy() || upLeftMotor.isBusy() || downLeftMotor.isBusy() || downRightMotor.isBusy()) && opModeIsActive()) {

            //Loop body can be empty
            telemetry.update();

            /*if(currentSpeed < speed){

                currentSpeed = currentSpeed + 0.01;

            }
            */
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

            upLeftMotor.setPower    (speed);
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

    public void wiggleMove(String direction, int distance, double speed){}
}