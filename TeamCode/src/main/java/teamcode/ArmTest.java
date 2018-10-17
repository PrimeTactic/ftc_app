package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.Array;


@TeleOp(name = "ARM TEST", group = "Linear Opmode") //making the code appear in the app



public class ArmTest extends LinearOpMode { //make it work right

    private Servo baseJoint;
    private Servo shoulderJoint;
    private Servo elbowJoint;
    private Servo wristJoint;
    private Servo intakeHand;

    @Override
    public void runOpMode() {

        this.baseJoint= hardwareMap.get(Servo.class, "Base");
        this.shoulderJoint = hardwareMap.get(Servo.class, "Shoulder");
        this.elbowJoint = hardwareMap.get(Servo.class, "Elbow");
        this.wristJoint = hardwareMap.get(Servo.class, "Wrist");
        this.intakeHand = hardwareMap.get(Servo.class, "Hand");

        waitForStart();

        while (opModeIsActive()) {

            Servo[] motorList = new Servo[3];

            int selectedJoint  = 0;

            motorList[0] = (baseJoint);
            motorList[1] = (shoulderJoint);
            motorList[2] = (elbowJoint);
            motorList[3] = (wristJoint);

            if(gamepad1.a){

                if(selectedJoint == 3){

                    selectedJoint = 0;
                    telemetry.addData("Motor in use", motorList[selectedJoint]);
                    telemetry.update();

                }

                else if(selectedJoint != 3){

                    selectedJoint++;
                    telemetry.addData("Motor in use", motorList[selectedJoint]);
                    telemetry.update();

                }
            }

            if (gamepad1.left_bumper){

                motorList[selectedJoint].setPosition((motorList[selectedJoint].getPosition()) - 0.01);

                sleep(50);

                telemetry.addData("Base Pos", baseJoint.getPosition());
                telemetry.addData("Shoulder Pos", shoulderJoint.getPosition());
                telemetry.addData("Elbow Pos", elbowJoint.getPosition());
                telemetry.addData("Wrist Pos", wristJoint.getPosition());

                telemetry.update();

            }

            if (gamepad1.left_bumper){

                motorList[selectedJoint].setPosition((motorList[selectedJoint].getPosition()) + 0.01);

                sleep(50);

                telemetry.addData("Base Pos", baseJoint.getPosition());
                telemetry.addData("Shoulder Pos", shoulderJoint.getPosition());
                telemetry.addData("Elbow Pos", elbowJoint.getPosition());
                telemetry.addData("Wrist Pos", wristJoint.getPosition());

                telemetry.update();

            }

            if(gamepad1.b){

                intakeHand.setPosition(1.0);

                sleep(150);

                intakeHand.setPosition(0.5);

            }

            if(gamepad1.x){

                intakeHand.setPosition(0);

                sleep(150);

                intakeHand.setPosition(0.5);

            }
        }
    }
}