package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TTRobot {

    private TTDriveSystem driveSystem;
    private TTArm arm;

    public TTRobot(HardwareMap hardwareMap) {
        TTHardwareManager hardwareManager = new TTHardwareManager(hardwareMap);
        DcMotor frontLeftDrive = hardwareManager.getFrontLeftDrive();
        DcMotor frontRightDrive = hardwareManager.getFrontRightDrive();
        DcMotor backLeftDrive = hardwareManager.getBackLeftDrive();
        DcMotor backRightDrive = hardwareManager.getBackRightDrive();
        driveSystem = new TTDriveSystem(frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive);

        DcMotor armElbow = hardwareManager.getArmElbow();
        DcMotor armLift = hardwareManager.getArmLift();
        arm = new TTArm(armElbow, armLift);
    }

    public TTDriveSystem getDriveSystem() {
        return driveSystem;
    }

    public TTArm getArm() {
        return arm;
    }

}
