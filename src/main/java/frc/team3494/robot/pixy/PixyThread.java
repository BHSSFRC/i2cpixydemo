package frc.team3494.robot.pixy;

import frc.team3494.robot.Robot;

public class PixyThread implements Runnable {
    private PixyComplete pixyComplete;

    public PixyThread(PixyComplete p) {
        this.pixyComplete = p;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            // grab data from pixy and convert to proper object
            // we will update the AtomicReference in Robot w/packet object
            byte[] raw = new byte[32];
            try {
                // read off 32 bytes into raw
                pixyComplete.getIface().readOnly(raw, 32);
            } catch (Exception e) {
                // oh no
                e.printStackTrace();
            }
            if (raw.length < 32) { // haha what
                System.out.println("Expected 32 bytes, got " + raw.length);
            } else {
                PixyPacket packet = rawToPacket(raw);
                if (packet != null) {
                    Robot.aRef.set(packet);
                }
            }
        }
        System.out.println("Broke out of loop!");
    }

    /**
     * Converts raw binary data from the I2C port into a more abstract form.
     * Calls to {@link PixyComplete#cvt} are made in this method (don't pass already converted ints as binary.)
     *
     * @param raw The raw data from the I2C port.
     */
    private static PixyPacket rawToPacket(byte[] raw) {
        int i = 0;
        int sync = PixyComplete.cvt(raw[i + 1], raw[i]); // parses bytes [0, 1]
        if (sync == 0xaa55) {
            // first two bytes = syncword
            // (see http://cmucam.org/projects/cmucam5/wiki/Porting_Guide, Object block format)
            // TODO: Handle other syncword (0xaa56) for CC signatures
            sync = PixyComplete.cvt(raw[i + 3], raw[i + 2]); // we may start with one sync word or two so here we parse the next two bytes to see which it was
            if (sync != 0xaa55) { // did not get a second syncword
                i -= 2;
            }

            int checksum = PixyComplete.cvt(raw[i + 5], raw[i + 4]);
            int pixySig = PixyComplete.cvt(raw[i + 7], raw[i + 6]);
            int xPos = PixyComplete.cvt(raw[i + 9], raw[i + 8]);
            int yPos = PixyComplete.cvt(raw[i + 11], raw[i + 10]);
            int width = PixyComplete.cvt(raw[i + 13], raw[i + 12]);
            int height = PixyComplete.cvt(raw[i + 15], raw[i + 14]);

            int calcSum = pixySig + xPos + yPos + width + height; // calculate sum of [4, 13]
            if (calcSum != checksum) {
                // Here's the bottom line guideline: If a client can reasonably be expected to recover from an exception, make it a checked exception.
                // **If a client cannot do anything to recover from the exception, make it an unchecked exception.**
                // ~ https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html
                throw new PixySignatureExecption("Checksum did not match calculated sum!");
            } else {
                return new PixyPacket(pixySig, xPos, yPos, width, height);
            }
        } else {
            return null;
        }
    }
}
