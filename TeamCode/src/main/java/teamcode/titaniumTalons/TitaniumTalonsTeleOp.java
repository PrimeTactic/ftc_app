package teamcode.titaniumTalons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

import teamcode.Vector2;

@TeleOp(name = "TitaniumTalonsTeleOp", group = "Linear OpMode")
public class TitaniumTalonsTeleOp extends SingletonOpMode {

    private Timer timer;

    @Override
    protected void onInitialize() {
        timer = new Timer();
    }

    @Override
    protected void onStart() {
        while (opModeIsActive()) {
            driveInputUpdate();
            armInputUpdate();
        }
    }

    private void driveInputUpdate() {
        float driveX = gamepad1.right_stick_x;
        float driveY = gamepad1.right_stick_y;
        Vector2 driveVector = new Vector2(-driveX, driveY);
        double turnSpeed = gamepad1.left_stick_x;
        Drive.driveIndefinite(driveVector, turnSpeed);
    }

    private boolean intakeGateOpened = false;
    private boolean gateOnCooldown = false;

    private void armInputUpdate() {
        if (gamepad1.y) {
            if (Arm.getStatus() == Arm.ArmStatus.EXTENDED) {
                Arm.retract();
            }
        } else if (gamepad1.a) {
            if (Arm.getStatus() == Arm.ArmStatus.RETRACTED) {
                Arm.extend();
            }
        }
        if (gamepad1.x) {
            if (!gateOnCooldown) {
                if (intakeGateOpened) {
                    Arm.closeIntakeGate();
                    startGateCooldown();
                } else {
                    Arm.openIntakeGate();
                    startGateCooldown();
                }
                intakeGateOpened = !intakeGateOpened;
            }
        }
        if (gamepad1.right_trigger > gamepad1.left_trigger) {
            // intake
            Arm.setIntakePower(gamepad1.right_trigger);
        } else {
            // outtake
            Arm.setIntakePower(-gamepad1.left_trigger);
        }
    }

    private void startGateCooldown() {
        gateOnCooldown = true;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gateOnCooldown = false;
            }
        };
        timer.schedule(task, 500);
    }

}
