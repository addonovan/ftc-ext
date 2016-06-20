package com.addonovan.ftcext.control

import android.text.Editable
import android.text.TextWatcher
import com.addonovan.ftcext.*
import com.addonovan.ftcext.config.*
import com.addonovan.ftcext.reflection.ClassFinder
import com.addonovan.ftcext.reflection.FieldFinder
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.robocol.Telemetry
import java.util.*

/**
 * The OpMode class built upon Qualcomm's. This class provides more utility
 * than the standard class does, and *must* be used in order to be able
 * to access the other functionality of this library.
 *
 * @see OpMode
 * @see LinearOpMode
 * @see Register
 *
 * @author addonovan
 * @since 6/12/16
 */
abstract class AbstractOpMode()
{

    //
    // Values
    //

    /** The name that this OpMode is registered as in the Registrar. */
    val RegisteredName by lazy()
    {
        getRegisterName( javaClass );
    }

    /** The first controller. */
    val gamepad1: Gamepad
            get() = Hardware.gamepad1;

    /** The second controller. */
    val gamepad2: Gamepad
            get() = Hardware.gamepad2;

    /** Telemetry for sending back current states and what not. */
    val telemetry: Telemetry
            get() = Hardware.telemetry;

    /** Direct access to the hardware map. Directly referencing this is discouraged. */
    val hardwareMap: HardwareMap
            get() = Hardware.hardwareMap;

    /** True if we're in `inConfig` mode, where some operations are skipped. */
    private val inConfig = System.getProperty( "ftcext.inconfig", "false" ).toBoolean();

    //
    // Constructors
    //

    init
    {
        // only do this if we aren't being created for configuration purposes
        if ( !inConfig )
        {
            // add a text change listener so that whenever
            // Qualcomm tries to change it
            OpModeLabel.addTextChangedListener( object : TextWatcher
            {
                override fun afterTextChanged( s: Editable? ){}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

                override fun onTextChanged( s: CharSequence, start: Int, before: Int, count: Int )
                {
                    val string = s.toString();

                    val name = getRegisterName( this@AbstractOpMode.javaClass );

                    // remove our listener whenever the OpMode is over
                    if ( !string.contains( name ) )
                    {
                        OpModeLabel.removeTextChangedListener( this );
                    }

                    // if it doesn't have the configuration details, add them
                    if ( !string.contains( "[" ) )
                    {
                        // run on the ui thread so we don't get yelled at
                        Activity.runOnUiThread {
                            // when this is created, update the opmode label to also show
                            // the active variant

                            val text = "Op Mode: $name [${getActiveVariant( name )}]";
                            this@AbstractOpMode.v( "Updating OpMode label to read: \"$text\"" );

                            OpModeLabel.text = text;
                        }
                    }
                }
            } );
        }
    }

    //
    // Things from Original OpMode
    //

    /**
     * Initialize the robot. This occurs directly after the OpMode
     * is selected.
     */
    abstract fun init();

    /**
     * Called the moment that the start button is pressed on the
     * Driver Station remote. This is the first code that is executed
     * when an OpMode is running.
     */
    open fun start() {};

    /**
     * Called the moment that the stop button is pressed (or the
     * timer runs our) on the Driver Station remote. This is the
     * last code that is executed when an OpMode is running.
     */
    open fun stop() {};

    //
    // Config Fetching
    //

    /** The selected configuration variant (defaults to &#91;default&$93;) */
    private var config = getActiveConfig( getRegisterName( javaClass ) );

    /**
     * Gets a `String` value from the configuration. Returns the default
     * value after adding it to the configuration if there was no
     * value in the map for the key.
     *
     * @param[name]
     *          The name of the property.
     * @param[default]
     *          The default value if the property isn't found.
     *
     * @return The value of the property in the configuration, or the default if
     *         there was none.
     */
    final fun get( name: String, default: String )  = config[ name, default ];

    /**
     * Gets a `long` value from the configuration. Returns the default
     * value after adding it to the configuration if there was no
     * value in the map for the key.
     *
     * @param[name]
     *          The name of the property.
     * @param[default]
     *          The default value if the property isn't found.
     *
     * @return The value of the property in the configuration, or the default if
     *         there was none.
     */
    final fun get( name: String, default: Long )    = config[ name, default ];

    /**
     * Gets a `double` value from the configuration. Returns the default
     * value after adding it to the configuration if there was no
     * value in the map for the key.
     *
     * @param[name]
     *          The name of the property.
     * @param[default]
     *          The default value if the property isn't found.
     *
     * @return The value of the property in the configuration, or the default if
     *         there was none.
     */
    final fun get( name: String, default: Double )  = config[ name, default ];

    /**
     * Gets a `boolean` value from the configuration. Returns the default
     * value after adding it to the configuration if there was no
     * value in the map for the key.
     *
     * @param[name]
     *          The name of the property.
     * @param[default]
     *          The default value if the property isn't found.
     *
     * @return The value of the property in the configuration, or the default if
     *         there was none.
     */
    final fun get( name: String, default: Boolean ) = config[ name, default ];

    /**
     * Gets a value from the configuration. Returns `null` if the
     * value wasn't in the configuration. The generic type can only
     * be `String`, `int`, `long`, `double`, or `boolean`, anything
     * else will cause an [IllegalArgumentException].
     *
     * @param[name]
     *          The name of the property.
     *
     * @return The value of the property in the configuration, or the `null` if
     *         there was none.
     *
     * @throws IllegalArgumentException
     *          If an unsupported type was passed via the generics.
     */
    final fun < T > get( name: String ) = config.get< T >( name );

    //
    // Device Fetching
    //

    private final fun < T > getDevice( name: String, map: HardwareMap.DeviceMapping< T > ): T
    {
        if ( inConfig )
        {
            // TODO if in config thing goes here
        }

        return map[ name ];
    }

    final fun getMotorcontroller( name: String ) = getDevice( name, hardwareMap.dcMotorController );
    final fun getMotor( name: String )           = getDevice( name, hardwareMap.dcMotor );

    final fun getServoController( name: String ) = getDevice( name, hardwareMap.servoController );
    final fun getServo( name: String )           = getDevice( name, hardwareMap.servo );

    final fun getLegacyModule( name: String )          = getDevice( name, hardwareMap.legacyModule );
    final fun getDeviceInterfaceModule( name: String ) = getDevice( name, hardwareMap.deviceInterfaceModule );

    final fun getAnalogInput( name: String )    = getDevice( name, hardwareMap.analogInput );
    final fun getAnalogOutput( name: String )   = getDevice( name, hardwareMap.analogOutput );
    final fun getDigitalChannel( name: String ) = getDevice( name, hardwareMap.digitalChannel );
    final fun getPwmOutput( name: String )      = getDevice( name, hardwareMap.pwmOutput );
    final fun getI2CDevice( name: String )      = getDevice( name, hardwareMap.i2cDevice );

    final fun getOpticalDistanceSensor( name: String ) = getDevice( name, hardwareMap.opticalDistanceSensor );
    final fun getTouchSensor( name: String )           = getDevice( name, hardwareMap.touchSensor );
    final fun getColorSensor( name: String )           = getDevice( name, hardwareMap.colorSensor );
    final fun getAccelerationSensor( name: String )    = getDevice( name, hardwareMap.accelerationSensor );
    final fun getCompassSensor( name: String )         = getDevice( name, hardwareMap.compassSensor );
    final fun getGyroSensor( name: String )            = getDevice( name, hardwareMap.gyroSensor );
    final fun getIrSensor( name: String )              = getDevice( name, hardwareMap.irSeekerSensor );
    final fun getLightSensor( name: String )           = getDevice( name, hardwareMap.lightSensor );
    final fun getUltrasonicSensor( name: String )      = getDevice( name, hardwareMap.ultrasonicSensor );
    final fun getVoltageSensor( name: String )         = getDevice( name, hardwareMap.voltageSensor );

    final fun getTouchSensorMultiplexer( name: String ) = getDevice( name, hardwareMap.touchSensorMultiplexer );
    final fun getLED( name: String )                    = getDevice( name, hardwareMap.led );
}
