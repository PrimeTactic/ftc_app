package teamcode.impl;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


@Autonomous
public class InvertedMotors extends LinearOpMode
{

    private DcMotor motorOne, motorTwo;

    @Override
    public void runOpMode()
    {

        initialize();

        telemetry.addData("Status: ", "Ready");
        telemetry.update();
        waitForStart();
        while (opModeIsActive())
        {

            motorOne.setPower(0.25);
            motorTwo.setPower(-0.25);

        }

    }

    public void initialize()
    {

        motorOne = hardwareMap.get(DcMotor.class, "motorOne");
        motorTwo = hardwareMap.get(DcMotor.class, "motorTwo");

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

    }

}
