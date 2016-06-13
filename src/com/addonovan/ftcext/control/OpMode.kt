package com.addonovan.ftcext.control

import com.addonovan.ftcext.annotation.UsesKotlin

/**
 * The OpMode class built upon Qualcomm's. This class provides more utility
 * than the standard class does, and _must_ be used in order to be able
 * to access the other functionality of this library.
 *
 * @author addonovan
 * @since 6/12/16
 */
abstract class OpMode : com.qualcomm.robotcore.eventloop.opmode.OpMode()
{

    //
    // Initializations
    //

    /**
     * The final override of the init method.
     * We need this in our abstract class to get basic things done that will
     * make the subclass(es) easier to use.
     */
    final override fun init()
    {
        HardwareConfigurer( this, isKotlin( javaClass ) ).configure(); // configure the hardwares in this object

        initRobot(); // perform regular initialization
    }

    //
    // Abstracts
    //

    /**
     * Initializes the robot as the `init()` method would in the standard
     * `OpMode` class.
     */
    abstract fun initRobot();

    abstract override fun loop(); // just here so it's easier to tell what the subclass needs to override

}

/**
 * Determines if the given class uses Kotlin or not.
 *
 * @param[class]
 *          The class to test for the `@UsesKotlin` control.
 * @return If `class` has the `@UsesKotlin` control on it.
 */
private fun isKotlin( `class`: Class< out OpMode > ) = `class`.isAnnotationPresent( UsesKotlin::class.java )
