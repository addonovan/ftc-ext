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

import com.addonovan.ftcext.control.*
import com.addonovan.ftcext.utils.MotorAssembly
import com.addonovan.ftcext.utils.MotorType
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorController

/**
 * An abstraction on top of the [DcMotor] which makes it much easier
 * to move via encoders and distances. The [assembly] value allows
 * for the motor assembly to be specified so that movement by encoders
 * becomes a trivial task.
 *
 * A big thing to note is that motor values are no longer represented
 * on the scale [-1.0, 1.0], but now are on the interval [-100.0, 100.0],
 * because they are much easier to read that way.
 *
 * @author addonovan
 * @since 6/27/16
 */
@HardwareExtension( DcMotor::class )
class Motor( dcMotor: DcMotor ) : DcMotor( dcMotor.controller, dcMotor.portNumber, dcMotor.direction )
{

    //
    // Motor identification
    //

    /** A representation of this motor based on the DcMotorController and its port. */
    val Name = "Motor (${controller.deviceName} port $portNumber)";

    //
    // Motor Assembly
    //

    /**
     * The motor assembly that this motor is a part of. By default,
     * this represents an assembly with a tetrix motor, with a 4 inch
     * (10.16 cm) wheel, and a 1:1 gear ratio.
     */
    private var assembly: MotorAssembly = MotorAssembly( MotorType.TETRIX );

    /**
     * Sets the motor assembly that this motor is a part of. By default,
     * this represents an assembly with a tetrix motor, with a 4 inch
     * (10.16 cm) wheel, and a 1:1 gear ratio.
     *
     * @param[assembly]
     *          The new motor assembly to set for this motor.
     *
     * @return The value of this motor, for initialization purposes.
     */
    fun setAssembly( assembly: MotorAssembly ): Motor
    {
        this.assembly = assembly;

        return this;
    }

    //
    // Encoders
    //

    /**
     * Creates and registers a task with the [TaskManager] that will
     * reset the motor encoders.
     *
     * If the OpMode is linear, then whenever this method returns, the
     * task will have already been completed.
     *
     * @return The created task. (Use [Task.isFinished] to determine
     *         when the task has been finished).
     */
    fun resetEncoders(): Task
    {
        val task = object : SimpleTask()
        {

            override fun tick()
            {
                setMode( DcMotorController.RunMode.RESET_ENCODERS );
            }

            override fun isFinished() = currentPosition == 0;

        }

        TaskManager.registerTask( task, "$Name resetting encoders" );

        return task;
    }

    /**
     * Creates and registers a task with the [TaskManager] that wil
     * make the motor move at the given power until the given distance
     * has been covered.
     *
     * If the OpMode is linear, then whenever this method returns, the
     * task will have been completed.
     *
     * This is not guaranteed to be accurate, as it is entirely dependent
     * upon motor encoders!
     *
     * @param[distance]
     *          The distance to cover (in cm).
     * @param[power]
     *          The power to move the motor at.
     * @return The created task. (Use [Task.isFinished] to determine
     *         when the task has been finished).
     */
    fun moveDistance( distance: Double, power: Double ): Task
    {
        val ticks = assembly.toTicks( distance ); // precalculate the number of ticks

        // we don't need to reset the encoders if we just add the goal ticks to the current position
        val goalTicks = ticks + currentPosition;

        // create the task
        val task = object : SimpleTask()
        {

            override fun tick()
            {
                setPower( power ); // continually set the power
            }

            // we're only finished once we're in the right place
            override fun isFinished(): Boolean
            {
                return currentPosition == goalTicks;
            }

            // we reached the goal
            override fun onFinish()
            {
                brake();
            }

        };

        TaskManager.registerTask( task, "$Name running for $ticks encoder ticks" );

        return task;
    }

    //
    // Basic Movement
    //

    /**
     * Sets the power of the motor to be 0%.
     */
    fun brake()
    {
        power = 0.0;
    }

    /**
     * @param[power]
     *          The power to set the motor at [-100,100].
     */
    override fun setPower( power: Double )
    {
        super.setPower( power / 100.0 );
    }

    /**
     * @return The power, on a scale of [-100,100].
     */
    override fun getPower() = super.getPower() * 100;

}
