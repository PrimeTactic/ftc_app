package teamcode.common;

public class MecanumDriveCalibration {

    private final TTRobot robot;

    public MecanumDriveCalibration(TTRobot robot) {
        this.robot = robot;
    }

    public void start() {
        MecanumDriveSystem driveSystem = robot.getDriveSystem();
        double speed = 0.5;
        driveSystem.vertical(10.0, speed);
        driveSystem.lateral(10.0, speed);
        driveSystem.vertical(-10.0, speed);
        driveSystem.lateral(-10.0, speed);
        int quadrant = 0;
        driveSystem.diagonal(quadrant, Math.sqrt(50), speed);
        driveSystem.turn(360.0,speed);
    }

}
