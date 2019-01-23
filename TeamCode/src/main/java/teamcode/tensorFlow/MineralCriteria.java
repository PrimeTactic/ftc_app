package teamcode.tensorFlow;

public class MineralCriteria {

    private double minCenter, maxCenter, minWidth, maxWidth;

    public MineralCriteria(double minCenter, double maxCenter, double minWidth, double maxWidth) {
        this.minCenter = minCenter;
        this.maxCenter = maxCenter;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }

    public double getMinCenter() {
        return minCenter;
    }

    public double getMaxCenter(){
        return maxCenter;
    }

    public double getMinWidth() {
        return minWidth;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

}
