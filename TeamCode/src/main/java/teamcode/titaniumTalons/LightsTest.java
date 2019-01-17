package teamcode.titaniumTalons;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.TimerTask;

@TeleOp(name = "Lights Test", group = "Linear OpMode")
public class LightsTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        waitForStart();
        telemetry.addData("status", "started");
        telemetry.update();
        RobotTimer timer = new RobotTimer(this);

        final RevBlinkinLedDriver ledDriver = hardwareMap.get(RevBlinkinLedDriver.class, "LedDriver");

        RevBlinkinLedDriver.BlinkinPattern green = RevBlinkinLedDriver.BlinkinPattern.RAINBOW_PARTY_PALETTE;
        ledDriver.setPattern(green);

        TimerTask yellow = new TimerTask() {
            @Override
            public void run() {
                RevBlinkinLedDriver.BlinkinPattern yellow = RevBlinkinLedDriver.BlinkinPattern.BLUE_VIOLET;
                ledDriver.setPattern(yellow);
            }
        };

        TimerTask orange = new TimerTask() {
            @Override
            public void run() {
                RevBlinkinLedDriver.BlinkinPattern orange = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                ledDriver.setPattern(orange);
            }
        };

        TimerTask solidRed = new TimerTask() {
            @Override
            public void run() {
                RevBlinkinLedDriver.BlinkinPattern solidRed = RevBlinkinLedDriver.BlinkinPattern.RED;
                ledDriver.setPattern(solidRed);
            }
        };

        TimerTask flashingRed = new TimerTask() {
            @Override
            public void run() {
                RevBlinkinLedDriver.BlinkinPattern flashingRed = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
                ledDriver.setPattern(flashingRed);
            }
        };

        TimerTask flashingWhite = new TimerTask() {
            @Override
            public void run() {
                RevBlinkinLedDriver.BlinkinPattern flashingWhite = RevBlinkinLedDriver.BlinkinPattern.STROBE_WHITE;
                ledDriver.setPattern(flashingWhite);
            }
        };

        timer.schedule(yellow, 30.0);
        timer.schedule(orange, 60.0);
        timer.schedule(solidRed, 90.0);
        timer.schedule(flashingRed, 105.0);
        timer.schedule(flashingWhite, 115.0);

        while (opModeIsActive()) ;
        // 2 to 1: green
        // 1 to 30: yellow
        // 30 to 15: solid red
        // last 15: flashing red
        // last 5: white flashing
    }

}
