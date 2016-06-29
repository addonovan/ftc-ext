package com.addonovan.ftcext.control

import com.addonovan.ftcext.config.*
import com.addonovan.ftcext.hardware.CurrentHardware
import com.addonovan.ftcext.hardware.HardwareBundle

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
        CurrentHardware = HardwareBundle( gamepad1, gamepad2, telemetry, hardwareMap );

        if ( instance == null )
        {
            instance = opMode.newInstance();
        }

        instance!!.init();

        writeConfigs( CONFIG_FILE ); // save the config file, theoretically there might not be another chance
        TaskManager.setIsLinearOpMode( false );
        detachRobotIconListener();
    }

    override fun init_loop() = instance!!.init_loop();

    override fun start() = instance!!.start();

    override fun loop()
    {
        instance!!.tickTasks();
        instance!!.loop();
    }

    override fun stop()
    {
        instance!!.stop();
        attachRobotIconListener(); // reattach the listener
    }


}