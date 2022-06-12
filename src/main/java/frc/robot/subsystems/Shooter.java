package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

public class Shooter extends MotoredGenericSubsystem {

    private final AnalogPotentiometer potentiometer;

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            MotorController talon = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON);
            MotorController victor = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_VICTOR);
            AnalogPotentiometer potentiometer = new AnalogPotentiometer(RobotMap.AIO.SHOOTER_POTENTIOMETER);
            instance = new Shooter("shooter", potentiometer, talon, victor);
        }
        return instance;
    }

    private Shooter(String namespaceName, AnalogPotentiometer potentiometer, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
        this.potentiometer = potentiometer;
    }

    public void configureDashboard() {
        rootNamespace.putNumber("speed", this::getSpeed);
        rootNamespace.putData("activate shooter", new MoveGenericSubsystem(this, this::getSpeed));
    }

    public double getSpeed() {
        return Math.abs(potentiometer.get());
    }


}
