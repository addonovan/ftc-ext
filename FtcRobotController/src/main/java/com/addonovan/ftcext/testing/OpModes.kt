package com.addonovan.ftcext.testing

import com.addonovan.ftcext.control.*
import com.qualcomm.robotcore.hardware.DcMotor

@Register( "OpMode1" )
class OpMode1 : OpMode()
{

    val motorSpeed = get( "motor_speed", 1.0 );
    val timeLength = get( "time_length", 1 );
    val redTeam = get( "red_team", false );

    val motor_back = getDevice< DcMotor >( "motor_back" );
    val motor_front: DcMotor = getDevice( "motor_front" );

    val motor_left = motor( "motor_left" );
    val motor_right = motor( "motor_right" );

    val blueTeam = get( "blue_team", true );
    val stuff = get( "stuff", 100000 );

    override fun init(){}
    override fun loop(){}
}

@Register( "OpMode2" )
class OpMode2 : OpMode()
{
    val motorSpeed = get( "motor_speed", 0.5 );
    val duration = get( "duration", 500000 );

    override fun init(){}
    override fun loop(){}
}

@Register( "OpMode3" )
class OpMode3 : OpMode()
{
    override fun init(){}
    override fun loop(){}
}

//
// Invisible Ones
//

@Register( "IOM 1" )
abstract class OpMode4 : OpMode();

@Register( "IOM 2" )
private class OpMode5 : OpMode()
{
    override fun init(){}
    override fun loop(){}
}