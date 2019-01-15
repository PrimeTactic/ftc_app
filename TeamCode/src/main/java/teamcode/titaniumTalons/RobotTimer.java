package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A timer that cancels all tasks scheduled to it once the current OpMode becomes inactive.
 */
public class RobotTimer {

    private static final long CHECK_FOR_DESTRUCT_PERIOD = 10L;

    private Timer timer;

    /**
     * Uses the current {@link SingletonOpMode} in use.
     */
    public RobotTimer() {
        this(SingletonOpMode.instance);
    }

    /**
     * Allows you to specify the OpMode. Use this for testing
     */
    public RobotTimer(final LinearOpMode opMode) {
        timer = new Timer();
        TimerTask selfDestructTask = new TimerTask() {
            @Override
            public void run() {
                if (!opMode.opModeIsActive()) {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(selfDestructTask, 0, CHECK_FOR_DESTRUCT_PERIOD);
    }

    public void schedule(TimerTask task, double delaySeconds) {
        timer.schedule(task, (long) (delaySeconds * 1000));
    }

    public void scheduleAtFixedRate(TimerTask task, double periodSeconds) {
        timer.scheduleAtFixedRate(task, 0L, (long) (periodSeconds * 1000));
    }

}
