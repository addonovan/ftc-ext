package com.addonovan.ftcext.control

/**
 * A standard OpMode.
 *
 * @author addonovan
 * @since 6/14/16
 */
abstract class OpMode() : AbstractOpMode()
{

    //
    // Abstract
    //

    open fun init_loop() {};
    abstract fun loop();

}
