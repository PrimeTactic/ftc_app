package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.LED;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.SingletonOpMode;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    @Override
    protected void onInitialize(){
        Arm.status = Arm.ArmStatus.EXTENDED;
    }

    @Override
    protected void onStart() {
        // in case the robot is stuck on the latch
        HardwareManager.pinServo.setPosition(0.5);

        Arm.setWristServoPos(0.4);
        new DriveInputListener();
        new ArmInputListener();
        new TeleOpLights().enable();
    }

}
