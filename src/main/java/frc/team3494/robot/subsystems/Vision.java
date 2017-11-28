package frc.team3494.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team3494.robot.pixy.PixyComplete;

public class Vision extends Subsystem {

    public PixyComplete pixy2;

    public Vision() {
        this.pixy2 = new PixyComplete(0x54);
    }

    @Override
    protected void initDefaultCommand() {
    }
}
