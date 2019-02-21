package teamcode.titaniumTalons.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import teamcode.titaniumTalons.Arm;
import teamcode.titaniumTalons.HardwareManager;
import teamcode.titaniumTalons.LightingSystem;
import teamcode.titaniumTalons.SingletonOpMode;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    @Override
    protected void onInitialize() {
        Arm.status = Arm.ArmStatus.EXTENDED;
    }

    @Override
    protected void onStart() {
        // in case the robot is stuck on the latch
        HardwareManager.pinServo.setPosition(0.5);

        Arm.setWristServoPos(0.4);
        new DriveInputListener();
        new ArmInputListener();

        enableLights();
    }

    private void enableLights() {
        LightingSystem lightingSystem = new LightingSystem();
        lightingSystem.addPattern(BlinkinPattern.RAINBOW_PARTY_PALETTE, 0.0);
        lightingSystem.addPattern(BlinkinPattern.BLUE_VIOLET, 30.0);
        lightingSystem.addPattern(BlinkinPattern.GREEN, 60.0);
        lightingSystem.addPattern(BlinkinPattern.RED, 90.0);
        lightingSystem.addPattern(BlinkinPattern.STROBE_RED, 105.0);
        lightingSystem.addPattern(BlinkinPattern.STROBE_WHITE, 115.0);
        lightingSystem.start();
    }

}
