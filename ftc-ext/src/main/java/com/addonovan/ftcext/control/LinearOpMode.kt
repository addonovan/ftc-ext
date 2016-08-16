package com.addonovan.ftcext.control

/**
 * An OpMode that runs linearly. There is no `loop()` method
 * as in a regular [OpMode], which is executed at a semiregular
 * basis. A LinearOpMode will run exactly as it sounds, linearly.
 * Once [runOpMode] returns, the OpMode is finished, the method
 * will never be called again.
 *
 * This is most useful for Autonomous programs.
 *
 * @see AbstractOpMode
 * @see OpMode
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
    @Suppress( "unused" ) fun sleep( milliseconds: Long ) = Thread.sleep( milliseconds );

    //
    // Abstract
    //

    /**
     * Runs the OpMode.
     */
    abstract fun runOpMode();

}
