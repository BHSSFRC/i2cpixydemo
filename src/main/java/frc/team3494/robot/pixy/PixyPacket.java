package frc.team3494.robot.pixy;

public class PixyPacket {
    public int signature;
    public int x;
    public int y;
    public int width;
    public int height;

    public PixyPacket(int signature, int x, int y, int width, int height) {
        this.height = height;
        this.width = width;
        this.signature = signature;
        this.x = x;
        this.y = y;
    }
}
