package teamcode.autoModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="DoYouEvenLiftBro", group="Auto Modes")


public class LiftTest extends LinearOpMode {

    private DcMotor liftMotor;


    @Override
    public void runOpMode() {
        this.liftMotor= hardwareMap.get(DcMotor.class, "LiftMotor");
        waitForStart();
        lift("U", 1000, 0.5);

    }
    public void lift(String direction, int distance, double speed){

        this.liftMotor.setDirection   (DcMotorSimple.Direction.REVERSE);
        liftMotor.setMode     (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(direction == "F") { //up

            liftMotor.setTargetPosition   (distance);
        }

        if(direction == "B") { //down

            liftMotor.setTargetPosition   (-distance);

        }
        double currentSpeed = (speed);

            liftMotor.setPower    (currentSpeed);

        liftMotor.setPower(0);
    }


}
