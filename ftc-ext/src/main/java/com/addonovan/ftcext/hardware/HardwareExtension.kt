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

import com.qualcomm.robotcore.hardware.*
import kotlin.reflect.KClass

/**
 * An annotation for hardware class that are build on top of another hardware device.
 * Valid classes for the [hardwareMapType] are as follows:
 * * [DcMotorController]
 * * [DcMotor]
 * * [ServoController]
 * * [Servo]
 * * [LegacyModule]
 * * [TouchSensorMultiplexer]
 * * [DeviceInterfaceModule]
 * * [AnalogInput]
 * * [AnalogOutput]
 * * [DigitalChannel]
 * * [LED]
 * * [OpticalDistanceSensor]
 * * [TouchSensor]
 * * [PWMOutput]
 * * [I2cDevice]
 * * [ColorSensor]
 * * [AccelerationSensor]
 * * [CompassSensor]
 * * [GyroSensor]
 * * [IrSeekerSensor]
 * * [LightSensor]
 * * [UltrasonicSensor]
 * * [VoltageSensor]
 *
 * @param[hardwareMapType]
 *          The type of [HardwareDevice] that the class is based around. See the list
 *          above for valid classes.
 *
 * @author addonovan
 * @since 6/26/16
 *
 * @see [HardwareMap]
 */
@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.RUNTIME )
@MustBeDocumented
annotation class HardwareExtension( val hardwareMapType: KClass< out HardwareDevice > );

//
// Extension functions
//

/**
 * An extension method for checking if the current class has the
 * [HardwareExtension] annotation on it.
 *
 * @return `true` if this class has the `HardwareExtension` annotation on it.
 */
fun Class< * >.isHardwareExtension() = isAnnotationPresent( HardwareExtension::class.java );

/**
 * @return The `hardwareMapType` value from the [HardwareExtension] annotation
 *         on this class.
 */
fun Class< * >.getHardwareMapType() =
        if ( !this.isHardwareExtension() ) throw IllegalArgumentException( "No @HardwareExtension annotation on class \"$simpleName\"" );
        else getAnnotation( HardwareExtension::class.java ).hardwareMapType;