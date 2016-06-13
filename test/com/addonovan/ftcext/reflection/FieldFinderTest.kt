package com.addonovan.ftcext.reflection

import com.addonovan.ftcext.annotation.Hardware
import org.jetbrains.annotations.NotNull
import org.junit.Test
import java.lang.String.format

import org.junit.Assert.*
import java.lang.reflect.Modifier
import java.util.*

/**
 * @author addonovan
 * *
 * @since 6/12/16
 */
class FieldFinderTest
{

    @Test
    fun testWith()
    {
        //
        // Test for @Hardware
        //
        val hardwares = arrayListOf( "greeting", "phrases", "y", "z" );
        val foundHardwares = finder().with( Hardware::class.java ).get();

        foundHardwares.forEach { field ->
            assertTrue( "Field (${field.name}) is not marked with @Hardware", hardwares.contains( field.name ) );
            hardwares.remove( field.name );
        }

        assertTrue(
                "@Hardwared'd fields that were not found: ${hardwares.toString().replace( "(\\[|\\])".toRegex(), "" )}",
                0 == hardwares.size
        );
    }

    @Test
    fun testInheritsFrom()
    {
        assertEquals( 3, finder().inheritsFrom( String::class.java ).get().size );
        assertEquals( 0, finder().inheritsFrom( Int::class.java ).get().size );
        assertEquals( 3, finder().inheritsFrom( Double::class.java ).get().size );
        assertEquals( 1, finder().inheritsFrom( Long::class.java ).get().size );
        assertEquals( 1, finder().inheritsFrom( List::class.java ).get().size );
        assertEquals( 1, finder().inheritsFrom( ArrayList::class.java ).get().size );
        assertEquals( 4, finder().inheritsFrom( Any::class.java ).get().size ); // primitives in java are not subclasses of Object
    }

    @Test
    fun testIs()
    {
        assertEquals( 8, finder().`is`( Modifier.PUBLIC ).get().size ); // this shouldn't do anything to the list
        assertEquals( 0, finder().`is`( Modifier.PRIVATE ).get().size );
        assertEquals( 2, finder().`is`( Modifier.FINAL ).get().size );
        assertEquals( 1, finder().`is`( Modifier.FINAL, Modifier.STATIC ).get().size );
        assertEquals( 3, finder().`is`( Modifier.FINAL, Modifier.STATIC, strict = false ).get().size );
    }

    @Test
    fun testIsNot()
    {
        assertEquals( 8, finder().isNot( Modifier.PRIVATE ).get().size ); // this shouldn't do anything to the list
        assertEquals( 6, finder().isNot( Modifier.FINAL ).get().size );
        assertEquals( 6, finder().isNot( Modifier.STATIC ).get().size );
        assertEquals( 7, finder().isNot( Modifier.STATIC, Modifier.FINAL ).get().size );
        assertEquals( 5, finder().isNot( Modifier.STATIC, Modifier.FINAL, lenient = false ).get().size );
    }

    private fun finder() = FieldFinder( ReflectionsTestClass() );

}