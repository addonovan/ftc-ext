package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * A wrapper for a LinearOpMode.
 *
 * @author addonovan
 * @since 6/15/16
 */
class LinearOpModeWrapper( private val opMode: Class< out LinearOpMode > ) : LinearOpMode()
{

    private var instance: LinearOpMode? = null;

    override fun runOpMode()
    {
        Hardware = HardwareBundle( gamepad1, gamepad2, telemetry, hardwareMap );

        if ( instance == null )
        {
            instance = opMode.newInstance();
        }
        instance?.runOpMode();
    }

}
