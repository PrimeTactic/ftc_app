package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Pin", group = "Linear OpMode")
public class Pin extends SingletonOpMode {

    @Override
    public void onInitialize() {
    }

    @Override
    public void onStart() {
        while (opModeIsActive()) {
            if (gamepad1.x) {
                HardwareManager.pinServo.setPosition(0.0);
            } else if (gamepad1.y) {
                HardwareManager.pinServo.setPosition(0.5);
            } else if (gamepad1.b) {
                HardwareManager.pinServo.setPosition(1.0);
            }
        }
    }

}
