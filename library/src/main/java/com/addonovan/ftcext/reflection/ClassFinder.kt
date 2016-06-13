package com.addonovan.ftcext.reflection

import com.addonovan.ftcext.Context
import dalvik.system.DexFile
import java.lang.reflect.Modifier
import java.util.*

/**
 * Utility to find certain classes with certain attributes.
 *
 * @author austin
 * @since 6/12/16
 */
class ClassFinder()
{

    //
    // Vals
    //

    /** The classes we're left with */
    private val classes: LinkedHashSet< Class< * > > = linkedSetOf();

    //
    // Cosntructors
    //

    init
    {
        classes.addAll( baseClasses );
    }

    //
    // Actions
    //

    /**
     * @return The classes that have matched the filters.
     */
    fun get(): MutableList< Class< * > >
    {
        val list = mutableListOf< Class< * > >();
        list.addAll( classes );
        return list;
    }

    //
    // Filters
    //

    /**
     * Removes every class that does not have the annotation passed.
     *
     * @return The object so you can chain calls.
     */
    fun with( annotation: Class< out Annotation > ): ClassFinder
    {
        val iter = classes.iterator();
        while ( iter.hasNext() )
        {
            val c = iter.next();

            // if it doesn't have the annotation, remove it
            if ( !c.isAnnotationPresent( annotation ) ) iter.remove();
        }

        return this;
    }

    /**
     * Removes every class that is not a subclass of the passed class.
     *
     * @return The object so you can chain calls.
     */
    fun inheritsFrom( clazz: Class< * > ): ClassFinder
    {
        val iter = classes.iterator();
        while ( iter.hasNext() )
        {
            val c = iter.next();

            // if it isn't a subclass, remove it
            if ( !clazz.isAssignableFrom( c ) ) iter.remove();
        }

        return this;
    }

}

private val blackList: LinkedHashSet< String > =
        linkedSetOf( "com.google", "com.addonovan.ftcext", "com.qualcomm", "com.java" /* also covers javax */ );

/**
 * The base classes that every ClassFinder is based off of
 */
private val baseClasses: LinkedList< Class< * > > by lazy()
{
    val result = LinkedList< Class< * > >();
    val classNames = LinkedList< String >( Collections.list( DexFile( Context.packageCodePath ).entries() ) );

    val prohibitedModifiers = Modifier.ABSTRACT or Modifier.INTERFACE;

    // find the classes that are okay
    for ( name in classNames )
    {
        if ( isBlacklisted( name ) ) continue;

        try
        {
            val c = Class.forName( name, false, Context.classLoader );

            if ( c.modifiers and Modifier.PUBLIC == 0 ) continue; // not public
            if ( c.modifiers and prohibitedModifiers != 0 ) continue; // breaks at least one of the prohibited modifiers

            result.add( c );
        }
        catch ( e: Exception )
        {
            // then this class wasn't instantiable, so don't bother doing anything
        }
    }

    result;
}

private fun isBlacklisted( name: String ): Boolean
{
    for ( blacklisted in blackList )
    {
        if ( name.startsWith( blacklisted ) ) return true;
    }
    return false;
}
