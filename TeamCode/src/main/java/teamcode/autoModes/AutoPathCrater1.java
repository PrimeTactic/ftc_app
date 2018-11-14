package teamcode.autoModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

@Autonomous(name = "Auto Path Crater 1", group = "Auto Modes")


public class AutoPathCrater1 extends LinearOpMode {

    private static final double COUNTS_PER_MOTOR_REV = 1120;    // Andymark Neverest 40
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV / (WHEEL_DIAMETER_INCHES * 3.1415));//879.645943005

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

    private ColorSensor colorSensor;


    @Override
    public void runOpMode() {

        this.upLeftMotor = hardwareMap.get(DcMotor.class, "Up Left Motor");
        this.upRightMotor = hardwareMap.get(DcMotor.class, "Up Right Motor");
        this.downLeftMotor = hardwareMap.get(DcMotor.class, "Down Left Motor");
        this.downRightMotor = hardwareMap.get(DcMotor.class, "Down Right Motor");

        this.liftMotor = hardwareMap.get(DcMotor.class, "Lift Motor");

        this.armBase = hardwareMap.get(DcMotor.class, "Arm Motor");

        this.armServo = hardwareMap.get(Servo.class, "Arm Servo");

        this.colorSensor = hardwareMap.get(ColorSensor.class, "Color Sensor");

        telemetry.addData("UL Motor pos", upLeftMotor.getCurrentPosition());
        telemetry.addData("UR Motor Pos", upRightMotor.getCurrentPosition());
        telemetry.addData("DR Motor Pos", downRightMotor.getCurrentPosition());
        telemetry.addData("DL Motor Pos", downLeftMotor.getCurrentPosition());
        telemetry.addData("Arm Servo pos", armServo.getPosition());
        telemetry.addData("Arm Motor Pos", armBase.getCurrentPosition());

        this.downLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.downRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        this.upLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        this.armBase.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        commandLine();
    }

    public boolean isGold() {

        return ((colorSensor.red() >= (1. * colorSensor.blue())) && colorSensor.green() >= (1.4 * colorSensor.blue()));
        //more red and green then blue

    }

    public boolean isWhite(){
        int tolerance = 30;
        return (colorSensor.red() >= 50 &&
                (Math.abs(colorSensor.red() - colorSensor.green()) <= tolerance) &&
                (Math.abs(colorSensor.red() - colorSensor.blue()) <= tolerance) &&
                (Math.abs(colorSensor.green() - colorSensor.blue()) <= tolerance));
    }

    public void commandLine() {

        boolean foundMineral = false;

        moveWrist(0.5); // level the wrist horizontally
        moveLift(-0.5, 3000);//get down
        move("F", 250, 0.8);//get off of the hook
        // turn("R", 125, 0.3);//straightens the robot
        move("L", 1150, 0.65);//move to the minerals

        int scan = scanMineral();//look at the middle mineral
        if(scan == 1){

            foundMineral = true;

            move("L", 1200, 0.8);//move the minerals
            //move("R", 800, 0.8);//move back from mineral

        }

        int loc = 1;

        if (scan != 1) {//if the middle is not gold, check the right one
            move("F", 1100, 0.65);//forward to the first mineral
            loc = 0;
        }



        // scan the minerals
        while (scan != 1 && foundMineral == false) {
            scan = scanMineral();

            if (scan == 1) {
                // found gold
                foundMineral = true;
                move("L", 1200, 0.8);//move the minerals
                //move("R", 800, 0.8);//move back from mineral
                break;
            }
            else if (scan == 2) {
                // found white
                move("B", 1500, 0.8);
                loc++;
            }
            else {
                // found nothing
            }
        }
    }

    public int scanMineral() {
        //checks for the mineral as it walks backwards

        if(isGold()){
            telemetry.addData("Mineral:", "GOLD");
            telemetry.update();
            return 1;
        }
        else if (isWhite()) {
            telemetry.addData("Mineral:", "WHITE");
            telemetry.update();
            return 2;
        }
        else {
            telemetry.addData("Mineral:", "NONE");
            telemetry.update();
            move("B", 180, 1.0);
            return 0;
        }

    }

    public void move(String direction, int distance, double speed) {

        /*
        all motors point clockwise by default
         */
        upRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        upRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        upLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        downRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        downLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (direction == "F") {//forwards movement

            upLeftMotor.setTargetPosition(distance);
            upRightMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(distance);
        }

        if (direction == "B") {//backwards

            upLeftMotor.setTargetPosition(-distance);
            upRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(distance);

        }

        if (direction == "R") {//right

            upLeftMotor.setTargetPosition(distance);
            upRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(-distance);

        }

        if (direction == "L") {//left

            upLeftMotor.setTargetPosition(-distance);
            upRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(distance);
            downRightMotor.setTargetPosition(distance);

        }

        if (direction == "UL") {//up left diagonal

            upLeftMotor.setTargetPosition(0);
            upRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(distance);
            downRightMotor.setTargetPosition(0);

        }

        if (direction == "UR") {//up right diagonal

            upLeftMotor.setTargetPosition(distance);
            upRightMotor.setTargetPosition(0);
            downRightMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(0);

        }

        if (direction == "DR") {//down right diagonal

            upLeftMotor.setTargetPosition(0);
            upRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(0);

        }

        if (direction == "DL") {//down left diagonal

            upLeftMotor.setTargetPosition(-distance);
            upRightMotor.setTargetPosition(0);
            downRightMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(0);

        }


        double currentSpeed = (speed);

        while (!motorsWithinTarget()) {

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
    } // give a move command, stops the command once all motors are within 10 ticks

    public void turn(String direction, int distance, double speed) {

        upRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        upLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        downLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        upRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        upLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        downRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        downLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (direction == "L") {// left

            upRightMotor.setTargetPosition(-distance);
            upLeftMotor.setTargetPosition(-distance);
            downLeftMotor.setTargetPosition(-distance);
            downRightMotor.setTargetPosition(-distance);

        }

        if (direction == "R") {

            upRightMotor.setTargetPosition(distance);
            upLeftMotor.setTargetPosition(distance);
            downLeftMotor.setTargetPosition(distance);
            downRightMotor.setTargetPosition(distance);

        }

        upLeftMotor.setPower(speed);
        upRightMotor.setPower(speed);
        downRightMotor.setPower(speed);
        downLeftMotor.setPower(speed);

        while (motorsWithinTarget() == false) {

            //Loop body can be empty
            telemetry.update();

        }

        upLeftMotor.setPower(0);
        upRightMotor.setPower(0);
        downRightMotor.setPower(0);
        downLeftMotor.setPower(0);

    } // give a move command, stops the command once all motors are within 10 ticks

    public boolean motorsBusy() {

        return (upRightMotor.isBusy() || upLeftMotor.isBusy() || downLeftMotor.isBusy() || downRightMotor.isBusy()) && opModeIsActive();

    }

    public boolean motorsWithinTarget() {

        int ulDif = (upLeftMotor.getTargetPosition() - upLeftMotor.getCurrentPosition());
        int urDif = (upRightMotor.getTargetPosition() - upRightMotor.getCurrentPosition());
        int dlDif = (downLeftMotor.getTargetPosition() - downLeftMotor.getCurrentPosition());
        int drDif = (downRightMotor.getTargetPosition() - downRightMotor.getCurrentPosition());

        return ((Math.abs(ulDif) <= 7 & (Math.abs(urDif) <= 7) & (Math.abs(drDif) <= 7) & (Math.abs(dlDif) < 7)));

    }

    public boolean armMotorWithinTarget() {

        int dif = (armBase.getTargetPosition() - armBase.getCurrentPosition());

        return (Math.abs(dif) <= 10);

    }

    public void moveLift(double power, int time) {

        liftMotor.setPower(power);

        sleep(time);

        liftMotor.setPower(0);

    }

    public void moveWrist(double servoPos) {

        armServo.setPosition(servoPos);

    }

    public void moveBase(int motorPos, double power) {

        armBase.setTargetPosition(motorPos);
        armBase.setPower(power);

        while (!armMotorWithinTarget()) {

            //Loop body can be empty
            telemetry.update();

        }

        armBase.setPower(0.0);
    }

    public void moveBase(int motorPos) {
        moveBase(motorPos, 1.0);
    }

    public void extendArm(int motorPos, double servopos) {

        moveBase(motorPos);
        moveWrist(servopos);
    }
}