package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LED;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.SingletonOpMode;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    @Override
    protected void onInitialize(){
        // nothing
    }

    @Override
    protected void onStart() {
        Arm.lockElbow();
        Arm.setWristServoPos(0.4);
        new DriveInputListener();
        new ArmInputListener();
    }

}
