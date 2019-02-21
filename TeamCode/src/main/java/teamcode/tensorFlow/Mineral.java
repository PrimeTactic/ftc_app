package teamcode.tensorFlow;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class Mineral {
    private float left;
    private float right;
    private float top;
    private float bottom;
    private float confidence;
    private boolean isGold;
    private double angle; //degrees
    private double a;
    private double b;
    private double c;

    public Mineral(Recognition recognition) throws IllegalArgumentException {
        if (recognition == null) {
            throw new IllegalArgumentException("The recognition parameter cannot be null.");
        }

        this.left = recognition.getLeft();
        this.right = recognition.getRight();
        this.top = recognition.getTop();
        this.bottom = recognition.getBottom();
        this.confidence = recognition.getConfidence();
        this.isGold = recognition.getLabel().equals(TensorFlowManager.LABEL_GOLD_MINERAL);
    }

    public float getLeft() {
        return this.left;
    }

    public float getRight() {
        return this.right;
    }

    public float getTop() {
        return this.top;
    }

    public float getBottom() {
        return this.bottom;
    }

    public float getConfidence() {
        return this.confidence;
    }

    public boolean isGold() {
        return this.isGold;
    }

    // degrees
    public double getAngle() {
        return angle;
    }

    // degrees
    public void setAngle(double newAngle) {
        angle = newAngle;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getA() {
        return this.a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getB() {
        return this.b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getC() {
        return this.c;
    }

    public double getHorizontalCenter() {
        return (left + right) / 2;
    }

    public double getVerticalCenter() {
        return (top + bottom) / 2;
    }

    public double gethorizontalWidth() {
        return right - left;
    }

    public double getVerticalWidth() {
        return top - bottom;
    }

    /**
     * Returns true if this mineral matches the specified {@link MineralCriteria}.
     */
    public boolean matchesCriteria(MineralCriteria criteria) {
        double centr = getVerticalCenter();
        double width = getVerticalWidth();
        return (centr > criteria.getMinVerticalCenter() && centr < criteria.getMaxVerticalCenter()
                && width > criteria.getMinVerticalWidth() && width < criteria.getMaxVerticalWidth());
    }

}
