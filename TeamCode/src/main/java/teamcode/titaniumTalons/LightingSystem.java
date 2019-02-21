package teamcode.titaniumTalons;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Represents a sequence of lighting patterns that can be displayed on the robot.
 */
public class LightingSystem {

    private Map<Double, BlinkinPattern> patternMap;
    boolean started;

    public LightingSystem() {
        patternMap = new HashMap<>();
        started = false;
    }

    public void addPattern(BlinkinPattern pattern, double timeSeconds) {
        validateNotStarted();
        patternMap.put(timeSeconds, pattern);
    }

    public void start() {
        validateNotStarted();
        started = true;
        for (double timeSeconds : patternMap.keySet()) {
            final BlinkinPattern pattern = patternMap.get(timeSeconds);
            TimerTask patternShiftTask = new TimerTask() {
                @Override
                public void run() {
                    HardwareManager.ledDriver.setPattern(pattern);
                }
            };
            RobotTimer.schedule(patternShiftTask, timeSeconds);
        }
    }

    private void validateNotStarted() {
        if (started) {
            throw new IllegalStateException("Lighting system already started");
        }
    }

}
