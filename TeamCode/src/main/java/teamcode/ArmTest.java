package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.lang.reflect.Array;


@TeleOp(name = "ARM TEST", group = "Linear Opmode") //making the code appear in the app



public class ArmTest extends LinearOpMode { //make it work right

    private DcMotor baseJoint;
    private Servo elbowJoint;
    private Servo wristJoint;
    private Servo intakeHand;

    public String currentDevice = "Base";

    @Override
    public void runOpMode() {

        this.baseJoint = hardwareMap.get(DcMotor.class, "Base");

        baseJoint.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseJoint.setMode    (DcMotor.RunMode.RUN_TO_POSITION);

        this.elbowJoint = hardwareMap.get(Servo.class, "Elbow");
        this.wristJoint = hardwareMap.get(Servo.class, "Wrist");
        this.intakeHand = hardwareMap.get(Servo.class, "Hand");

        waitForStart();

        while (opModeIsActive()) {

            selectDevice();

            if(gamepad1.y){

                baseJoint.setTargetPosition(200);

            }

            while(currentDevice == "Hand"){//hand must be continuous

                if(gamepad1.x){

                    this.intakeHand.setPosition(1.0);
                    sleep(150);
                    this.intakeHand.setPosition(0.5);

                }

                if(gamepad1.b){

                    this.intakeHand.setPosition(0.0);
                    sleep(150);
                    this.intakeHand.setPosition(0.5);

                }

                selectDevice();
            }


            while(currentDevice == "Wrist"){//wrist must use encoders

                if(gamepad1.x){

                    this.wristJoint.setPosition(this.wristJoint.getPosition() - 0.01);

                }

                if(gamepad1.b){

                    this.wristJoint.setPosition(this.wristJoint.getPosition() + 0.01);

                }

                selectDevice();
            }


            while(currentDevice == "Elbow"){//elbow must use encoders

                if(gamepad1.x){

                    this.elbowJoint.setPosition(this.elbowJoint.getPosition() - 0.01);

                }

                if(gamepad1.b){

                    this.elbowJoint.setPosition(this.elbowJoint.getPosition() + 0.01);

                }

                selectDevice();
            }

            while(currentDevice == "Base"){//the base is a motor using encoders

                if(gamepad1.x){

                    baseJoint.setTargetPosition(baseJoint.getCurrentPosition() + 10);

                }

                if(gamepad1.b){

                    baseJoint.setTargetPosition(baseJoint.getCurrentPosition() - 10);

                }

                runBaseToPos();
                selectDevice();

            }


        }
    }

    public void selectDevice(){
        if(gamepad1.dpad_up){

            currentDevice = "Base";

        }

        if(gamepad1.dpad_down){

            currentDevice = "Elbow";

        }

        if(gamepad1.dpad_left){

            currentDevice = "Wrist";

        }

        if(gamepad1.dpad_right){

            currentDevice = "Hand";


        }

        updateTelemetry();
    }

    public void updateTelemetry(){

        telemetry.addData("Selected device", currentDevice);
        telemetry.addData("Hand Speed", intakeHand.getPosition());
        telemetry.addData("Wrist Pos", wristJoint.getPosition());
        telemetry.addData("Elbow Pos", elbowJoint.getPosition());
        telemetry.addData("Base Pos", baseJoint.getCurrentPosition());

        telemetry.addData("Base Targer", baseJoint.getTargetPosition());

        telemetry.update();

    }

    public void runBaseToPos(){

        baseJoint.setPower(1);

        while(!motorWithinTarget()) {

        telemetry.update();

            if(gamepad1.x){

                baseJoint.setTargetPosition(baseJoint.getCurrentPosition() + 10);

            }

            if(gamepad1.b){

                baseJoint.setTargetPosition(baseJoint.getCurrentPosition() - 10);

            }

        }

        baseJoint.setPower(0);
    }

    public boolean motorWithinTarget(){

        int difference = (baseJoint.getTargetPosition() - baseJoint.getCurrentPosition());

        return (Math.abs(difference) <= 5);

    }
}