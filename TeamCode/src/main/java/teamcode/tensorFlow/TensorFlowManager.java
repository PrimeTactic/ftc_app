package teamcode.tensorFlow;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TensorFlowManager {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private HardwareMap hardwareMap;
    private TFObjectDetector tfod;
    private VisionManager visionManager;

    public TensorFlowManager(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        this.visionManager = new VisionManager();
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.tfod != null) {
            this.tfod.shutdown();
        }
        super.finalize();
    }

    public TFObjectDetector getObjectDetector() {
        return tfod;
    }

    public List<Mineral> getRecognizedMinerals() {
        List<Recognition> updatedRecognitions = null;
        while (updatedRecognitions == null) {
            updatedRecognitions = tfod.getUpdatedRecognitions();
        }
        List<Mineral> minerals = new ArrayList<Mineral>();
        for (Recognition recognition : updatedRecognitions) {
            Mineral mineral = new Mineral(recognition);
            minerals.add(mineral);
        }

        Collections.sort(minerals, new MineralComparator());
        return minerals;
    }

    public void initialize() {
        this.visionManager.initialize();
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, this.visionManager.getVuforia());
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        tfod.activate();
    }
}
