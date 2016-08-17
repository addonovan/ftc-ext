package com.addonovan.ftcext.testing;

import com.addonovan.ftcext.control.Devices;
import com.addonovan.ftcext.control.OpMode;
import com.addonovan.ftcext.control.Register;
import com.addonovan.ftcext.hardware.Motor;
import com.addonovan.ftcext.hardware.ToggleServo;
import com.addonovan.ftcext.utils.MotorAssembly;
import com.addonovan.ftcext.utils.MotorType;
import com.qualcomm.robotcore.hardware.DcMotor;

@Register( name = "JOpMode" )
public class JOpMode extends OpMode
{

    private boolean isRed = get( "red_team", false );

    private double motor_speed = get( "motor_speed", 1.0 );

    private DcMotor motor_left = motor( "motor_left" );

    // if these values appear in the default configurations, then the FalseHardwareMap works!

    private long value = get( "long_value", 5 );

    private boolean isBlue = get( "blue_team", true );

    private Motor motor = Devices.< Motor >get( "motor" ).setAssembly( new MotorAssembly( MotorType.TETRIX, 10.16, 1 ) );

    private ToggleServo servo = Devices.get( "toggle" );

    @Override
    public void init()
    {

    }

    @Override
    public void loop()
    {

    }

}
