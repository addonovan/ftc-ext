package com.addonovan.ftcext.control

import com.addonovan.ftcext.*

/**
 * A wrapper for the OpMode class. This acts as the standard
 * OpMode for Qualcomm's retarded systems, and allows this
 * library's better approach to take over fully.
 *
 * @author addonovan
 * @since 6/14/16
 */
class OpModeWrapper( private val opMode: Class< out OpMode > ) : com.qualcomm.robotcore.eventloop.opmode.OpMode()
{

    private var instance: OpMode? = null;

    override fun init()
    {
        // create the hardware bundle for the OpMode to use
        Hardware = HardwareBundle( gamepad1, gamepad2, telemetry, hardwareMap );

        if ( instance == null )
        {
            instance = opMode.newInstance();
        }
        instance?.init();
    }

    override fun init_loop() = instance?.init_loop() ?: throw NullPointerException();

    override fun start() = instance?.start() ?: throw NullPointerException();

    override fun loop() = instance?.loop() ?: throw NullPointerException();

    override fun stop() = instance?.stop() ?: throw NullPointerException();


}