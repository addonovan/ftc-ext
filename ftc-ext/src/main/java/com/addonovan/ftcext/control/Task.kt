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
package com.addonovan.ftcext.control

/**
 * A task is an action that is repeatedly executed until it's finished.
 * Tasks are executed asynchronously by the [TaskManager], and two
 * tasks should not be modifying the same hardware components, as they
 * might conflict.
 *
 * @author addonovan
 * @since 6/28/16
 */
interface Task
{

    /**
     * @return If the task is ready to begin or not.
     */
    fun canStart(): Boolean;

    /**
     * 'Ticks' the task. This is executed as often as the OpMode's 'loop'
     * method is invoked, and should be written to function as if it were
     * in the loop method.
     */
    fun tick();

    /**
     * @return `true` if the task has been completed and should be removed
     *         from the tasking queue.
     */
    fun isFinished(): Boolean;

    /**
     * Called when the task is complete for clean up.
     */
    fun onFinish();

}

/**
 * A simple task is an implementation of a task that can immediately start
 * and has no necessary cleanup.
 */
abstract class SimpleTask : Task
{

    override fun canStart() = true;

    override fun onFinish() {}

}