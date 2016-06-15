package com.addonovan.ftcext.reflection;

import com.addonovan.ftcext.control.OpMode;
import com.addonovan.ftcext.control.Register;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author addonovan
 * @since 6/12/16
 */
@Register( name = "Test#1" )
public class ReflectionsTestClass extends OpMode
{

    //
    // Fields
    //

    public final long TIME = System.currentTimeMillis();

    public String greeting = "Hello";

    public String farewell = "Goodbye";

    public static final ArrayList< String > phrases = new ArrayList<>();

    public double x = 0;

    public double y = 0;

    public double z = 0;

    public static String NAME = "name";

    private String hidden = "I should never show up in _any_ answers!";

    private transient final boolean flag = false;

    @Override public void init() {}
    @Override public void loop() {}

    @Register( name = "Test#2" )
    public class TestClass2 extends OpMode
    {
        @Override public void init() {}
        @Override public void loop() {}
    }

}
