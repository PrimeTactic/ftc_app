package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "New Drive Auto Test", group = "Linear OpMode")
public class NewDriveAutoTest extends LinearOpMode {

    private static final double DRIVE_MOTOR_TICKS_PER_INCH_TRAVELLED = -100.0;

    private DcMotor driveMotor;

    @Override
    public void runOpMode() {
        initHardware();
        waitForStart();
        drive(10.0, 1.0);
        drive(-10.0, 1.0);
        while (opModeIsActive()) ;
    }

    private void initHardware() {
        driveMotor = hardwareMap.get(DcMotor.class, "DriveMotor");
        driveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void drive(double inches, double power) {
        driveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int target = (int) (inches * DRIVE_MOTOR_TICKS_PER_INCH_TRAVELLED);
        driveMotor.setTargetPosition(target);
        driveMotor.setPower(power);
        while (driveMotor.isBusy()) ;
    }

}
