package teamcode.autopaths;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import teamcode.common.TTArm;
import teamcode.common.TTOpMode;
import teamcode.common.TTParallelogramArm;

@Autonomous(group = "FullAuto")
public class FullAuto extends TTOpMode {
    /* this is going to have all of the tasks the game requires ideally,
    collecting the first Skystone, pulling the foundation,
    getting the second Skystone,
    then proceeding to get as many bricks as possible
    average 4 secs/stone (5 * 6 factoring in the foundation
     */
    @Override
    protected void onInitialize() {

        TTParallelogramArm TTArm = new TTParallelogramArm();
        TTArm.getElbowJoint().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    protected void onStart() {

    }


}
