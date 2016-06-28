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

import com.addonovan.ftcext.utils.MotorAssembly
import com.addonovan.ftcext.utils.MotorType
import com.qualcomm.robotcore.hardware.DcMotor

/**
 * An abstraction on top of the [DcMotor] which makes it much easier
 * to move via encoders and distances. The [Assembly] value allows
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
    // Motor Assembly
    //

    /**
     * The motor assembly that this motor is a part of. By default,
     * this represents an assembly with a tetrix motor, with a 4 inch
     * (10.16 cm) wheel, and a 1:1 gear ratio.
     */
    var Assembly: MotorAssembly = MotorAssembly( MotorType.TETRIX );

    /**
     * @param[assembly]
     *          The new motor assembly to set for this motor.
     *
     * @return The value of this motor, for initialization purposes.
     */
    fun setAssembly( assembly: MotorAssembly ): Motor
    {
        Assembly = assembly;
        return this;
    }

    //
    // Movement
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
