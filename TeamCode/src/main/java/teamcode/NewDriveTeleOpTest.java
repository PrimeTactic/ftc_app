package teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "New Drive TeleOp Test", group = "Linear OpMode")
public class NewDriveTeleOpTest extends LinearOpMode {

    private DcMotor driveMotor;

    @Override
    public void runOpMode() {
        initHardware();
        waitForStart();
        while (opModeIsActive()) {
            update();
        }
    }

    private void initHardware() {
        driveMotor = hardwareMap.get(DcMotor.class, "DriveMotor");
        driveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void update() {
        float forward = Math.abs(gamepad1.left_stick_y) > Math.abs(gamepad1.right_stick_y) ? gamepad1.left_stick_y / 2 : gamepad1.right_stick_y;
        driveMotor.setPower(forward);
    }

}
