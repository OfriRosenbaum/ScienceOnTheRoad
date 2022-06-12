package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import com.spikes2212.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.robot.RobotMap;

public class Shooter extends MotoredGenericSubsystem {

    private static final double STEP = 0.2;

    private boolean lastUpLimitState = false;
    private boolean lastDownLimitState = false;

    private double speed;

    private final DigitalInput upLimit, downLimit;

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            MotorController talon = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON);
            MotorController victor = new WPI_VictorSPX(RobotMap.CAN.SHOOTER_VICTOR);
            victor.setInverted(true);
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
        this.speed = 0;
    }

    public void configureDashboard() {
        rootNamespace.putNumber("speed", () -> speed);
        rootNamespace.putData("activate shooter", new MoveGenericSubsystem(this, () -> speed));
        rootNamespace.putBoolean("up limit", upLimit::get);
        rootNamespace.putBoolean("down limit", downLimit::get);
    }

    @Override
    public void periodic() {
        if (upLimit.get() && !lastUpLimitState && speed + STEP <= 1) speed += STEP;
        if (downLimit.get() && !lastDownLimitState && speed - STEP >= 0) speed -= STEP;
        lastUpLimitState = upLimit.get();
        lastDownLimitState = downLimit.get();
        rootNamespace.update();
    }
}
