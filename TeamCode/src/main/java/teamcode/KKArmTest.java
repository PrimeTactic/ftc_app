package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class KKArmTest extends LinearOpMode {
    private Servo armServoBase;
    private Servo armServoTop;
    private Servo clawServo;
    private DcMotor armBaseMotor;
    @Override
    //runop calls other methods to control arm- main method


    public void runOpMode() throws InterruptedException {
        initialize();
        while(opModeIsActive()){

        }
    }
    public void initialize() {
        //armbasemotor = hardwareMap.get(DcMotor.class, "motor1");
        //armServo = hardwareMap.get(Servo.class, "armServo");
        //motor1.setDirection(DcMotor.Direction.FORWARD);
        armServoBase.setDirection(Servo.Direction.FORWARD);
        armServoTop.setDirection(Servo.Direction.FORWARD);
        clawServo.setDirection(Servo.Direction.FORWARD);






    }
}

/*
public class LinearSlideTest extends LinearOpMode {
    private DcMotor motor1;

    public void runOpMode() {
        initialize();

        //Always True
        while (opModeIsActive() ) {
            float joystickY = gamepad1.right_stick_y;
            motor1.setPower(joystickY);
        }
    }

    public void initialize() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor1.setDirection(DcMotor.Direction.FORWARD);
    }
}
*/