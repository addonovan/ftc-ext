package com.addonovan.ftcext.control

import com.addonovan.ftcext.*

/**
 * A wrapper for the OpMode class. This acts as the standard
 * OpMode for Qualcomm's poorly written systems, and allows this
 * library's better approach to take over fully.
 *
 * @see OpMode
 * @see LinearOpModeWrapper
 *
 * @author addonovan
 * @since 6/14/16
 */
class OpModeWrapper( private val opMode: Class< out OpMode > ) : com.qualcomm.robotcore.eventloop.opmode.OpMode()
{

    /** The actual OpMode that needs to be run. */
    private var instance: OpMode? = null;

    /**
     * Creates the [HardwareBundle] for the OpMode, then creates
     * the OpMode and runs it's [OpMode.init].
     */
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