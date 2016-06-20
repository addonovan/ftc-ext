package com.addonovan.ftcext.reflection

import com.addonovan.ftcext.*
import dalvik.system.DexFile
import java.lang.reflect.Modifier
import java.util.*

/**
 * Utility to find certain classes with certain attributes.
 *
 * @see FieldFinder
 * @see Class
 *
 * @author addonovan
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
    // Constructors
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
            if ( !c.isAnnotationPresent( annotation ) )
            {
                iter.remove();
            }
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
            if ( !clazz.isAssignableFrom( c ) )
            {
                iter.remove();
            }
        }

        return this;
    }

}

/** A list of packages that are blacklisted to save time when loading the baseClasses */
private val blackList: LinkedHashSet< String > =
        linkedSetOf( "com.google", "com.android", "dalvik", "android", // android packages
                     "java", "kotlin",                                 // language packages
                     "com.ftdi" );                                     // FTC packages

/**
 * The base classes that every ClassFinder is based off of
 */
private val baseClasses: LinkedList< Class< * > > by lazy()
{
    // all the classes that fit our criteria
    val result = LinkedList< Class< * > >();

    // all the classes in this dex file
    val classNames = Collections.list( DexFile( Context.packageCodePath ).entries() );

    // modifiers that we won't allow
    val prohibitedModifiers = Modifier.ABSTRACT or Modifier.INTERFACE;

    // find the classes that are okay
    for ( name in classNames )
    {
        if ( isBlacklisted( name ) ) continue; // skip classes that are in blacklisted packages

        try
        {
            val c = Class.forName( name, false, Context.classLoader );

            if ( c.modifiers and Modifier.PUBLIC == 0         // not public
              || c.modifiers and prohibitedModifiers != 0 )   // has a prohibited modifier
            {
                continue;
            }

            result.add( c );
        }
        catch ( e: Throwable ) // now catches everything!
        {
            // then this class wasn't instantiable, so don't bother doing anything
        }
    }

    result;
}

/**
 * @param[name]
 *          The full name of the class (package included).
 * @return If the class name is in a blacklisted package.
 */
private fun isBlacklisted( name: String ): Boolean
{
    if ( name.contains( "$" ) ) return true;

    for ( blacklisted in blackList )
    {
        if ( name.startsWith( blacklisted ) ) return true;
    }
    return false;
}
