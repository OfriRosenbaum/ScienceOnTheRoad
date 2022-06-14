package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.command.genericsubsystem.MotoredGenericSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotMap;

import java.util.List;

public class Shooter extends MotoredGenericSubsystem {

    private static final double SOLENOID_DURATION = 0.25;

    private static final List<Double> SPEEDS = List.of(0.0, 0.35, 0.420, 0.5);

    private int currentSpeedIndex = 0;

    private boolean lastUpLimitState = false;
    private boolean lastDownLimitState = false;

    private final DigitalInput upLimit, downLimit;
    private final Solenoid solenoid;

    private static Shooter instance;

    public static Shooter getInstance() {
        if (instance == null) {
            MotorController talon = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_TALON);
            MotorController victor = new WPI_VictorSPX(RobotMap.CAN.SHOOTER_VICTOR);
            talon.setInverted(true);
            DigitalInput upLimit = new DigitalInput(RobotMap.DIO.SHOOTER_UP_LIMIT);
            DigitalInput downLimit = new DigitalInput(RobotMap.DIO.SHOOTER_DOWN_LIMIT);
            instance = new Shooter("shooter", upLimit, downLimit,
                    new Solenoid(RobotMap.CAN.PCM, PneumaticsModuleType.CTREPCM, RobotMap.PCM.LED), talon, victor);
        }
        return instance;
    }

    private Shooter(String namespaceName, DigitalInput upLimit, DigitalInput downLimit,
                    Solenoid solenoid, MotorController... motorControllers) {
        super(namespaceName, motorControllers);
        this.upLimit = upLimit;
        this.downLimit = downLimit;
        this.solenoid = solenoid;
    }

    public void configureDashboard() {
        rootNamespace.putNumber("speed", this::getSpeedFromList);
        rootNamespace.putBoolean("up limit", upLimit::get);
        rootNamespace.putBoolean("down limit", downLimit::get);
        rootNamespace.putData("solenoid on", new InstantCommand(() -> solenoid.set(true)));
        rootNamespace.putData("solenoid off", new InstantCommand(() -> solenoid.set(false)));

    }

    @Override
    public void periodic() {
        if (upLimit.get() && !lastUpLimitState && currentSpeedIndex < SPEEDS.size() - 1) {
            currentSpeedIndex++;
            activateSolenoid();
        }
        if (downLimit.get() && !lastDownLimitState && currentSpeedIndex > 0) {
            currentSpeedIndex--;
            activateSolenoid();
        }
        lastUpLimitState = upLimit.get();
        lastDownLimitState = downLimit.get();
        rootNamespace.update();
    }

    public double getSpeedFromList() {
        return SPEEDS.get(currentSpeedIndex);
    }

    public void activateSolenoid() {
        double start = Timer.getFPGATimestamp();
        double now = start;
        solenoid.set(true);
        while (now - start < SOLENOID_DURATION) {
            now = Timer.getFPGATimestamp();
        }
        solenoid.set(false);
    }

    public void zeroSpeed() {
        currentSpeedIndex = 0;
    }
}
