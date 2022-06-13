package frc.robot;

public class RobotMap {

    public interface CAN {
        int SHOOTER_TALON = 14;
        int SHOOTER_VICTOR = 3;
        int PCM = 1;
    }
    
    public interface DIO {

        int SHOOTER_UP_LIMIT = 0;
        int SHOOTER_DOWN_LIMIT = 1;
    }
    
    public interface PWM {

    }
    
    public interface AIO {

    }

    public interface PCM {
        int LED = 3;
    }
}
