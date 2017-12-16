/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Math.sqrt;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="redNintyV2", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class redNintyV2 extends LinearOpMode {

    // Declare OpMode members.
    private KiwiRobot robot;
    private ElapsedTime runtime = new ElapsedTime();

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    int cooldown = 1000;
    private boolean isClamped = true;
    private double row1Position = 0.2;
    private double row2Position = 0.4;
    private double row3Position = 0.6;
    private BNO055IMU gyro;
    final private double OPENCLAMPPOSITION = 0;
    final private double CLOSECLAMPPOSITION = 1;
    final private double LIFTEDARMPOSITION = .55;
    final private double DOWNARMPOSITION = .8;

    final private double JEWELCLOSECLAMP = .05; // close clamp
    final private double JEWELCHOPTIME = JEWELCLOSECLAMP + 1; // jewel servo down
    final private double JEWELARMRAISE = JEWELCHOPTIME + .5; // arm servo up
    final private double SPINTOWIN = JEWELARMRAISE + .1; // turn to knock off jewel
    final private double JEWELSHEATHARM = SPINTOWIN + 2; // raise arm to sheath
    final private double JEWELSTOREARM = JEWELSHEATHARM + .5; // store arm
    final private double JEWELSPINBACK = JEWELSTOREARM + .1; // turn back to compensate for knock off jewel turn

    final private double LIFTARM = JEWELSPINBACK + 2; //lift arm
    final private double PHASETHREE = LIFTARM + 1.8; // drive forward
    final private double PHASETHREEHALF = PHASETHREE + .1; // turn off motors
    final private double PHASEFOUR = PHASETHREEHALF + .5; // lowerarm
    final private double PHASEFIVE = PHASEFOUR + .6; // turn to face columns
    final private double PHASESIX = PHASEFIVE + 2; // drive stright
    final private double PHASESIXHALF = PHASESIX + .1; // turn off motors
    final private double PHASESEVEN = PHASESIXHALF + .5; // open clamp
    final private double PHASEEIGHT = PHASESEVEN + .5; // turn
    final private double PHASENINE = PHASEEIGHT + .2; // back up
    final private double PHASENINEHALF = PHASENINE + .1; // turn off motors

    final private double TURNTOWARDSGLYPHPIT = PHASENINEHALF + .6; // turn towards glyph pit
    final private double DRIVETOGLYPHPIT = TURNTOWARDSGLYPHPIT + 1; // drive to the glyph pit
    final private double GRABABLOCK = DRIVETOGLYPHPIT + 1.5; // grab a block in the pit
    final private double BACKTOBASE = GRABABLOCK + .75; // grab a block in the pit
    final private double TURNTOFACECOLUMNS = BACKTOBASE + .6; // turn to face columns
    //turns off all motors at end


    @Override
    public void runOpMode() {
        KiwiRobot robot = new KiwiRobot(hardwareMap, telemetry);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = false;
        parameters.loggingTag          = "gyro";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        gyro = hardwareMap.get(BNO055IMU.class, "gyro");
        gyro.initialize(parameters);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        double elapsedTime;

        // run until the end of the match (driver presses STOP)
        boolean test = true;
        double speed = .8;
        Boolean isDetected = false;
        double jewelDegrees = -15;//- degrees go right, positive degrees go left
        int column = 0;
        while (opModeIsActive())
        {
            //opmode stuck in stop() resets robot controller;(could be pressing play too early before it finishes initializing)

            elapsedTime = runtime.time();

            robot.rightClampServo.setPosition(1);

            while ((column = robot.getColumn()) == -1 && elapsedTime < 5 ){
                elapsedTime = runtime.time();
            }

            if(column == -1)
            {
                column = 2;
            }

            robot.jewelServo.setPosition(1);
            sleep(1500);

            if (!robot.isJewelRed()&& !isDetected) {
                // the red jewel is on the left of sensor
                jewelDegrees = -jewelDegrees;
                isDetected = !isDetected;
            }
            robot.turnDegrees(speed,jewelDegrees);

            robot.jewelServo.setPosition(.65);
            sleep(700);

            robot.jewelServo.setPosition(0);
            sleep(700);

            robot.armServo.setPosition(LIFTEDARMPOSITION);
            sleep(200);

            robot.turnDegrees(speed,-jewelDegrees);
            robot.turnDegrees(.5,-10);

            double distance = 18.5 + (4 - column) * 7.5;
            if(column == 1)
            {
                distance = distance -1;
            }
            robot.driveForward(.5,distance,true);
            robot.logEnconders();
            robot.updateLog();

            robot.armServo.setPosition(.7);
            sleep(500);

            robot.turnDegrees(-.5,-90);

            robot.driveForward(0.5,16,true);

            robot.openClaw();

            robot.turnDegrees(.5,-30);

            robot.driveForward(.5,-3,true);
            if(column == 1)
            {
                robot.driveLateral(.5,5,true);
                robot.driveForward(.5,3,true);
                robot.turnDegrees(.5,10);
            }
            robot.armServo.setPosition(.8);
            //robot.driveForward(.5,-5,true);

            /*
            //robot.armServo.setPosition(.9);
            double pitDegrees = -145 + (4 - column) * 10;
            robot.turnDegrees(.5,pitDegrees);

            robot.rightClampServo.setPosition(0.7);
            robot.driveForward(.8, 24, true);

            // wiggle
            double turnDegrees = 15;
            robot.turnDegrees(1, -turnDegrees);
            robot.turnDegrees(1,  turnDegrees);
            //robot.turnDegrees(1, -turnDegrees);

            boolean grabbed = false;

            for (int i = 0; i < 2; i++) {
                if (grabbed = robot.grabGlyph()) {
                    break;
                }
                robot.driveForward(.5, 6, true);
            }

            if(!grabbed)
            {
                robot.closeClaw();
            }
            robot.armServo.setPosition(.6);
            robot.driveForward(.5,-12,true);
            double pitDegreesBack = 180 - (4 * column) * 10;
            robot.turnDegrees(.5,pitDegreesBack);
            robot.driveForward(1, 20, true);
            robot.rightClampServo.setPosition(0);
            robot.armServo.setPosition(.8);
            robot.driveForward(.5,-2,true);
            */
            updateTelemetry();
            break;
        }
    }

    public boolean grabGlyph() {
        robot.rightClampServo.setPosition(CLOSECLAMPPOSITION);
        boolean grabbed = robot.isGlyphGrabbed();
        if (!grabbed) {
            robot.rightClampServo.setPosition(0.7);
        }
        return grabbed;
    }

    public void updateTelemetry(){
        //double armServoValue = armServo.getPosition();
        //double leftClampValue = leftClampServo.getPosition();
        //double rightClampValue = rightClampServo.getPosition();
        //boolean aButton = gamepad1.a;
        telemetry.addData("Status", "Run Time   : " + runtime.toString());

        telemetry.update();
    }
}
