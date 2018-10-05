package teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "Intake Test", group = "Linear Opmode")

public class IntakeTest extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();

    private Servo servo;


    @Override
    public void runOpMode(){

        waitForStart();
        runtime.reset();

        this.servo = hardwareMap.get(Servo.class, "Servo");


        while (opModeIsActive()){

         if(gamepad1.a == true) {

             this.servo.setPosition(0);

             sleep(500);

             this.servo.setPosition(0.5);

         }

         if(gamepad1.b == true){

             this.servo.setPosition(0.8);

             sleep(500);

             this.servo.setPosition(0.5);

         }

        }
    }
}

