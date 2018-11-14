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

    public String currentDevice = "Base";

    @Override
    public void runOpMode() {

        this.baseJoint = hardwareMap.get(DcMotor.class, "Arm Motor");

        baseJoint.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        baseJoint.setMode    (DcMotor.RunMode.RUN_TO_POSITION);

        this.elbowJoint = hardwareMap.get(Servo.class, "Arm Servo");

        waitForStart();

        while (opModeIsActive()) {

            if(gamepad1.a){

                this.elbowJoint.setPosition(this.elbowJoint.getPosition() - 0.01);

            }

            if(gamepad1.y) {

                this.elbowJoint.setPosition(this.elbowJoint.getPosition() + 0.01);

            }
            if(gamepad1.x){

                baseJoint.setTargetPosition(baseJoint.getCurrentPosition() + 10);

            }

            if(gamepad1.b){

                baseJoint.setTargetPosition(baseJoint.getCurrentPosition() - 10);

            }

            if(gamepad1.dpad_down){

                runBaseToPos();

            }

            updateTelemetry();
        }
    }


    public void updateTelemetry(){

        telemetry.addData("Selected device", currentDevice);
        telemetry.addData("Elbow Pos", elbowJoint.getPosition());
        telemetry.addData("Base Pos", baseJoint.getCurrentPosition());

        telemetry.addData("Base Target", baseJoint.getTargetPosition());

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