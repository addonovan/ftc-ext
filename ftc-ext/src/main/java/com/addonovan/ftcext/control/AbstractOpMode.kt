package com.addonovan.ftcext.control

import android.text.Editable
import android.text.TextWatcher
import com.addonovan.ftcext.*
import com.addonovan.ftcext.config.*
import com.addonovan.ftcext.hardware.CurrentHardware
import com.addonovan.ftcext.hardware.HardwareExtension;
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.robocol.Telemetry

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
            get() = CurrentHardware.gamepad1;

    /** The second controller. */
    val gamepad2: Gamepad
            get() = CurrentHardware.gamepad2;

    /** Telemetry for sending back current states and what not. */
    val telemetry: Telemetry
            get() = CurrentHardware.telemetry;

    /** Direct access to the hardware map. Directly referencing this is discouraged. */
    val hardwareMap: HardwareMap
            get() = CurrentHardware.hardwareMap;

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

    /** The selected configuration variant (defaults to default) */
    private val config = getActiveConfig( RegisteredName );

    // unless these methods are expanded, apparently they don't exist according to the JVM
    // this bullshit randomly fucking happened and took me over 2 hours to fix
    // seriously, fuck you, JVM/ART whatever the hell is running this bullshit

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
    fun get( name: String, default: String ): String
    {
        return config[ name, default ];
    }

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
    fun get( name: String, default: Long ): Long
    {
        return config[ name, default ];
    }

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
    fun get( name: String, default: Double ): Double
    {
        return config[ name, default ];
    };

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
    fun get( name: String, default: Boolean ): Boolean
    {
        return config[ name, default ];
    }

    //
    // Device Fetching
    //

    /**
     * A single method approach to getting a device, this requires a type parameter
     * on the method, however, which means this cannot be correctly used if referenced
     * in java.
     *
     * @param[name]
     *          The name of the device.
     *
     * @return The value in the `DeviceMapping` in the hardware map for the given type,
     *         [T], with the key [name].
     *
     * @throws IllegalArgumentException
     *          If [T] had no DeviceMapping associated with it.
     * @throws NullPointerException
     *          If there was no entry for [name] with the type [T].
     * @throws IllegalAnnotationValueException
     *          If the [T] class had a [HardwareExtension] annotation on it that had
     *          an invalid value (that is, one that doesn't have a value in the backing
     *          map of classes and `DeviceMapping`s) assigned to the `hardwareMapType`
     *          parameter.
     *          This should *never* happen to an end-user, that would mean that the
     *          developer who wrote the extension did so wrongly.
     * @throws IllegalClassSetupException
     *          If [T] is a [HardwareExtension] that has been insufficiently
     *          set up, which could be for a multitude of reasons, check the
     *          error message if this occurs for more details.
     */
    inline fun < reified T : HardwareDevice > getDevice( name: String ) = hardwareMap.getDeviceByType( T::class.java, name ) as T;

    /**
     * **Java Only**
     *
     * A single method approach to getting a device, this requires the type to be
     * explicitly given; however, it returns a generic [HardwareDevice] object which
     * must be casted down before use.
     *
     * @param[name]
     *          The name of the device to fetch.
     * @param[type]
     *          The class of the hardware device, this doesn't necessary have to be one
     *          of the types directly associated in the hardware map.
     *
     * @return The value in the `DeviceMapping` in the hardware map for the given type,
     *         [type], with the key [name].
     *
     * @throws IllegalArgumentException
     *          If [type] had no DeviceMapping associated with it.
     * @throws NullPointerException
     *          If there was no entry for [name] with the type [type].
     * @throws IllegalAnnotationValueException
     *          If the [type] class had a [HardwareExtension] annotation on it that had
     *          an invalid value (that is, one that doesn't have a value in the backing
     *          map of classes and `DeviceMapping`s) assigned to the `hardwareMapType`
     *          parameter.
     *          This should *never* happen to an end-user, that would mean that the
     *          developer who wrote the extension did so wrongly.
     * @throws IllegalClassSetupException
     *          If [type] is a [HardwareExtension] that has been insufficiently
     *          set up, which could be for a multitude of reasons, check the
     *          error message if this occurs for more details.
     */
    fun getDevice( name: String, type: Class< out HardwareDevice > ) = hardwareMap.getDeviceByType( type, name );

    //
    // Java pleb-stuff
    //

    @Suppress( "unused" ) fun motorController( name: String ) = hardwareMap.dcMotorController[ name ];
    @Suppress( "unused" ) fun motor( name: String ) = hardwareMap.dcMotor[ name ];

    @Suppress( "unused" ) fun servoController( name: String ) = hardwareMap.servoController[ name ];
    @Suppress( "unused" ) fun servo( name: String ) = hardwareMap.servo[ name ];

    @Suppress( "unused" ) fun legacyModule( name: String ) = hardwareMap.legacyModule[ name ];
    @Suppress( "unused" ) fun deviceInterfaceModule( name: String ) = hardwareMap.deviceInterfaceModule[ name ];

    @Suppress( "unused" ) fun analogIn( name: String ) = hardwareMap.analogInput[ name ];
    @Suppress( "unused" ) fun analogOut( name: String ) = hardwareMap.analogOutput[ name ];
    @Suppress( "unused" ) fun digitalChannel( name: String ) = hardwareMap.digitalChannel[ name ];
    @Suppress( "unused" ) fun pwmOut( name: String ) = hardwareMap.pwmOutput[ name ];
    @Suppress( "unused" ) fun i2cDevice( name: String ) = hardwareMap.i2cDevice[ name ];

    @Suppress( "unused" ) fun opticalDistanceSensor( name: String ) = hardwareMap.opticalDistanceSensor[ name ];
    @Suppress( "unused" ) fun touchSensor( name: String ) = hardwareMap.touchSensor[ name ];
    @Suppress( "unused" ) fun colorSensor( name: String ) = hardwareMap.colorSensor[ name ];
    @Suppress( "unused" ) fun accelerationSensor( name: String ) = hardwareMap.accelerationSensor[ name ];
    @Suppress( "unused" ) fun compassSensor( name: String ) = hardwareMap.compassSensor[ name ];
    @Suppress( "unused" ) fun gyroSensor( name: String ) = hardwareMap.gyroSensor[ name ];
    @Suppress( "unused" ) fun irSensor( name: String ) = hardwareMap.irSeekerSensor[ name ];
    @Suppress( "unused" ) fun lightSensor( name: String ) = hardwareMap.lightSensor[ name ];
    @Suppress( "unused" ) fun ultrasonicSensor( name: String ) = hardwareMap.ultrasonicSensor[ name ];
    @Suppress( "unused" ) fun voltageSensor( name: String ) = hardwareMap.voltageSensor[ name ];

    @Suppress( "unused" ) fun touchSensorMultiplexer( name: String ) = hardwareMap.touchSensorMultiplexer[ name ];
    @Suppress( "unused" ) fun led( name: String ) = hardwareMap.led[ name ];

}