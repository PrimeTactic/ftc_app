package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name = "Drive System", group = "Linear Opmode") //making the code appear in the app



public class XdriveSystem extends LinearOpMode{ //make it work right

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor upRightMotor;
    private DcMotor upLeftMotor;
    private DcMotor downLeftMotor;
    private DcMotor downRightMotor;

    private double upRightMotorPower;
    private double upleftMotorPower;
    private double downLeftMotorPower;
    private double downRightMotorPower;

    double leftStickY;
    double leftStickX;
    double rightStickX;

    private int controlMode;


/*
put variables above here, but in the class still
 */


    @Override
    public void runOpMode() {

        waitForStart();
        runtime.reset();


        this.upLeftMotor = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        this.downLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.downRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Up Left Motor Power", this.upLeftMotor.getPower());
        telemetry.addData("Up Right Motor Power", this.upRightMotor.getPower());
        telemetry.addData("Down Left Motor Power", this.downLeftMotor.getPower());
        telemetry.addData("Down Right Motor Power", this.downRightMotor.getPower());

        /*
        setup stuff goes here
         */

        /*
        GAMEPAD BUTTON USES BELOW HERE
         */

        while (opModeIsActive()) {

            leftStickY = gamepad1.left_stick_y;
            leftStickX = -gamepad1.left_stick_x;
            rightStickX = -gamepad1.right_stick_x;


            //this block of code works as the axis lock, simply by locking the movement to an axis if the right trigger is held



            /*
            COMMAND SETS BELOW HERE
             */

            if(gamepad1.right_trigger != 0){

                sprintMovement();

            }
            else if(gamepad1.right_trigger == 0){

                regularMovement();

            }
            setMotorSpeeds();
            telemetry.update();

        }
    }


    /*
    METHODS FOR STUFF BELOW HERE
     */

    public void regularMovement() {

        /*
        this method moves the bot at half speed, the fastest we want to go
        it also stops the robot, if the sticks are not moved
         */

            if (rightStickX == 0 & (leftStickX != 0 || leftStickY != 0)) { //if only left stick, move

                upleftMotorPower    = ((-leftStickY / 2) - (leftStickX / 2));
                downLeftMotorPower  = ((-leftStickY / 2) + (leftStickX / 2));
                upRightMotorPower   = ((leftStickY / 2)  - (leftStickX / 2));
                downRightMotorPower = ((leftStickY / 2)  + (leftStickX / 2));

            } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

                upRightMotorPower   = (-rightStickX);
                upleftMotorPower    = (-rightStickX);
                downLeftMotorPower  = (-rightStickX);
                downRightMotorPower = (-rightStickX);

            } else if (rightStickX != 0 & (leftStickY != 0 || leftStickX != 0)) { //if both sticks, move and turn

                upleftMotorPower    = (((-leftStickY / 3) - (leftStickX / 3)) - rightStickX / 3);
                downLeftMotorPower  = (((-leftStickY / 3) + (leftStickX / 3)) - rightStickX / 3);
                upRightMotorPower   = (((leftStickY / 3)  - (leftStickX / 3)) - rightStickX / 3);
                downRightMotorPower = (((leftStickY / 3)  + (leftStickX / 3)) - rightStickX / 3);

            } else { //if no sticks, stop

                upRightMotorPower   = 0;
                upleftMotorPower    = 0;
                downLeftMotorPower  = 0;
                downRightMotorPower = 0;


            }
        }

    public void sprintMovement(){

        if(Math.abs(leftStickX) > Math.abs(leftStickY)){ //if x is greater than y, only move on the x axis

            upRightMotorPower   = -leftStickX;
            upleftMotorPower    = -leftStickX;
            downLeftMotorPower  = leftStickX;
            downRightMotorPower = leftStickX;

        }

        else if(Math.abs(leftStickX) < Math.abs(leftStickY)){ //if y is greater than x, only move on the y axis

            upRightMotorPower   = leftStickY;
            upleftMotorPower    = -leftStickY;
            downLeftMotorPower  = -leftStickY;
            downRightMotorPower = leftStickY;

        }else { //if no sticks, stop

            upRightMotorPower   = 0;
            upleftMotorPower    = 0;
            downLeftMotorPower  = 0;
            downRightMotorPower = 0;

        }
    }

    public void setMotorSpeeds(){

        //this method just sets the motor powers to the variables i told them to be at

        this.upRightMotor.setPower(upRightMotorPower);
        this.upLeftMotor.setPower(upleftMotorPower);
        this.downLeftMotor.setPower(downLeftMotorPower);
        this.downRightMotor.setPower(downRightMotorPower);

        telemetry.update();

    }
}