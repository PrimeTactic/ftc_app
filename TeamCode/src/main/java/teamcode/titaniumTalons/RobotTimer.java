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

    private static Timer timer = new Timer();

    private RobotTimer() {
        // do not instantiate
    }

    public static void schedule(TimerTask task, double delaySeconds) {
        timer.schedule(task, (long) (delaySeconds * 1000));
    }

    public static void scheduleAtFixedRate(TimerTask task, double periodSeconds) {
        timer.scheduleAtFixedRate(task, 0L, (long) (periodSeconds * 1000));
    }

    public static void cancel() {
        timer.cancel();
    }

}
