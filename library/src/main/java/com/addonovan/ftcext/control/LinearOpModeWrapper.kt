package com.addonovan.ftcext.control

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
        if ( instance == null )
        {
            instance = opMode.newInstance();
        }
        instance?.runOpMode();
    }

}
