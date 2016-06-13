package com.addonovan.ftcext.reflection;

import com.addonovan.ftcext.annotation.Hardware;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author addonovan
 * @since 6/12/16
 */
public class ReflectionsTestClass
{

    //
    // Fields
    //

    public final long TIME = System.currentTimeMillis();

    @Hardware
    public String greeting = "Hello";

    public String farewell = "Goodbye";

    @Hardware
    public static final ArrayList< String > phrases = new ArrayList<>();

    public double x = 0;

    @Hardware
    public double y = 0;

    @Hardware
    public double z = 0;

    public static String NAME = "name";

    private String hidden = "I should never show up in _any_ answers!";

    private transient final boolean flag = false;

}
