package com.addonovan.ftcext.control

import android.content.Intent
import android.widget.*
import com.addonovan.ftcext.*
import com.addonovan.ftcext.config.ConfigActivity
import com.addonovan.ftcext.reflection.ClassFinder
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister

/**
 * The class responsible for finding all the OpModes that
 * are meant to be registered and actually register them
 * with the OpModeManager so that they may be selected.
 *
 * In order to be a valid OpMode, the class must:
 * * inherit from [AbstractOpMode] in some way
 * * be instantiable (i.e. not abstract)
 * * have the @[Register] annotation with a valid name
 *
 * Valid names for OpModes must:
 * * not consist entirely of whitespace
 * * not be "Stop Robot"
 * * contain an equals sign
 *
 * @see OpModeWrapper
 * @see LinearOpModeWrapper
 *
 * @author addonovan
 * @since 6/12/16
 */
class OpModeRegistrar() : OpModeRegister
{

    // None of this stuff really belongs here, it's just that this is
    // the entry point and only bridge between the Robot Controller
    // activity and the library
    init
    {

        i( "Attaching long-press listener to robot icon" );

        // now there is absolutely no visible trace of this program in case it
        // technically violates rules (which I'm sure it doesn't, but, hey, I'm not
        // actually a team member anymore)
        RobotIcon.setOnLongClickListener { view ->

            val intent = Intent( Activity, ConfigActivity::class.java );
            Activity.startActivity( intent );

            true; // long press handled? I guess, idk what this is used for
        };
    }

    /**
     * Searches for all instantiable classes which inherit from [AbstractOpMode]
     * and have the [Register] annotation and will then verify the name of the
     * prospective OpMode and then create the correct OpModeWrapper for the
     * class and register it with Qualcomm's [OpModeManager].
     *
     * @see AbstractOpMode
     * @see OpMode
     * @see LinearOpMode
     * @see OpModeWrapper
     * @see LinearOpModeWrapper
     */
    @Suppress( "unchecked_cast" ) // the cast is check via reflections
    override fun register( manager: OpModeManager )
    {
        i( "Discovering OpModes" );

        // OpModes must be
        // instantiable
        // subclasses of ftcext's OpMode
        // and have the @Register annotation
        val opModeClasses = ClassFinder().inheritsFrom( AbstractOpMode::class.java ).with( Register::class.java ).get();

        i( "Discovered ${opModeClasses.size} correctly formed OpMode classes!" );

        for ( opMode in opModeClasses )
        {
            val name = getRegisterName( opMode );

            if ( name.isBlank() || name == "Stop Robot" || name.contains( "=" ) )
            {
                e( "$name is NOT a valid OpMode name! Change it to something that is!" );
                e( "OpMode names cannot be blank, null, \"Stop Robot\", or contain a '='" );
                continue; // skip this one
            }

            // if it's a regular OpMode
            if ( OpMode::class.java.isAssignableFrom( opMode ) )
            {
                manager.register( name, OpModeWrapper( opMode as Class< out OpMode > ) );
                i( "Registered OpMode class ${opMode.simpleName} as $name" );
            }
            // if it's a LinearOpMode
            else if ( LinearOpMode::class.java.isAssignableFrom( opMode ) )
            {
                manager.register( name, LinearOpModeWrapper( opMode as Class< out LinearOpMode > ) );
                i( "Registered LinearOpMode class ${opMode.simpleName} as $name" );
            }
            // an unknown subclass of AbstractOpMode
            else
            {
                e( "Tried to register an unknown type of OpMode (class: ${opMode.name}) as $name" );
                e( "OpModes must inherit from either com.addonovan.ftcext.control.OpMode or com.addonovan.ftcext.control.LinearOpMode!" );
            }
        }
    }

}

private var iconId = -1;