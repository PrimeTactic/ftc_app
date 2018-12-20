package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class SingletonOpMode extends LinearOpMode {

    public static SingletonOpMode instance;

    @Override
    public void runOpMode() {
        instance = this;
        HardwareManager.initialize(hardwareMap);
        onInitialize();
        waitForStart();
        onStart();
    }

    /**
     * Returns whether the current OpMode instance is active.
     */
    public static boolean active() {
        return instance.opModeIsActive();
    }

    /**
     * Invoked when the 'init' button is pressed on the driver phone.
     */
    protected abstract void onInitialize();

    /**
     * Invoked when the 'start' button is pressed on the driver phone.
     */
    protected abstract void onStart();

}
