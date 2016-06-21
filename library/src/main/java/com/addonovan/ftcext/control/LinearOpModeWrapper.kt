package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.config.CONFIG_FILE
import com.addonovan.ftcext.config.writeConfigs

/**
 * A wrapper for a LinearOpMode.
 *
 * @see OpModeWrapper
 * @see LinearOpMode
 *
 * @author addonovan
 * @since 6/15/16
 */
class LinearOpModeWrapper( private val opMode: Class< out LinearOpMode > ) : com.qualcomm.robotcore.eventloop.opmode.LinearOpMode()
{

    /** The actual OpMode that this is a wrapper for. */
    private var instance: LinearOpMode? = null;

    override fun runOpMode()
    {
        Hardware = HardwareBundle( gamepad1, gamepad2, telemetry, hardwareMap );

        if ( instance == null )
        {
            instance = opMode.newInstance();
        }

        writeConfigs( CONFIG_FILE ); // write the config, theoretically there might not be another chance
        instance!!.runOpMode();
    }

}
