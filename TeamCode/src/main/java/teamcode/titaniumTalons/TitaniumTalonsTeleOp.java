package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import teamcode.Vector2;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onStart() {
        while (opModeIsActive()) {
            driveInputUpdate();
        }
    }

    private void driveInputUpdate() {
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        Vector2 driveVector = new Vector2(-driveX, driveY);
        double turnSpeed = gamepad1.left_stick_x;
        Drive.driveIndefinite(driveVector, turnSpeed);
    }

}
