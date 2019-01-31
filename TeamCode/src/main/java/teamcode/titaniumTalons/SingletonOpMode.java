package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class SingletonOpMode extends LinearOpMode {

    public static SingletonOpMode instance;

    @Override
    public void runOpMode() {
        instance = this;
        RobotTimer.initialize();
        HardwareManager.initialize(hardwareMap);
        Arm.initialize();
        onInitialize();
        waitForStart();
        onStart();
        while (opModeIsActive()) ;
        RobotTimer.cancel();
    }

    /**
     * Returns whether the current OpMode instance is active.
     */
    public static boolean active() {
        return instance.opModeIsActive();
    }

    /**
     * Invoked when the initialize button is pressed on the driver phone.
     */
    protected abstract void onInitialize();

    /**
     * Invoked when the play button is pressed on the driver phone.
     */
    protected abstract void onStart();

}
