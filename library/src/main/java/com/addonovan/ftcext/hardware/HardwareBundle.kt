/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016
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

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.robocol.Telemetry

/**
 * A bundle of hardware information for an OpMode.
 * This contains the two gamepads, the telemetry object, and the current hardware map
 * for the OpMode to use.
 *
 * @author addonovan
 * @since 6/26/16
 */
data class HardwareBundle( val gamepad1: Gamepad, val gamepad2: Gamepad, val telemetry: Telemetry, val hardwareMap: HardwareMap );

/** The backing, nullable field for [CurrentHardware]. */
private var _currentHardware: HardwareBundle? = null;

/**
 * A bundle of hardware information.
 * This is ensured to be non-null, because if the backing field
 * is null, a [NullPointerException] will be thrown.
 *
 * @throws NullPointerException
 *          If the backing field hasn't been set yet.
 */
var CurrentHardware: HardwareBundle
    get()
    {
        return _currentHardware ?: throw NullPointerException( "CurrentHardware cannot be accessed before it's been set!" );
    }
    set( value )
    {
        _currentHardware = value;
    }