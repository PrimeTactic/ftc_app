package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.Array;


@TeleOp(name = "TestClass", group = "Linear Opmode") //making the code appear in the app



public class ArmTest extends LinearOpMode { //make it work right

    private Servo baseJoint;
    private Servo shoulderJoint;
    private Servo elbowJoint;
    private Servo wristJoint;

    @Override
    public void runOpMode() {

        this.baseJoint= hardwareMap.get(Servo.class, "Base");
        this.shoulderJoint = hardwareMap.get(Servo.class, "Shoulder");
        this.elbowJoint = hardwareMap.get(Servo.class, "Elbow");
        this.wristJoint = hardwareMap.get(Servo.class, "Wrist");

        waitForStart();

        while (opModeIsActive()) {

            String[] motorList = new String[3];

            int selectedJoint  = 0;

            motorList[0] = ("Base");
            motorList[1] = ("Shoulder");
            motorList[2] = ("Elbow");
            motorList[3] = ("Wrist");

            if(gamepad1.a){

                if(selectedJoint == 3 ){



                }

            }




        }
    }
}