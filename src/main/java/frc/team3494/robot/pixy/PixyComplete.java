package frc.team3494.robot.pixy;

import edu.wpi.first.wpilibj.I2C;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to completely represent a PixyCam. Contains I2C interface and data thread.
 *
 * @author Edwan Vi
 * @author BHSRobotix
 */
public class PixyComplete {
    /**
     * I2C interface with RoboRIO.
     */
    private I2C iface;
    /**
     * Data gathering and translation thread.
     */
    public Thread thread;
    /**
     * Reference to the data retrieved from the Pixy.
     */
    private AtomicReference<PixyPacket> packetRef;

    public PixyComplete(int address) {
        this.iface = new I2C(I2C.Port.kOnboard, address);
        this.packetRef = new AtomicReference<>();
        this.thread = new Thread(new PixyThread(this));
    }

    /**
     * Evil hacking method to turn raw data from the pixy into usable primative ints.
     */
    static int cvt(byte upper, byte lower) {
        return (((int) upper & 0xff) << 8) | ((int) lower & 0xff);
    }

    public void startThread() {
        thread.start();
    }

    public I2C getIface() {
        return iface;
    }
}