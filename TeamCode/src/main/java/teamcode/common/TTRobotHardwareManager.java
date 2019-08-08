package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TTRobotHardwareManager {

    public static final String[] COMPONENT_NAMES = {"FrontLeftDrive", "FrontRightDrive", "BackLeftDrive", "BackRightDrive"};

    private final DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;

    public TTRobotHardwareManager(HardwareMap hardwareMap) {
        frontLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[0]);
        frontRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[1]);
        backLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[2]);
        backRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[3]);
    }

    public DcMotor getFrontLeftDrive() {
        return frontLeftDrive;
    }

    public DcMotor getFrontRightDrive() {
        return frontRightDrive;
    }

    public DcMotor getBackLeftDrive() {
        return backLeftDrive;
    }

    public DcMotor getBackRightDrive() {
        return backRightDrive;
    }

}
