package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "Game Sytem", group = "Linear Opmode") //making the code appear in the app



public class XdriveSystem extends LinearOpMode{ //make it work right

    private DcMotor upRightMotor;
    private DcMotor upLeftMotor;
    private DcMotor downLeftMotor;
    private DcMotor downRightMotor;

    private DcMotor liftMotor;

    private DcMotor armBase;

    private Servo armElbow;
    private Servo armWrist;
    private Servo intakeHand;

    private double upRightMotorPower;
    private double upLeftMotorPower;
    private double downLeftMotorPower;
    private double downRightMotorPower;

    private double leftStickY;
    private double leftStickX;
    private double rightStickX;


/*
put variables above here, but in the class still
 */


    @Override
    public void runOpMode() {

        initializeHardware();

        waitForStart();

        while (opModeIsActive()) {

            leftStickY = gamepad1.left_stick_y;
            leftStickX = -gamepad1.left_stick_x;
            rightStickX = -gamepad1.right_stick_x;

            commandSet();
            updateTelemetry();

        }
    }

    private void commandSet() {

        movement();

        armLoop();

        updateTelemetry();

    }

    private void regularMovement() {

        /*
        this method moves the bot at half speed, the fastest we want to go
        it also stops the robot, if the sticks are not moved
         */

        if (rightStickX == 0 & (leftStickX != 0 || leftStickY != 0)) { //if only left stick, move

            upLeftMotorPower =      ((-leftStickY / 2) - (leftStickX / 2));
            downLeftMotorPower =    ((-leftStickY / 2) + (leftStickX / 2));
            upRightMotorPower =     ((leftStickY / 2)  - (leftStickX / 2));
            downRightMotorPower =   ((leftStickY / 2)  + (leftStickX / 2));

        } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

            upRightMotorPower =     (-rightStickX);
            upLeftMotorPower =      (-rightStickX);
            downLeftMotorPower =    (-rightStickX);
            downRightMotorPower =   (-rightStickX);

        } else if (rightStickX != 0 & (leftStickY != 0 || leftStickX != 0)) { //if both sticks, move and turn

            upLeftMotorPower =      (((-leftStickY / 3) - (leftStickX / 3)) - rightStickX / 3);
            downLeftMotorPower =    (((-leftStickY / 3) + (leftStickX / 3)) - rightStickX / 3);
            upRightMotorPower =     (((leftStickY / 3)  - (leftStickX / 3)) - rightStickX / 3);
            downRightMotorPower =   (((leftStickY / 3)  + (leftStickX / 3)) - rightStickX / 3);

        } else { //if no sticks, stop

            upRightMotorPower =     0;
            upLeftMotorPower =      0;
            downLeftMotorPower =    0;
            downRightMotorPower =   0;

        }

        setDriveMotorSpeeds();
        updateTelemetry();

    }

    private void sprintMovement(){

        if(Math.abs(leftStickX) > Math.abs(leftStickY)){ //if x is greater than y, only move on the x axis

            upRightMotorPower   = -leftStickX;
            upLeftMotorPower    = -leftStickX;
            downLeftMotorPower  = leftStickX;
            downRightMotorPower = leftStickX;

        }

        else if(Math.abs(leftStickX) < Math.abs(leftStickY)){ //if y is greater than x, only move on the y axis

            upRightMotorPower   = leftStickY;
            upLeftMotorPower    = -leftStickY;
            downLeftMotorPower  = -leftStickY;
            downRightMotorPower = leftStickY;

        } else if (rightStickX != 0 & (leftStickX == 0 & leftStickY == 0)) { //if only right stick, turn

            upRightMotorPower   = (-rightStickX);
            upLeftMotorPower    = (-rightStickX);
            downLeftMotorPower  = (-rightStickX);
            downRightMotorPower = (-rightStickX);

        } else if (rightStickX != 0 & (leftStickY != 0 || leftStickX != 0)) { //if both sticks, move and turn

            upLeftMotorPower    = (((-leftStickY / 3) - (leftStickX / 3)) - rightStickX / 3);
            downLeftMotorPower  = (((-leftStickY / 3) + (leftStickX / 3)) - rightStickX / 3);
            upRightMotorPower   = (((leftStickY / 3)  - (leftStickX / 3)) - rightStickX / 3);
            downRightMotorPower = (((leftStickY / 3)  + (leftStickX / 3)) - rightStickX / 3);

        }else { //if no sticks, stop

            upRightMotorPower   = 0;
            upLeftMotorPower    = 0;
            downLeftMotorPower  = 0;
            downRightMotorPower = 0;

        }

        setDriveMotorSpeeds();
        updateTelemetry();

    }

    private void setDriveMotorSpeeds(){

        //this method just sets the motor powers to the variables i told them to be at

        this.upRightMotor.setPower(upRightMotorPower);
        this.upLeftMotor.setPower(upLeftMotorPower);
        this.downLeftMotor.setPower(downLeftMotorPower);
        this.downRightMotor.setPower(downRightMotorPower);

        updateTelemetry();

    }

    private void armLoop(){

        if(gamepad1.b){

            armWrist.setPosition(0.1);
            armElbow.setPosition(.5694);

            armBase.setTargetPosition(-475);

        }

        if(!motorWithinTarget()){

            armBase.setPower(1.0);

        }

        if(motorWithinTarget()){

            armBase.setPower(0.0);

        }
    }

    private boolean motorWithinTarget(){

        int difference = (armBase.getTargetPosition() - armBase.getCurrentPosition());

        return (Math.abs(difference) <= 5);//if the motor is within 5 ticks in either direction

    }

    private void movement(){

        if(gamepad1.right_trigger != 0){

            sprintMovement();

        }
        else if(gamepad1.right_trigger == 0){

            regularMovement();

        }

    }

    private void initializeHardware(){

        this.upLeftMotor = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        this.liftMotor = hardwareMap.get(DcMotor.class, "Lift Motor");

        this.armBase = hardwareMap.get(DcMotor.class, "Base");

        this.armElbow = hardwareMap.get(Servo.class, "Elbow");
        this.armWrist = hardwareMap.get(Servo.class, "Wrist");
        this.intakeHand = hardwareMap.get(Servo.class, "Hand");

        this.downLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.downRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        armBase.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        updateTelemetry();
    }

    private void updateTelemetry() {

        telemetry.addData("Up Left Motor Power", this.upLeftMotor.getPower());
        telemetry.addData("Up Right Motor Power", this.upRightMotor.getPower());
        telemetry.addData("Down Left Motor Power", this.downLeftMotor.getPower());
        telemetry.addData("Down Right Motor Power", this.downRightMotor.getPower());

        telemetry.addData("lift Motor Pos", this.liftMotor.getCurrentPosition());

        telemetry.addData("Arm Base Pos", this.armBase.getCurrentPosition());

        telemetry.addData("Arm Elbow Pos", this.armElbow.getPosition());
        telemetry.addData("Arm Wrist Pos", this.armWrist.getPosition());
        telemetry.addData("Arm Hand Pos", this.intakeHand.getPosition());

        telemetry.update();

    }

}