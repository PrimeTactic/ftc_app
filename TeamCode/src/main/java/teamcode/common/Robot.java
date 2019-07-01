package teamcode.common;

public class Robot {

    private final IDriveSystem driveSystem;

    public Robot(IDriveSystem driveSystem) {
        this.driveSystem = driveSystem;
    }

    public IDriveSystem getDriveSystem() {
        return driveSystem;
    }

}
