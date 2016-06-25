package com.addonovan.ftcext.config

import android.content.Context
import com.qualcomm.robotcore.hardware.*

/**
 * A fake HardwareMap class for the sole purpose of faking a configuration map for
 * the purposed of configuration.

 * @author addonovan
 * *
 * @since 6/20/16
 */
class FalseHardwareMap( app: Context ) : HardwareMap( app )
{

    //
    // The Land of blank interface implementations
    //

    // formatter:off

    private val emptyMotorController = object : DcMotorController
    {
        override fun setMotorControllerDeviceMode( deviceMode: DcMotorController.DeviceMode ){}
        override fun getMotorControllerDeviceMode() = null;
        override fun setMotorChannelMode( i: Int, runMode: DcMotorController.RunMode ){}
        override fun getMotorChannelMode( i: Int ) = null;
        override fun setMotorPower( i: Int, v: Double ) {}
        override fun getMotorPower( i: Int ) = 0.0;
        override fun isBusy( i: Int ) = false;
        override fun setMotorPowerFloat( i: Int ) {}
        override fun getMotorPowerFloat( i: Int ) = false;
        override fun setMotorTargetPosition( i: Int, i1: Int ) {}
        override fun getMotorTargetPosition( i: Int ) = 0;
        override fun getMotorCurrentPosition( i: Int ) = 0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyServoController = object : ServoController
    {
        override fun pwmEnable() {}
        override fun pwmDisable() {}
        override fun getPwmStatus() = null;
        override fun setServoPosition( i: Int, v: Double ) {}
        override fun getServoPosition( i: Int ) = 0.0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyLegacyModule = object : LegacyModule
    {
        override fun enableAnalogReadMode( i: Int ) {}
        override fun enable9v( i: Int, b: Boolean ) {}
        override fun setDigitalLine( i: Int, i1: Int, b: Boolean ) {}
        override fun readAnalog( i: Int ) = ByteArray( 0 );
        override fun getSerialNumber() = null;
        override fun enableI2cReadMode( i: Int, i1: Int, i2: Int, i3: Int ) {}
        override fun enableI2cWriteMode( i: Int, i1: Int, i2: Int, i3: Int ) {}
        override fun getCopyOfReadBuffer( i: Int ) = ByteArray( 0 );
        override fun getCopyOfWriteBuffer( i: Int ) = ByteArray( 0 );
        override fun copyBufferIntoWriteBuffer( i: Int, bytes: ByteArray ) {}
        override fun setI2cPortActionFlag( i: Int ) {}
        override fun isI2cPortActionFlagSet( i: Int ) = false;
        override fun readI2cCacheFromController( i: Int ) {}
        override fun writeI2cCacheToController( i: Int ) {}
        override fun writeI2cPortFlagOnlyToController( i: Int ) {}
        override fun isI2cPortInReadMode( i: Int ) = false;
        override fun isI2cPortInWriteMode( i: Int ) = false;
        override fun isI2cPortReady( i: Int ) = false;
        override fun getI2cReadCacheLock( i: Int ) = null;
        override fun getI2cWriteCacheLock( i: Int ) = null;
        override fun getI2cReadCache( i: Int ) = ByteArray( 0 );
        override fun getI2cWriteCache( i: Int ) = ByteArray( 0 );
        override fun registerForI2cPortReadyCallback( i2cPortReadyCallback: I2cController.I2cPortReadyCallback, i: Int ) {}
        override fun getI2cPortReadyCallback( i: Int ) = null;
        override fun deregisterForPortReadyCallback( i: Int ) {}
        override fun registerForPortReadyBeginEndCallback( i2cPortReadyBeginEndNotifications: I2cController.I2cPortReadyBeginEndNotifications, i: Int ) {}
        override fun getPortReadyBeginEndCallback( i: Int ) = null;
        override fun deregisterForPortReadyBeginEndCallback( i: Int ) {}
        override fun readI2cCacheFromModule( i: Int ) {}
        override fun writeI2cCacheToModule( i: Int ) {}
        override fun writeI2cPortFlagOnlyToModule( i: Int ) {}
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyTouchSensorMultiplexer = object : TouchSensorMultiplexer
    {
        override fun isTouchSensorPressed( i: Int ) = false;
        override fun getSwitches() = 0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyDeviceInterfaceModule = object : DeviceInterfaceModule
    {
        override fun getDigitalInputStateByte() = 0;
        override fun setDigitalIOControlByte( b: Byte ) {}
        override fun getDigitalIOControlByte() = 0.toByte();
        override fun setDigitalOutputByte( b: Byte ) {}
        override fun getDigitalOutputStateByte() = 0.toByte();
        override fun getLEDState( i: Int ) = false;
        override fun setLED( i: Int, b: Boolean ) {}
        override fun getAnalogInputValue( i: Int ) = 0;
        override fun getSerialNumber() = null;
        override fun setAnalogOutputVoltage( i: Int, i1: Int ) {}
        override fun setAnalogOutputFrequency( i: Int, i1: Int ) {}
        override fun setAnalogOutputMode( i: Int, b: Byte ) {}
        override fun getDigitalChannelMode( i: Int ) = null;
        override fun setDigitalChannelMode( i: Int, mode: DigitalChannelController.Mode ) {}
        override fun getDigitalChannelState( i: Int ) = false;
        override fun setDigitalChannelState( i: Int, b: Boolean ) {}
        override fun enableI2cReadMode( i: Int, i1: Int, i2: Int, i3: Int ) {}
        override fun enableI2cWriteMode( i: Int, i1: Int, i2: Int, i3: Int ) {}
        override fun getCopyOfReadBuffer( i: Int ) = ByteArray( 0 );
        override fun getCopyOfWriteBuffer( i: Int ) = ByteArray( 0 );
        override fun copyBufferIntoWriteBuffer( i: Int, bytes: ByteArray ) {}
        override fun setI2cPortActionFlag( i: Int ) {}
        override fun isI2cPortActionFlagSet( i: Int ) = false;
        override fun readI2cCacheFromController( i: Int ) {}
        override fun writeI2cCacheToController( i: Int ) {}
        override fun writeI2cPortFlagOnlyToController( i: Int ) {}
        override fun isI2cPortInReadMode( i: Int ) = false;
        override fun isI2cPortInWriteMode( i: Int ) = false;
        override fun isI2cPortReady( i: Int ) = false;
        override fun getI2cReadCacheLock( i: Int ) = null;
        override fun getI2cWriteCacheLock( i: Int ) = null;
        override fun getI2cReadCache( i: Int ) = ByteArray( 0 );
        override fun getI2cWriteCache( i: Int ) = ByteArray( 0 );
        override fun registerForI2cPortReadyCallback( i2cPortReadyCallback: I2cController.I2cPortReadyCallback, i: Int ) {}
        override fun getI2cPortReadyCallback( i: Int ) = null;
        override fun deregisterForPortReadyCallback( i: Int ) {}
        override fun registerForPortReadyBeginEndCallback( i2cPortReadyBeginEndNotifications: I2cController.I2cPortReadyBeginEndNotifications, i: Int ) {}
        override fun getPortReadyBeginEndCallback( i: Int ) = null;
        override fun deregisterForPortReadyBeginEndCallback( i: Int ) {}
        override fun readI2cCacheFromModule( i: Int ) {}
        override fun writeI2cCacheToModule( i: Int ) {}
        override fun writeI2cPortFlagOnlyToModule( i: Int ) {}
        override fun setPulseWidthOutputTime( i: Int, i1: Int ) {}
        override fun setPulseWidthPeriod( i: Int, i1: Int ) {}
        override fun getPulseWidthOutputTime( i: Int ) = 0;
        override fun getPulseWidthPeriod( i: Int ) = 0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyOpticalDistanceSensor = object : OpticalDistanceSensor
    {
        override fun getLightDetected() = 0.0;
        override fun getLightDetectedRaw() = 0;
        override fun enableLed( b: Boolean ) {}
        override fun status() = null;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyTouchSensor = object : TouchSensor
    {
        override fun getValue() = 0.0;
        override fun isPressed() = false;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyColorSensor = object : ColorSensor
    {
        override fun red() = 0;
        override fun green() = 0;
        override fun blue() = 0;
        override fun alpha() = 0;
        override fun argb() = 0;
        override fun enableLed( b: Boolean ) {}
        override fun setI2cAddress( i: Int ) {}
        override fun getI2cAddress() = 0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyDigitalChannelController = object : DigitalChannelController
    {
        override fun getSerialNumber() = null;
        override fun getDigitalChannelMode( i: Int ) = null;
        override fun setDigitalChannelMode( i: Int, mode: DigitalChannelController.Mode ) {}
        override fun getDigitalChannelState( i: Int ) = false;
        override fun setDigitalChannelState( i: Int, b: Boolean ) {}
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyAccelerationSensor = object : AccelerationSensor
    {
        override fun getAcceleration() = null;
        override fun status() = null;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyCompassSensor = object : CompassSensor
    {
        override fun getDirection() = 0.0;
        override fun status() = null;
        override fun setMode( compassMode: CompassSensor.CompassMode ) {}
        override fun calibrationFailed() = false;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyGyroSensor = object : GyroSensor
    {
        override fun calibrate() {}
        override fun isCalibrating() = false;
        override fun getHeading() = 0;
        override fun getRotation() = 0.0;
        override fun rawX() = 0;
        override fun rawY() = 0;
        override fun rawZ() = 0;
        override fun resetZAxisIntegrator() {}
        override fun status() = null;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyIrSeekerSensor = object : IrSeekerSensor
    {
        override fun setSignalDetectedThreshold( v: Double ) {}
        override fun getSignalDetectedThreshold() = 0.0;
        override fun setMode( mode: IrSeekerSensor.Mode ) {}
        override fun getMode() = null;
        override fun signalDetected() = false;
        override fun getAngle() = 0.0;
        override fun getStrength() = 0.0;
        override fun getIndividualSensors() = arrayOfNulls< IrSeekerSensor.IrSeekerIndividualSensor >( 0 );
        override fun setI2cAddress( i: Int ) {}
        override fun getI2cAddress() = 0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyLightSensor = object : LightSensor
    {
        override fun getLightDetected() = 0.0;
        override fun getLightDetectedRaw() = 0;
        override fun enableLed( b: Boolean ) {}
        override fun status() = null;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyUltrasonicSensor = object : UltrasonicSensor
    {
        override fun getUltrasonicLevel() = 0.0;
        override fun status() = null;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    private val emptyVoltageSensor = object : VoltageSensor
    {
        override fun getVoltage() = 0.0;
        override fun getDeviceName() = null;
        override fun getConnectionInfo() = null;
        override fun getVersion() = 0;
        override fun close() {}
    }

    // formatter:on

    //
    // Constructors
    //

    init
    {
        dcMotorController = FalseDeviceMapping< DcMotorController >( emptyMotorController )
        dcMotor = FalseDeviceMapping( DcMotor( emptyMotorController, 0 ) )
        servoController = FalseDeviceMapping< ServoController >( emptyServoController )
        servo = FalseDeviceMapping( Servo( emptyServoController, 0 ) )
        legacyModule = FalseDeviceMapping< LegacyModule >( emptyLegacyModule )
        touchSensorMultiplexer = FalseDeviceMapping< TouchSensorMultiplexer >( emptyTouchSensorMultiplexer )
        deviceInterfaceModule = FalseDeviceMapping< DeviceInterfaceModule >( emptyDeviceInterfaceModule )
        analogInput = FalseDeviceMapping( AnalogInput( null, 0 ) )
        digitalChannel = FalseDeviceMapping( DigitalChannel( null, 0 ) )
        opticalDistanceSensor = FalseDeviceMapping< OpticalDistanceSensor >( emptyOpticalDistanceSensor )
        touchSensor = FalseDeviceMapping< TouchSensor >( emptyTouchSensor )
        pwmOutput = FalseDeviceMapping( PWMOutput( null, 0 ) )
        i2cDevice = FalseDeviceMapping( I2cDevice( null, 0 ) )
        analogOutput = FalseDeviceMapping( AnalogOutput( null, 0 ) )
        colorSensor = FalseDeviceMapping< ColorSensor >( emptyColorSensor )
        led = FalseDeviceMapping( LED( emptyDigitalChannelController, 0 ) )
        accelerationSensor = FalseDeviceMapping< AccelerationSensor >( emptyAccelerationSensor )
        compassSensor = FalseDeviceMapping< CompassSensor >( emptyCompassSensor )
        gyroSensor = FalseDeviceMapping< GyroSensor >( emptyGyroSensor )
        irSeekerSensor = FalseDeviceMapping< IrSeekerSensor >( emptyIrSeekerSensor )
        lightSensor = FalseDeviceMapping< LightSensor >( emptyLightSensor )
        ultrasonicSensor = FalseDeviceMapping< UltrasonicSensor >( emptyUltrasonicSensor )
        voltageSensor = FalseDeviceMapping< VoltageSensor >( emptyVoltageSensor )
    }

    /**
     * A false device mapping that will always return only one variable.
     *
     * @param[instance]
     *          The instance to always return for this map.
     */
    private inner class FalseDeviceMapping< T >( private val instance: T ) : HardwareMap.DeviceMapping< T >()
    {
        override fun get( deviceName: String ) = instance;
    }

}
