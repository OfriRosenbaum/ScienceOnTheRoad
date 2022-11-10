package frc.robot;

public class RobotMap {

    public interface CAN {

        int CONVEYOR_TALON = 5;
        int PCM = 1;
    }
    
    public interface DIO {

        int CONVEYOR_INCREASE_LIMIT = 2;
        int CONVEYOR_DECREASE_LIMIT = 1;
    }
    
    public interface PWM {

    }
    
    public interface AIO {

    }

    public interface PCM {
        int LED = 3;
    }
}
