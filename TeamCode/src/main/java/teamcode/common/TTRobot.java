package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TTRobot {

    private MecanumDriveSystem driveSystem;

    public TTRobot(HardwareMap hardwareMap) {
        TTRobotHardwareManager hardwareManager = new TTRobotHardwareManager(hardwareMap);
        DcMotor frontLeftDrive = hardwareManager.getFrontLeftDrive();
        DcMotor frontRightDrive = hardwareManager.getFrontRightDrive();
        DcMotor backLeftDrive = hardwareManager.getBackLeftDrive();
        DcMotor backRightDrive = hardwareManager.getBackRightDrive();
        driveSystem = new MecanumDriveSystem(frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive);
    }

    public MecanumDriveSystem getDriveSystem() {
        return driveSystem;
    }

}
