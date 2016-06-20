package com.addonovan.ftcext.testing;

import com.addonovan.ftcext.control.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class JOpMode extends OpMode
{

    private boolean isRed = get( "red_team", false );

    private double motor_speed = get( "motor_speed", 1.0 );

    private DcMotor motor_left = motor( "motor_left" );

    // if these values appear in the default configurations, then the FalseHardwareMap works!

    private long value = get( "long_value", 5 );

    private boolean isBlue = get( "blue_team", true );

    @Override
    public void loop()
    {
        
    }

    @Override
    public void init()
    {

    }

}
