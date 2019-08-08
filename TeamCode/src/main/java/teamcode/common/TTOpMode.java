package teamcode.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class TTOpMode extends LinearOpMode {

    private static TTOpMode opMode;

    private TTRobot robot;

    @Override
    public void runOpMode() {
        opMode = this;
        robot = new TTRobot(hardwareMap);
        onInitialize();
        waitForStart();
        onStart();
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
