package com.addonovan.ftcext.control

import com.addonovan.ftcext.*
import com.addonovan.ftcext.reflection.ClassFinder
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister

/**
 * The class responsible for finding all the OpModes that
 * are meant to be registered and actually register them
 * with the OpModeManager so that they may be selected.
 *
 * In order to be a valid OpMode, the class must:
 * * inherit from [OpMode] in some way
 * * be instantiable (i.e. not abstract)
 * * have the @[Register] annotation with a valid name
 *
 * Valid names for OpModes must:
 * * not consist entirely of whitespace
 * * not be "Stop Robot"
 * * contain an equals sign
 *
 * @author addonovan
 * @since 6/12/16
 */
class OpModeRegistrar() : OpModeRegister
{

    override fun register( manager: OpModeManager )
    {
        d( "Discovering OpModes" );

        // opmodes must be
        // instantiable
        // subclasses of ftcext's OpMode
        // and have the @Register annotation
        val opModeClasses = ClassFinder().inheritsFrom( OpMode::class.java ).with( Register::class.java ).get();

        for ( opMode in opModeClasses )
        {
            val name = getRegisterName( opMode );

            if ( name.isNullOrBlank() || name == "Stop Robot" || name.contains( "=" ) )
            {
                e( "$name is NOT a valid OpMode name! Change it to something that is!" );
                e( "OpMode names cannot be blank, null, \"Stop Robot\", or contain a '='" );
                continue; // skip this one
            }

            manager.register( name, opMode );
            i( "Registered OpMode class ${opMode.simpleName} as $name" );
        }
    }

}
