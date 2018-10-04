package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name = "drive system", group = "Linear Opmode") //making the code appear in the app



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
    public void runOpMode(){

        waitForStart();
        runtime.reset();

        controlMode = 1;

        this.upLeftMotor    = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor   = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor  = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        telemetry.addData("Up Left Motor Power",    this.upLeftMotor.getPower());
        telemetry.addData("Up Right Motor Power",   this.upRightMotor.getPower());
        telemetry.addData("Down Left Motor Power",  this.downLeftMotor.getPower());
        telemetry.addData("Down Right Motor Power", this.downRightMotor.getPower());
        telemetry.addData("Control Mode",           controlMode);

        /*
        setup stuff goes here
         */


        while (opModeIsActive()) {

            if(gamepad1.dpad_up){

                controlMode =1;

            }

            if(gamepad1.dpad_left){

                controlMode = 2;

            }

            if(gamepad1.dpad_right){

                controlMode = 3;

            }

            telemetry.update();

            leftStickY = gamepad1.left_stick_y;
            leftStickX =  - gamepad1.left_stick_x;
            rightStickX = - gamepad1.right_stick_x;


            //BELOW HERE IS CONTROL MODE FUNCTIONS SETS

            if (controlMode == 1){

                speed1Movement();
                setMotorSpeeds();
                axisLock(1);



                /*
                axis lock mode
                arm controls
                scoop controls
                 */


            }

            if (controlMode == 2){

                speed2Movement();
                setMotorSpeeds();

                /*
                axis lock mode
                arm controls
                scoop controls
                 */

            }

            if (controlMode == 3){


                speed3Movement();
                setMotorSpeeds();

                /*
                tenth speed movement
                axis lock mode
                lift controls
                arm controls
                scoop controls
                 */
            }

            telemetry.update();


        }
    }


    public void speed1Movement() {

        /*
        this method moves the bot at half speed, the fastest we want to go
        it also stops the robot, if the sticks are not moved
         */

        if (rightStickX == 0 & (leftStickX != 0 || leftStickY != 0)) { //if only left stick, move

            upleftMotorPower    = ((-leftStickY / 2) - (leftStickX / 2));
            downLeftMotorPower  = ((-leftStickY / 2) + (leftStickX / 2));
            upRightMotorPower   = ((leftStickY  / 2) - (leftStickX / 2));
            downRightMotorPower = ((leftStickY  / 2) + (leftStickX / 2));

        } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

            upRightMotorPower   = (-rightStickX / 2);
            upleftMotorPower    = (-rightStickX / 2);
            downLeftMotorPower  = (-rightStickX / 2);
            downRightMotorPower = (-rightStickX / 2);

        } else if(rightStickX != 0  & (leftStickY != 0 || leftStickX != 0)){ //if both sticks, move and turn

            upleftMotorPower    = (((-leftStickY / 3) - (leftStickX / 3)) - rightStickX / 3);
            downLeftMotorPower  = (((-leftStickY / 3) + (leftStickX / 3)) - rightStickX / 3);
            upRightMotorPower   = (((leftStickY  / 3) - (leftStickX / 3)) - rightStickX / 3);
            downRightMotorPower = (((leftStickY  / 3) + (leftStickX / 3)) - rightStickX / 3);

        }
        else { //if no sticks, stop

            upRightMotorPower   = 0;
            upleftMotorPower    = 0;
            downLeftMotorPower  = 0;
            downRightMotorPower = 0;


        }
    }

    public void speed2Movement(){

            /*
        this method moves the bot at quarter speed, as if to implement a 'slow mode'
        it also stops the robot, if the sticks are not moved
         */

        if (rightStickX == 0 & (leftStickX != 0 || leftStickY != 0)) { //if only left stick, move

            upleftMotorPower    =   ((-leftStickY / 3) - (leftStickX / 3));
            downLeftMotorPower  =   ((-leftStickY / 3) + (leftStickX / 3));
            upRightMotorPower   =   ((leftStickY  / 3) - (leftStickX / 3));
            downRightMotorPower =   ((leftStickY  / 3) + (leftStickX / 3));

        } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

            upRightMotorPower   =   (-rightStickX / 3);
            upleftMotorPower    =   (-rightStickX / 3);
            downLeftMotorPower  =   (-rightStickX / 3);
            downRightMotorPower =   (-rightStickX / 3);

        } else if(rightStickX != 0  & (leftStickY != 0 || leftStickX != 0)){ //if both sticks, move and turn

            upleftMotorPower    =   (((-leftStickY / 4.5)  -  (leftStickX / 4.5)) - rightStickX / 4.5);
            downLeftMotorPower  =   (((-leftStickY / 4.5)  +  (leftStickX / 4.5)) - rightStickX / 4.5);
            upRightMotorPower   =   (((leftStickY  / 4.5)  -  (leftStickX / 4.5)) - rightStickX / 4.5);
            downRightMotorPower =   (((leftStickY  / 4.5)  +  (leftStickX / 4.5)) - rightStickX / 4.5);

        }
        else { //if no sticks, stop

            upRightMotorPower   = 0;
            upleftMotorPower    = 0;
            downLeftMotorPower  = 0;
            downRightMotorPower = 0;


        }


    }

    public void speed3Movement(){

             /*
        this method moves the bot at the slowest speed for a precision movement mode
        it also stops the robot, if the sticks are not moved
         */

        if (rightStickX == 0 & (leftStickX != 0 || leftStickY != 0)) { //if only left stick, move

            upleftMotorPower    = ((-leftStickY  / 6) - (leftStickX / 6));
            downLeftMotorPower  = ((-leftStickY) / 6 + (leftStickX  / 6));
            upRightMotorPower   = ((leftStickY)  / 6 - (leftStickX  / 6));
            downRightMotorPower = ((leftStickY)  / 6 + (leftStickX  / 6));

        } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

            upRightMotorPower   = (-rightStickX / 6);
            upleftMotorPower    = (-rightStickX / 6);
            downLeftMotorPower  = (-rightStickX / 6);
            downRightMotorPower = (-rightStickX / 6);

        } else if(rightStickX != 0  & (leftStickY != 0 || leftStickX != 0)){ //if both sticks, move and turn

            upleftMotorPower    = (((-leftStickY / 8) - (leftStickX / 8)) - rightStickX / 8);
            downLeftMotorPower  = (((-leftStickY / 8) + (leftStickX / 8)) - rightStickX / 8);
            upRightMotorPower   = (((leftStickY  / 8) - (leftStickX / 8)) - rightStickX / 8);
            downRightMotorPower = (((leftStickY  / 8) + (leftStickX / 8)) - rightStickX / 8);

        }
        else { //if no sticks, stop

            upRightMotorPower   = 0;
            upleftMotorPower    = 0;
            downLeftMotorPower  = 0;
            downRightMotorPower = 0;


        }
    }

    public void axisLock(double motorPower){



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

