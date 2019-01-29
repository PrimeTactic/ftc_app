package teamcode.tensorFlow;

public class MineralCriteria {

    private double minVerticalCenter, maxVerticalCenter, minVerticalWidth, maxVerticalWidth;

    public MineralCriteria(double minVerticalCenter, double maxVerticalCenter, double minVerticalWidth, double maxVerticalWidth) {
        this.minVerticalCenter = minVerticalCenter;
        this.maxVerticalCenter = maxVerticalCenter;
        this.minVerticalWidth = minVerticalWidth;
        this.maxVerticalWidth = maxVerticalWidth;
    }

    public double getMinVerticalCenter() {
        return minVerticalCenter;
    }

    public double getMaxVerticalCenter(){
        return maxVerticalCenter;
    }

    public double getMinVerticalWidth() {
        return minVerticalWidth;
    }

    public double getMaxVerticalWidth() {
        return maxVerticalWidth;
    }

}
