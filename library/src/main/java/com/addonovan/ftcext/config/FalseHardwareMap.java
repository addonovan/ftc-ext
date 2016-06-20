package com.addonovan.ftcext.config;

import android.content.Context;
import android.util.Log;

import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.SerialNumber;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Lock;

/**
 * A fake HardwareMap class for the sole purpose of faking a configuration map for
 * the purposed of configuration.
 *
 * @author addonovan
 * @since 6/20/16
 */
public class FalseHardwareMap extends HardwareMap
{

    //
    // Constructors
    //

    public FalseHardwareMap( Context appContext )
    {
        super( appContext );
        dcMotorController = new FalseDeviceMapping<>( emptyMotorController );
        dcMotor = new FalseDeviceMapping<>( new DcMotor( emptyMotorController, 0 ) );
        servoController = new FalseDeviceMapping<>( emptyServoController );
        servo = new FalseDeviceMapping<>( new Servo( emptyServoController, 0 ) );
        legacyModule = new FalseDeviceMapping<>( emptyLegacyModule );
        touchSensorMultiplexer = new FalseDeviceMapping<>( emptyTouchSensorMultiplexer );
        deviceInterfaceModule = new FalseDeviceMapping<>( emptyDeviceInterfaceModule );
        analogInput = new FalseDeviceMapping<>( new AnalogInput( null, 0 ) );
        digitalChannel = new FalseDeviceMapping<>( new DigitalChannel( null, 0 ) );
        opticalDistanceSensor = new FalseDeviceMapping<>( emptyOpticalDistanceSensor );
        touchSensor = new FalseDeviceMapping<>( emptyTouchSensor );
        pwmOutput = new FalseDeviceMapping<>( new PWMOutput( null, 0 ) );
        i2cDevice = new FalseDeviceMapping<>( new I2cDevice( null, 0 ) );
        analogOutput = new FalseDeviceMapping<>( new AnalogOutput( null, 0 ) );
        colorSensor = new FalseDeviceMapping<>( emptyColorSensor );
        led = new FalseDeviceMapping<>( new LED( emptyDigitalChannelController, 0 ) );
        accelerationSensor = new FalseDeviceMapping<>( emptyAccelerationSensor );
        compassSensor = new FalseDeviceMapping<>( emptyCompassSensor );
        gyroSensor = new FalseDeviceMapping<>( emptyGyroSensor );
        irSeekerSensor = new FalseDeviceMapping<>( emptyIrSeekerSensor );
        lightSensor = new FalseDeviceMapping<>( emptyLightSensor );
        ultrasonicSensor = new FalseDeviceMapping<>( emptyUltrasonicSensor );
        voltageSensor = new FalseDeviceMapping<>( emptyVoltageSensor );
    }

    /**
     * A false device mapping that will always return only one variable.
     *
     * @param <T> The type of device mapping this is.
     */
    private class FalseDeviceMapping< T > extends DeviceMapping< T >
    {

        /**
         * The instance that is always returned by this false device mapping.
         */
        private final T instance;

        /**
         * Creates a false device mapping that will always return {@code instance}
         * through the {@code get} method.
         *
         * @param instance The instance of {@code T} to always return.
         */
        public FalseDeviceMapping( T instance )
        {
            this.instance = instance;
        }

        public T get( String deviceName )
        {
            return instance;
        }
    }

    //
    // Now Entering the land of blank interface implementations!
    //

    private DcMotorController emptyMotorController = new DcMotorController()
    {
        @Override
        public void setMotorControllerDeviceMode( DeviceMode deviceMode )
        {
        }

        @Override
        public DeviceMode getMotorControllerDeviceMode()
        {
            return null;
        }

        @Override
        public void setMotorChannelMode( int i, RunMode runMode )
        {
        }

        @Override
        public RunMode getMotorChannelMode( int i )
        {
            return null;
        }

        @Override
        public void setMotorPower( int i, double v )
        {
        }

        @Override
        public double getMotorPower( int i )
        {
            return 0;
        }

        @Override
        public boolean isBusy( int i )
        {
            return false;
        }

        @Override
        public void setMotorPowerFloat( int i )
        {
        }

        @Override
        public boolean getMotorPowerFloat( int i )
        {
            return false;
        }

        @Override
        public void setMotorTargetPosition( int i, int i1 )
        {
        }

        @Override
        public int getMotorTargetPosition( int i )
        {
            return 0;
        }

        @Override
        public int getMotorCurrentPosition( int i )
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {
        }
    };

    private ServoController emptyServoController = new ServoController()
    {
        @Override
        public void pwmEnable()
        {
        }

        @Override
        public void pwmDisable()
        {
        }

        @Override
        public PwmStatus getPwmStatus()
        {
            return null;
        }

        @Override
        public void setServoPosition( int i, double v )
        {
        }

        @Override
        public double getServoPosition( int i )
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {
        }
    };

    private LegacyModule emptyLegacyModule = new LegacyModule()
    {
        @Override
        public void enableAnalogReadMode( int i )
        {
        }

        @Override
        public void enable9v( int i, boolean b )
        {
        }

        @Override
        public void setDigitalLine( int i, int i1, boolean b )
        {
        }

        @Override
        public byte[] readAnalog( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public SerialNumber getSerialNumber()
        {
            return null;
        }

        @Override
        public void enableI2cReadMode( int i, int i1, int i2, int i3 )
        {
        }

        @Override
        public void enableI2cWriteMode( int i, int i1, int i2, int i3 )
        {
        }

        @Override
        public byte[] getCopyOfReadBuffer( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public byte[] getCopyOfWriteBuffer( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public void copyBufferIntoWriteBuffer( int i, byte[] bytes )
        {
        }

        @Override
        public void setI2cPortActionFlag( int i )
        {
        }

        @Override
        public boolean isI2cPortActionFlagSet( int i )
        {
            return false;
        }

        @Override
        public void readI2cCacheFromController( int i )
        {
        }

        @Override
        public void writeI2cCacheToController( int i )
        {
        }

        @Override
        public void writeI2cPortFlagOnlyToController( int i )
        {
        }

        @Override
        public boolean isI2cPortInReadMode( int i )
        {
            return false;
        }

        @Override
        public boolean isI2cPortInWriteMode( int i )
        {
            return false;
        }

        @Override
        public boolean isI2cPortReady( int i )
        {
            return false;
        }

        @Override
        public Lock getI2cReadCacheLock( int i )
        {
            return null;
        }

        @Override
        public Lock getI2cWriteCacheLock( int i )
        {
            return null;
        }

        @Override
        public byte[] getI2cReadCache( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public byte[] getI2cWriteCache( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public void registerForI2cPortReadyCallback( I2cPortReadyCallback i2cPortReadyCallback, int i )
        {
        }

        @Override
        public I2cPortReadyCallback getI2cPortReadyCallback( int i )
        {
            return null;
        }

        @Override
        public void deregisterForPortReadyCallback( int i )
        {
        }

        @Override
        public void registerForPortReadyBeginEndCallback( I2cPortReadyBeginEndNotifications i2cPortReadyBeginEndNotifications, int i )
        {
        }

        @Override
        public I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback( int i )
        {
            return null;
        }

        @Override
        public void deregisterForPortReadyBeginEndCallback( int i )
        {
        }

        @Override
        public void readI2cCacheFromModule( int i )
        {
        }

        @Override
        public void writeI2cCacheToModule( int i )
        {
        }

        @Override
        public void writeI2cPortFlagOnlyToModule( int i )
        {
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {
        }
    };

    private TouchSensorMultiplexer emptyTouchSensorMultiplexer = new TouchSensorMultiplexer()
    {
        @Override
        public boolean isTouchSensorPressed( int i )
        {
            return false;
        }

        @Override
        public int getSwitches()
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private DeviceInterfaceModule emptyDeviceInterfaceModule = new DeviceInterfaceModule()
    {
        @Override
        public int getDigitalInputStateByte()
        {
            return 0;
        }

        @Override
        public void setDigitalIOControlByte( byte b )
        {

        }

        @Override
        public byte getDigitalIOControlByte()
        {
            return 0;
        }

        @Override
        public void setDigitalOutputByte( byte b )
        {

        }

        @Override
        public byte getDigitalOutputStateByte()
        {
            return 0;
        }

        @Override
        public boolean getLEDState( int i )
        {
            return false;
        }

        @Override
        public void setLED( int i, boolean b )
        {

        }

        @Override
        public int getAnalogInputValue( int i )
        {
            return 0;
        }

        @Override
        public SerialNumber getSerialNumber()
        {
            return null;
        }

        @Override
        public void setAnalogOutputVoltage( int i, int i1 )
        {

        }

        @Override
        public void setAnalogOutputFrequency( int i, int i1 )
        {

        }

        @Override
        public void setAnalogOutputMode( int i, byte b )
        {

        }

        @Override
        public Mode getDigitalChannelMode( int i )
        {
            return null;
        }

        @Override
        public void setDigitalChannelMode( int i, Mode mode )
        {

        }

        @Override
        public boolean getDigitalChannelState( int i )
        {
            return false;
        }

        @Override
        public void setDigitalChannelState( int i, boolean b )
        {

        }

        @Override
        public void enableI2cReadMode( int i, int i1, int i2, int i3 )
        {

        }

        @Override
        public void enableI2cWriteMode( int i, int i1, int i2, int i3 )
        {

        }

        @Override
        public byte[] getCopyOfReadBuffer( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public byte[] getCopyOfWriteBuffer( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public void copyBufferIntoWriteBuffer( int i, byte[] bytes )
        {

        }

        @Override
        public void setI2cPortActionFlag( int i )
        {

        }

        @Override
        public boolean isI2cPortActionFlagSet( int i )
        {
            return false;
        }

        @Override
        public void readI2cCacheFromController( int i )
        {

        }

        @Override
        public void writeI2cCacheToController( int i )
        {

        }

        @Override
        public void writeI2cPortFlagOnlyToController( int i )
        {

        }

        @Override
        public boolean isI2cPortInReadMode( int i )
        {
            return false;
        }

        @Override
        public boolean isI2cPortInWriteMode( int i )
        {
            return false;
        }

        @Override
        public boolean isI2cPortReady( int i )
        {
            return false;
        }

        @Override
        public Lock getI2cReadCacheLock( int i )
        {
            return null;
        }

        @Override
        public Lock getI2cWriteCacheLock( int i )
        {
            return null;
        }

        @Override
        public byte[] getI2cReadCache( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public byte[] getI2cWriteCache( int i )
        {
            return new byte[ 0 ];
        }

        @Override
        public void registerForI2cPortReadyCallback( I2cPortReadyCallback i2cPortReadyCallback, int i )
        {

        }

        @Override
        public I2cPortReadyCallback getI2cPortReadyCallback( int i )
        {
            return null;
        }

        @Override
        public void deregisterForPortReadyCallback( int i )
        {

        }

        @Override
        public void registerForPortReadyBeginEndCallback( I2cPortReadyBeginEndNotifications i2cPortReadyBeginEndNotifications, int i )
        {

        }

        @Override
        public I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback( int i )
        {
            return null;
        }

        @Override
        public void deregisterForPortReadyBeginEndCallback( int i )
        {

        }

        @Override
        public void readI2cCacheFromModule( int i )
        {

        }

        @Override
        public void writeI2cCacheToModule( int i )
        {

        }

        @Override
        public void writeI2cPortFlagOnlyToModule( int i )
        {

        }

        @Override
        public void setPulseWidthOutputTime( int i, int i1 )
        {

        }

        @Override
        public void setPulseWidthPeriod( int i, int i1 )
        {

        }

        @Override
        public int getPulseWidthOutputTime( int i )
        {
            return 0;
        }

        @Override
        public int getPulseWidthPeriod( int i )
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private OpticalDistanceSensor emptyOpticalDistanceSensor = new OpticalDistanceSensor()
    {
        @Override
        public double getLightDetected()
        {
            return 0;
        }

        @Override
        public int getLightDetectedRaw()
        {
            return 0;
        }

        @Override
        public void enableLed( boolean b )
        {

        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private TouchSensor emptyTouchSensor = new TouchSensor()
    {
        @Override
        public double getValue()
        {
            return 0;
        }

        @Override
        public boolean isPressed()
        {
            return false;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private ColorSensor emptyColorSensor = new ColorSensor()
    {
        @Override
        public int red()
        {
            return 0;
        }

        @Override
        public int green()
        {
            return 0;
        }

        @Override
        public int blue()
        {
            return 0;
        }

        @Override
        public int alpha()
        {
            return 0;
        }

        @Override
        public int argb()
        {
            return 0;
        }

        @Override
        public void enableLed( boolean b )
        {

        }

        @Override
        public void setI2cAddress( int i )
        {

        }

        @Override
        public int getI2cAddress()
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private DigitalChannelController emptyDigitalChannelController = new DigitalChannelController()
    {
        @Override
        public SerialNumber getSerialNumber()
        {
            return null;
        }

        @Override
        public Mode getDigitalChannelMode( int i )
        {
            return null;
        }

        @Override
        public void setDigitalChannelMode( int i, Mode mode )
        {

        }

        @Override
        public boolean getDigitalChannelState( int i )
        {
            return false;
        }

        @Override
        public void setDigitalChannelState( int i, boolean b )
        {

        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private AccelerationSensor emptyAccelerationSensor = new AccelerationSensor()
    {
        @Override
        public Acceleration getAcceleration()
        {
            return null;
        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private CompassSensor emptyCompassSensor = new CompassSensor()
    {
        @Override
        public double getDirection()
        {
            return 0;
        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public void setMode( CompassMode compassMode )
        {

        }

        @Override
        public boolean calibrationFailed()
        {
            return false;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private GyroSensor emptyGyroSensor = new GyroSensor()
    {
        @Override
        public void calibrate()
        {

        }

        @Override
        public boolean isCalibrating()
        {
            return false;
        }

        @Override
        public int getHeading()
        {
            return 0;
        }

        @Override
        public double getRotation()
        {
            return 0;
        }

        @Override
        public int rawX()
        {
            return 0;
        }

        @Override
        public int rawY()
        {
            return 0;
        }

        @Override
        public int rawZ()
        {
            return 0;
        }

        @Override
        public void resetZAxisIntegrator()
        {

        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private IrSeekerSensor emptyIrSeekerSensor = new IrSeekerSensor()
    {
        @Override
        public void setSignalDetectedThreshold( double v )
        {

        }

        @Override
        public double getSignalDetectedThreshold()
        {
            return 0;
        }

        @Override
        public void setMode( Mode mode )
        {

        }

        @Override
        public Mode getMode()
        {
            return null;
        }

        @Override
        public boolean signalDetected()
        {
            return false;
        }

        @Override
        public double getAngle()
        {
            return 0;
        }

        @Override
        public double getStrength()
        {
            return 0;
        }

        @Override
        public IrSeekerIndividualSensor[] getIndividualSensors()
        {
            return new IrSeekerIndividualSensor[ 0 ];
        }

        @Override
        public void setI2cAddress( int i )
        {

        }

        @Override
        public int getI2cAddress()
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private LightSensor emptyLightSensor = new LightSensor()
    {
        @Override
        public double getLightDetected()
        {
            return 0;
        }

        @Override
        public int getLightDetectedRaw()
        {
            return 0;
        }

        @Override
        public void enableLed( boolean b )
        {

        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private UltrasonicSensor emptyUltrasonicSensor = new UltrasonicSensor()
    {
        @Override
        public double getUltrasonicLevel()
        {
            return 0;
        }

        @Override
        public String status()
        {
            return null;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

    private VoltageSensor emptyVoltageSensor = new VoltageSensor()
    {
        @Override
        public double getVoltage()
        {
            return 0;
        }

        @Override
        public String getDeviceName()
        {
            return null;
        }

        @Override
        public String getConnectionInfo()
        {
            return null;
        }

        @Override
        public int getVersion()
        {
            return 0;
        }

        @Override
        public void close()
        {

        }
    };

}
