package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.reflect.Array;


@TeleOp(name = "ARM TEST", group = "Linear Opmode") //making the code appear in the app



public class PowerArmTest extends LinearOpMode { //make it work right

   private DcMotor basemotor;

    @Override
    public void runOpMode() {

        this.basemotor= hardwareMap.get(DcMotor.class, "Base");

        waitForStart();

        while(opModeIsActive()){


            if (gamepad1.x){

                basemotor.setPower(1);

                sleep(50);

                basemotor.setPower(0);

            }

            if(gamepad1.b){

                basemotor.setPower(-1);

                sleep(50);

                basemotor.setPower(0);
            }


        }


    }
}