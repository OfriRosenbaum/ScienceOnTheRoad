package frc.robot.subsystems;

import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

public class Shooter extends MotoredGenericSubsystem {

    private static final double STEP = 0.2;

    private double speed = 0;

    private final DigitalInput upLimit, downLimit;

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            MotorController talon = null;//new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON);
            MotorController victor = null;//new WPI_TalonSRX(RobotMap.CAN.SHOOTER_VICTOR);
            DigitalInput upLimit = new DigitalInput(RobotMap.DIO.SHOOTER_UP_LIMIT);
            DigitalInput downLimit = new DigitalInput(RobotMap.DIO.SHOOTER_DOWN_LIMIT);
            instance = new Shooter("shooter", upLimit, downLimit, talon, victor);
        }
        return instance;
    }

    private Shooter(String namespaceName, DigitalInput upLimit, DigitalInput downLimit, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
        this.upLimit = upLimit;
        this.downLimit = downLimit;
    }

    public void configureDashboard() {
        rootNamespace.putNumber("speed", () -> speed);
        rootNamespace.putData("activate shooter", new MoveGenericSubsystem(this, () -> speed));
    }

    @Override
    public void periodic() {
        if (upLimit.get()) speed += STEP;
        if (downLimit.get()) speed -= STEP;
    }
}
