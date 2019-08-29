package teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TTOpMode extends LinearOpMode {

    private static TTOpMode opMode;

    private TTRobot robot;

    @Override
    public void runOpMode() {
        opMode = this;
        TTTimer.init();
        robot = new TTRobot(hardwareMap);
        onInitialize();
        waitForStart();
        onStart();
        TTTimer.cancel();
    }

    public static TTOpMode getOpMode() {
        return opMode;
    }

    public TTRobot getRobot() {
        return robot;
    }

    protected abstract void onInitialize();

    protected abstract void onStart();

}
