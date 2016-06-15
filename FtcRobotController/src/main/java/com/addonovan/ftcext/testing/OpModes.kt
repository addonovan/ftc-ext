package com.addonovan.ftcext.testing

import com.addonovan.ftcext.control.OpMode
import com.addonovan.ftcext.control.Register
import com.qualcomm.robotcore.hardware.DcMotor

@Register( "OpMode1" )
class OpMode1 : OpMode()
{
    val motor_left = getDevice< DcMotor >( "motor_left" );
    val motor_right = getDevice< DcMotor >( "motor_right" );

    override fun initRobot(){}
    override fun loop(){}
}

@Register( "OpMode2" )
class OpMode2 : OpMode()
{
    override fun initRobot(){}
    override fun loop(){}
}

@Register( "OpMode3" )
class OpMode3 : OpMode()
{
    override fun initRobot(){}
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
    override fun initRobot(){}
    override fun loop(){}
}