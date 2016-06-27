/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Austin Donovan (addonovan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.addonovan.ftcext.hardware

import com.qualcomm.robotcore.hardware.Servo
import kotlin.concurrent.thread

/**
 * A blend between the [ContinuousServo] and the [ToggleServo]. This
 * inherits from a ContinuousServo, rather than a ToggleServo, because
 * this functions far differently than a normal ToggleServo does.
 *
 * How this functions is a little difficult to understand from the
 * code; however, it is actually quite simple. When a servo is
 * toggled, it will switch to the correct state, and, after
 * [ToggleRunTime] milliseconds, it will stop and be in the end state.
 * For example, if a servo were toggled on, it would run in
 * the direction given by [EngagedPosition] under the status of "Engaging"
 * for [ToggleRunTime] milliseconds, then stop under the status of
 * "Engaged". When it is next toggled, it will run for [ToggleRunTime]
 * milliseconds in the direction given by [DisengagedPosition] under
 * the status of "Disengaging", then it will stop and have the status
 * of "Disengaged" until it's toggled next.
 *
 * @param[servo]
 *          The servo to create a ContinuousToggleServo from.
 *
 * @author addonovan
 * @since 6/26/16
 *
 * @see [ToggleServo]
 * @see [ContinuousServo]
 */
class ContinuousToggleServo( servo: Servo ) : ContinuousServo( servo )
{

    //
    // States
    //

    /**
     * The position of a [ContinuousToggleServo].
     *
     * @param[StringValue]
     *          The value of the position as a string.
     */
    enum class Position( val StringValue: String )
    {

        /** The servo is in its engaged state. */
        ENGAGED( "Engaged" ),

        /** The servo is moving in its engaged direction. */
        ENGAGING( "Engaging" ),

        /** The servo is in its disengaged state. */
        DISENGAGED( "Disengaged" ),

        /** The servo is moving in its disengaged direction. */
        DISENGAGING( "Disengaging" );

    }

    //
    // Timing
    //

    /**
     * The amount of time (in milliseconds) to ensure between [toggle] calls go
     * through and toggle the position, by default this is 350.
     */
    var ToggleDelay: Long = 350;

    /**
     * The amount of time (in milliseconds) to be running the either direction,
     * by default this is 350.
     */
    var ToggleRunTime: Long = 350;

    /**
     * The last time the [toggle] call went through.
     */
    private var lastToggle: Long = 0;

    //
    // Current Positioning
    //

    /** The backing field for [CurrentPosition]. */
    @Volatile
    private var _currentPosition = Position.ENGAGED;

    /**
     * The current position of the ToggleServo.
     */
    val CurrentPosition: Position
        get() = _currentPosition;

    //
    // Positions
    //

    /**
     * The engaged position of the Toggle Servo, [toggleOn] will set
     * the servo to this position. This value must be between [0.0, 1.0].
     */
    var EngagedPosition: Double = 0.0;
        get() = field;

        /**
         * @param[value]
         *          The new value, must be on [0, 1].
         */
        set( value )
        {
            if ( value < 0 ) throw IllegalArgumentException( "EngagedPosition cannot be less than 0!" );
            if ( value > 1 ) throw IllegalArgumentException( "EngagedPosition cannot be greater than 1!" );
            field = value;
        }

    /**
     * The disengaged position of the Toggle Servo, [toggleOff] will set
     * the servo to this position. This value must be between [0.0, 1.0].
     */
    var DisengagedPosition: Double = 0.0;
        get() = field;

        /**
         * @param[value]
         *          The new value, must be on [0, 1].
         */
        set( value )
        {
            if ( value < 0 ) throw IllegalArgumentException( "DisengagedPosition cannot be less than 0!" );
            if ( value > 1 ) throw IllegalArgumentException( "EngagedPosition cannot be greater than 1!" );
            field = value;
        }

    //
    // Toggling
    //

    /**
     * The number of toggle threads we have running. This is incremented when a
     * toggling thread starts and decremented when it exits.
     *
     * @see [dispatchUpdateThread]
     */
    @Volatile private var toggleThreadCount = 0;

    /**
     * Toggles the servo to the opposite state than it's currently in.
     */
    fun toggle()
    {
        // ensure that at least [ToggleDelay] milliseconds have passed since
        // this method was last invoked.
        if ( System.currentTimeMillis() - lastToggle < ToggleDelay ) return;

        // toggle the position
        when ( _currentPosition )
        {
            Position.ENGAGED, Position.ENGAGING       -> toggleOff();

            Position.DISENGAGED, Position.DISENGAGING -> toggleOn();
        }

        lastToggle = System.currentTimeMillis(); // reset the timer
    }

    /**
     * Sets the servo's position to the [EngagedPosition]
     */
    fun toggleOn()
    {
        position = EngagedPosition;
        _currentPosition = Position.ENGAGING;

        dispatchUpdateThread();
    }

    /**
     * Sets the servo's position to the [DisengagedPosition].
     */
    fun toggleOff()
    {
        position = DisengagedPosition;
        _currentPosition = Position.DISENGAGING;

        dispatchUpdateThread();
    }

    /**
     * Dispatches the background thread that will stop the servo after [ToggleRunTime]
     * milliseconds have passed, provided no other toggling has occurred.
     */
    private fun dispatchUpdateThread()
    {
        thread {

            toggleThreadCount++; // we've just started our thread, after all

            Thread.sleep( ToggleRunTime ); // wait for the specified time

            // if we're the only running thread, then no other toggling has been going on!
            if ( toggleThreadCount == 1 )
            {
                stop(); // stop all movement

                // update the position
                if ( _currentPosition == Position.ENGAGING ) _currentPosition = Position.ENGAGED;
                if ( _currentPosition == Position.DISENGAGING ) _currentPosition = Position.DISENGAGING;
            }

            toggleThreadCount--; // we're now ended

        }.start();
    }

}
