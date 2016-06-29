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

import com.addonovan.ftcext.*
import java.util.*

/**
 * Not to be confused with the Windows `taskmgr.exe` program,
 * the Task Manager is the asynchronous system for managing
 * specific tasks.
 *
 * This should not be used in a [LinearOpMode] yet.
 *
 * @author addonovan
 * @since 6/28/16
 */
object TaskManager
{

    //
    // Vals
    //

    /** Backing field for [IsLinearOpMode]. */
    private var _isLinearOpMode = false;

    /** If the current OpMode is a LinearOpMode */
    val IsLinearOpMode: Boolean
        get() = _isLinearOpMode;

    /** The tasks currently enqueued. */
    private val tasks = HashSet< TaskWrapper >();

    //
    // Linear OpMode handling
    //

    /**
     * @param[isLinearOpMode]
     *          If we're handling a LinearOpMode or not.
     */
    internal fun setIsLinearOpMode( isLinearOpMode: Boolean )
    {
        _isLinearOpMode = isLinearOpMode;
    }

    //
    // Registration
    //

    /**
     * Registers a task to be completed at a later time. If [IsLinearOpMode] is
     * true, then the task will be executed immediately.
     *
     * @param[task]
     *          The task to complete.
     * @param[name]
     *          A description of the task.
     */
    fun registerTask( task: Task, name: String )
    {
        if ( IsLinearOpMode )
        {
            i( "Running task: \"$name\"" );

            v( "Waiting until task can start..." );
            // sleep for 10 ms until the task can start
            while ( !task.canStart() )
            {
                Thread.sleep( 10 );
            }

            v( "Running task until completion..." );
            // continue to tick the task until it's finished
            while ( !task.isFinished() )
            {
                task.tick();
                Thread.sleep( 10 );
            }

            v( "Task completed!" );
            task.onFinish();

            i( "Completed task: \"$name\"" );
        }
        else
        {
            tasks += TaskWrapper( task, name );
        }
    }

    //
    // Execution
    //

    /**
     * Ticks all of the tasks in the task queue.
     */
    fun tick()
    {
        if ( IsLinearOpMode ) throw UnsupportedOperationException( "TaskManager.tick() isn't supported for LinearOpModes!" );

        val iter = tasks.iterator();
        while ( iter.hasNext() )
        {
            val wrapper = iter.next();

            if ( !wrapper.Task.canStart() ) continue; // skip the task

            v( "Ticking task: \"${wrapper.Name}\"" );
            wrapper.Task.tick();

            // remove it if it's finished
            if ( wrapper.Task.isFinished() )
            {
                i( "Task \"${wrapper.Name}\" finished, removing from queue" );
                iter.remove();

                wrapper.Task.onFinish(); // let the task clean up
            }
        }
    }


    private data class TaskWrapper( val Task: Task, val Name: String );

}