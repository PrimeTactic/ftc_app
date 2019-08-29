package teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TTHardwareManager {

    private static final String[] COMPONENT_NAMES = {"FrontLeftDrive", "FrontRightDrive", "BackLeftDrive", "BackRightDrive", "ArmElbow", "ArmLift"};

    private final DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    private final DcMotor armElbow, armLift;

    public TTHardwareManager(HardwareMap hardwareMap) {
        frontLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[0]);
        frontRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[1]);
        backLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[2]);
        backRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[3]);

        armElbow = null;
        armLift = null;
        //armElbow = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[4]);
        //armLift = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[5]);
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

    public DcMotor getArmElbow() {
        return armElbow;
    }

    public DcMotor getArmLift() {
        return armLift;
    }

}
