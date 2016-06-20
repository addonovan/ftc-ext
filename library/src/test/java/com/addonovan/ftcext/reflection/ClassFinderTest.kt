package com.addonovan.ftcext.reflection

import com.addonovan.ftcext.control.AbstractOpMode
import com.addonovan.ftcext.control.Register
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Tests the ClassFinder class to see if it will correct detect the
 * OpMode classes in the ReflectionsTestClass
 *
 * @author addonovan
 * @since 6/13/16
 */
class ClassFinderTest
{

    @Test
    fun testWith()
    {
        assertEquals( 2, finder().with( Register::class.java ).count() );
    }

    @Test
    fun testInheritsFrom()
    {
        assertEquals( 4, finder().inheritsFrom( Any::class.java ).count() );
        assertEquals( 2, finder().inheritsFrom( AbstractOpMode::class.java ).count() );
    }

    //
    // utility
    //

    fun finder() = ClassFinder();

    fun ClassFinder.count() = this.get().size;

}