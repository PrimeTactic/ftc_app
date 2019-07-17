package teamcode.impl;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import teamcode.common.IDriveSystem;
import teamcode.common.Robot;
import teamcode.common.StandardDriveSystem;

public class TTRobot extends Robot {

    private static final String FRONT_LEFT_MOTOR_NAME = "frontLeftMotor";
    private static final String FRONT_RIGHT_MOTOR_NAME = "frontRightMotor";
    private static final String BACK_LEFT_MOTOR_NAME = "backLeftMotor";
    private static final String BACK_RIGHT_MOTOR_NAME = "backRightMotor";
    private static final double WHEEL_DIAMETER_INCHES = 10.0;

    public TTRobot(HardwareMap hardwareMap) {
        super(createDriveSystem(hardwareMap));
    }

    private static IDriveSystem createDriveSystem(HardwareMap hardwareMap) {
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, FRONT_LEFT_MOTOR_NAME);
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, FRONT_RIGHT_MOTOR_NAME);
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, BACK_LEFT_MOTOR_NAME);
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, BACK_RIGHT_MOTOR_NAME);
        return new StandardDriveSystem(frontLeftMotor, frontRightMotor, backLeftMotor, backRightMotor, WHEEL_DIAMETER_INCHES);
    }

}
