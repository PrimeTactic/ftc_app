package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name = "TestClass", group = "Linear Opmode") //making the code appear in the app



public class TestClass extends LinearOpMode { //make it work right

    private Servo testMotor;

    @Override
    public void runOpMode() {

        this.testMotor = hardwareMap.get(Servo.class, "Motor");
        this.testMotor.resetDeviceConfigurationForOpMode();

        waitForStart();

        while (opModeIsActive()) {

            while(gamepad1.left_trigger > 0){

                this.testMotor.setPosition(0.5 + (gamepad1.left_trigger / 2));
            }

            while(gamepad1.right_trigger > 0){

                this.testMotor.setPosition(0.5 - (gamepad1.right_trigger / 2));

            }

            this.testMotor.setPosition(0.5);

        }
    }
}