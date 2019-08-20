package teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TTOpMode extends LinearOpMode {

    private static TTOpMode opMode;

    private Timer timer;
    private TTRobot robot;

    @Override
    public void runOpMode() {
        opMode = this;
        timer = new Timer();
        robot = new TTRobot(hardwareMap);
        onInitialize();
        waitForStart();
        onStart();
    }

    public static TTOpMode getOpMode() {
        return opMode;
    }

    public void scheduleDelayedTask(TimerTask task, double delaySeconds) {
        timer.schedule(task, (long) (delaySeconds * 1000));
    }

    public void scheduleRepeatingTask(TimerTask task, double periodSeconds) {
        timer.scheduleAtFixedRate(task, 0L, (long) (periodSeconds * 1000));
    }


    public TTRobot getRobot() {
        return robot;
    }

    protected abstract void onInitialize();

    protected abstract void onStart();

}
