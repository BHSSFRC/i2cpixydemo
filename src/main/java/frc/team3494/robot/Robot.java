package frc.team3494.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3494.robot.pixy.PixyPacket;
import frc.team3494.robot.subsystems.Vision;

import java.util.concurrent.atomic.AtomicReference;

public class Robot extends IterativeRobot {

    private static Vision vision;
    public static AtomicReference<PixyPacket> aRef;

    @Override
    public void robotInit() {
        vision = new Vision();
        aRef = new AtomicReference<>();
        vision.pixy2.startThread();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
        System.out.println("Auton Init");
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void testInit() {
    }


    @Override
    public void disabledPeriodic() {
        SmartDashboard.putBoolean("Pixy alive", vision.pixy2.thread.isAlive());
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putBoolean("Pixy alive", vision.pixy2.thread.isAlive());

        PixyPacket p = aRef.get();
        if (p == null) {
            return;
        }
        SmartDashboard.putNumber("Pixy X (atomic)", p.x);
        System.out.format("Pixy X (atomic) at %d: " + p.x + "\n", System.currentTimeMillis());
        SmartDashboard.putNumber("Pixy Y (atomic)", p.y);
        SmartDashboard.putNumber("Pixy width (atomic)", p.width);
        SmartDashboard.putNumber("Pixy height (atomic)", p.height);
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testPeriodic() {
    }
}