package com.addonovan.ftcext.control

/**
 * An opmode that runs linearly, all at one times.
 *
 * @author addonovan
 * @since 6/15/16
 */
abstract class LinearOpMode() : AbstractOpMode()
{

    //
    // Helper
    //

    /**
     * Sleeps for the given amount of time.
     *
     * @param[milliseconds]
     *          The time (in milliseconds) to sleep for.
     */
    fun sleep( milliseconds: Long ) = Thread.sleep( milliseconds );

    //
    // Abstract
    //

    /**
     * Runs the OpMode.
     */
    abstract fun runOpMode();

}
