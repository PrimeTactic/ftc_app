package teamcode.autoModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Auto Path Depot 1", group="Auto Modes")


public class AutoPathDepot1 extends LinearOpMode {

    private static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // Andymark Neverest 40
    private static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    private static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.1415));//879.645943005

    /*\
    roughly 880 steps to move a wheel an inch, but this is untested
     */


    private DcMotor upRightMotor;
    private DcMotor upLeftMotor;
    private DcMotor downLeftMotor;
    private DcMotor downRightMotor;

    private DcMotor armBase;

    private DcMotor liftMotor;

    private Servo armServo;


    @Override
    public void runOpMode() {

        this.upLeftMotor = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        this.liftMotor = hardwareMap.get(DcMotor.class, "Lift Motor");

        this.armBase = hardwareMap.get(DcMotor.class, "Arm Motor");

        this.armServo = hardwareMap.get(Servo.class, "Arm Servo");

        telemetry.addData("UL Motor pos", upLeftMotor.getCurrentPosition());
        telemetry.addData("UR Motor Pos", upRightMotor.getCurrentPosition());
        telemetry.addData("DR Motor Pos", downRightMotor.getCurrentPosition());
        telemetry.addData("DL Motor Pos", downLeftMotor.getCurrentPosition());
        telemetry.addData("Arm Servo pos", armServo.getPosition());
        telemetry.addData("Arm Motor Pos", armBase.getCurrentPosition());

        this.downLeftMotor.setDirection (DcMotorSimple.Direction.REVERSE);
        this.downRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upRightMotor.setDirection  (DcMotorSimple.Direction.REVERSE);
        this.upLeftMotor.setDirection   (DcMotorSimple.Direction.REVERSE);

        this.armBase.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //this comment made by romanesque architecture gang

        waitForStart();

        commandLine();

    }

    public void commandLine(){

        moveLift(-0.4, 4000);//get down
        move("F", 250, 1.0);//get off of the hook
        move("R", 75, 0.5);//aligns the robot on the side of the lander
        move("L", 1300, 1.0);//move to the minerals
        move("F", 950, 1.0);//forward to the first mineral


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

            upLeftMotor.setTargetPosition   (0);
            upRightMotor.setTargetPosition  (-distance);
            downLeftMotor.setTargetPosition (distance);
            downRightMotor.setTargetPosition(0);

        }

        if(direction == "UR"){//up right diagonal

            upLeftMotor.setTargetPosition   (distance);
            upRightMotor.setTargetPosition  (0);
            downRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition (0);

        }

        if(direction == "DR"){//down right diagonal

            upLeftMotor.setTargetPosition   (0);
            upRightMotor.setTargetPosition  (distance);
            downLeftMotor.setTargetPosition (-distance);
            downRightMotor.setTargetPosition(0);

        }

        if(direction == "DL") {//down left diagonal

            upLeftMotor.setTargetPosition   (-distance);
            upRightMotor.setTargetPosition  (0);
            downRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition (0);

        }



        double currentSpeed = (speed);

        while(!motorsWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

            /*if(currentSpeed < speed){

                currentSpeed = currentSpeed + 0.01;

            }
            */
            upLeftMotor.setPower    (currentSpeed);
            upRightMotor.setPower   (currentSpeed);
            downRightMotor.setPower (currentSpeed);
            downLeftMotor.setPower  (currentSpeed);

        }

        upLeftMotor.setPower    (0);
        upRightMotor.setPower   (0);
        downRightMotor.setPower (0);
        downLeftMotor.setPower  (0);
    } // give a move command, stops the command once all motors are within 10 ticks

    public void turn(String direction, int distance, double speed){

        upRightMotor.setMode    (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upLeftMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downRightMotor.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downLeftMotor.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        upRightMotor.setMode    (DcMotor.RunMode.RUN_TO_POSITION);
        upLeftMotor.setMode     (DcMotor.RunMode.RUN_TO_POSITION);
        downRightMotor.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        downLeftMotor.setMode   (DcMotor.RunMode.RUN_TO_POSITION);



        if(direction == "L") {// left

            upRightMotor.setTargetPosition  (-distance);
            upLeftMotor.setTargetPosition   (-distance);
            downLeftMotor.setTargetPosition (-distance);
            downRightMotor.setTargetPosition(-distance);

        }

        if(direction == "R"){

            upRightMotor.setTargetPosition  (distance);
            upLeftMotor.setTargetPosition   (distance);
            downLeftMotor.setTargetPosition (distance);
            downRightMotor.setTargetPosition(distance);

        }

        upLeftMotor.setPower    (speed);
        upRightMotor.setPower   (speed);
        downRightMotor.setPower (speed);
        downLeftMotor.setPower  (speed);

        while(motorsWithinTarget() == false) {

            //Loop body can be empty
            telemetry.update();

        }

        upLeftMotor.setPower    (0);
        upRightMotor.setPower   (0);
        downRightMotor.setPower (0);
        downLeftMotor.setPower  (0);

    } // give a move command, stops the command once all motors are within 10 ticks

    public boolean motorsBusy(){

        return (upRightMotor.isBusy() || upLeftMotor.isBusy() || downLeftMotor.isBusy() || downRightMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget(){

        int ulDif = (upLeftMotor.getTargetPosition() - upLeftMotor.getCurrentPosition());
        int urDif = (upRightMotor.getTargetPosition() - upRightMotor.getCurrentPosition());
        int dlDif = (downLeftMotor.getTargetPosition() - downLeftMotor.getCurrentPosition());
        int drDif = (downRightMotor.getTargetPosition() - downRightMotor.getCurrentPosition());

        return ((Math.abs(ulDif) <= 10& (Math.abs(urDif) <= 10) & (Math.abs(drDif) <= 10) & (Math.abs(dlDif) < 10)));

    }

    public boolean armMotorWithinTarget(){

        int dif = (armBase.getTargetPosition() - armBase.getCurrentPosition());

        return (Math.abs(dif) <= 10);

    }

    public void moveLift(double power, int time){

        liftMotor.setPower(power);

        sleep(time);

        liftMotor.setPower(0);

    }

    public void extendArm(int target, double speed){

        armBase.setTargetPosition(target);

        armBase.setPower(speed);

        while(!armMotorWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

            upLeftMotor.setPower(0.0);
        }

        armServo.setPosition(0.2);

    }
}