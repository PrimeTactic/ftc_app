package teamcode.common;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TTHardwareManager {

    private static final String[] COMPONENT_NAMES = {"FrontLeftDrive", "FrontRightDrive",
            "BackLeftDrive", "BackRightDrive", "IMU", "ArmElbow", "ArmLift", "ArmLiftSensor"};

    private final DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    private final BNO055IMU imu;
    private final DcMotor armElbow, armLift;
    private final DistanceSensor armLiftSensor;

    public TTHardwareManager(HardwareMap hardwareMap, TTHardwareRestriction hardwareRestriction) {
        if (hardwareRestriction == TTHardwareRestriction.ARM_ONLY) {
            frontLeftDrive = null;
            frontRightDrive = null;
            backLeftDrive = null;
            backRightDrive = null;
            imu = null;
        } else {
            frontLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[0]);
            frontRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[1]);
            backLeftDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[2]);
            backRightDrive = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[3]);
            imu = hardwareMap.get(BNO055IMU.class, COMPONENT_NAMES[4]);
        }

        if (hardwareRestriction == TTHardwareRestriction.DRIVE_SYSTEM_ONLY) {
            armElbow = null;
            armLift = null;
            armLiftSensor = null;
        } else {
            armElbow = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[5]);
            armLift = hardwareMap.get(DcMotor.class, COMPONENT_NAMES[6]);
            armLiftSensor = hardwareMap.get(DistanceSensor.class, COMPONENT_NAMES[7]);
        }
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

    public BNO055IMU getIMU() {
        return imu;
    }

    public DcMotor getArmElbow() {
        return armElbow;
    }

    public DcMotor getArmLift() {
        return armLift;
    }

    public DistanceSensor getArmLiftSensor() {
        return armLiftSensor;
    }

    public enum TTHardwareRestriction {
        NONE, ARM_ONLY, DRIVE_SYSTEM_ONLY
    }

}
