package com.addonovan.ftcext.control

/**
 * A standard OpMode, in which the [loop] method will
 * be called at "regular" intervals (aka whenever the
 * Qualcomm application feels like it) to allow the
 * OpMode to update robot hardware values.
 *
 * This approach is useful for TeleOps; however, can be
 * fitted to be used by Autonomous programs by using an
 * enum for the current state and building an array of
 * the states and `switch`ing on the current state and
 * executing different methods based on the value, each
 * with their own ending condition, that, upon satisfaction,
 * will increment the current position in the array.
 *
 * Example:
 * ```
 * enum RunState{ FORWARD, TURN_LEFT, BACKWARD };
 *
 * RunState[] program = { FORWARD, TURN_LEFT, TURN_LEFT, BACKWARD,
 *                        TURN_LEFT, FORWARD };
 * int currentState = 0;
 *
 * ...
 *
 * @Override
 * public void loop()
 * {
 *      if ( currentState == program.length ) return; // we're finished with our program
 *      switch( currentState )
 *      {
 *          case FORWARD:
 *                  // code to make motors move backwards
 *                  if ( goneForward ) currentState++;
 *                  break;
 *          case TURN_LEFT:
 *                  // code to make motors rotate left
 *                  if ( turnedLeft ) currentState++;
 *                  break;
 *          case BACKWARD:
 *                  // code to make motors move backwards
 *                  if ( goneBackward ) currentState++;
 *                  break;
 *      }
 * }
 * ```
 *
 *
 * @see AbstractOpMode
 * @see LinearOpMode
 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode
 *
 * @author addonovan
 * @since 6/14/16
 */
abstract class OpMode : AbstractOpMode()
{

    //
    // Abstract
    //

    open fun init_loop() {};
    abstract fun loop();

}
