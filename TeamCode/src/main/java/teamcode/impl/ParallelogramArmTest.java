package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import teamcode.common.TTOpMode;

@TeleOp(name = "parallelogram")
public class ParallelogramArmTest extends TTOpMode {
    public DcMotor elbow;
    private Servo intake;

    @Override
    protected void onInitialize() {

        elbow = hardwareMap.get(DcMotor.class, "ArmRotation");
        intake = hardwareMap.get(Servo.class, "intakeClaw");
    }


    @Override
    protected void onStart() {
        elbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        while(opModeIsActive()){
            if(gamepad1.dpad_down){
                while(gamepad1.dpad_down) {
                    elbow.setPower(-0.5);
                }
                elbow.setPower(0);
            }else if(gamepad1.dpad_up){
                while(gamepad1.dpad_up) {
                    elbow.setPower(0.5);
                }
                elbow.setPower(0);
            }
        }
    }
}
