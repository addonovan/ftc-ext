package com.addonovan.ftcext.reflection

import java.lang.reflect.Field
import java.util.*

/**
 * Class used for finding specific fields in a class.
 *
 * @author addonovan
 * @since 6/12/16
 */
class FieldFinder private constructor()
{

    //
    // Constructors
    //

    constructor( clazz: Class< * > ) : this()
    {
        fields.addAll( clazz.fields );
    }

    /**
     * Secondary constructor for construct a FieldFinder from an object reference.
     *
     * @param[reference]
     *          The object reference.
     */
    constructor( reference: Any ) : this( reference.javaClass );

    //
    // Vals
    //

    /** The fields that haven't been filtered out. */
    private val fields: ArrayList< Field > = ArrayList();

    //
    // Getter
    //

    /**
     * @return The fields that have complied with all of the filters.
     */
    fun get(): ArrayList< Field >
    {
        val copy = ArrayList< Field >();
        copy.addAll( fields );
        return copy;
    }

    /**
     * @return A duplicate of this FieldFinder, where further filtering won't change anything.
     */
    fun duplicate(): FieldFinder
    {
        val copy = FieldFinder(); // call the empty private constructor that only we can
        copy.fields.addAll( fields );
        return copy;
    }

    //
    // Filters
    //

    /**
     * Filters out all fields that don't have the given annotation attached to them.
     *
     * @param[annotationClass]
     *          The class of the annotation to check for.
     * @return This FieldFinder so that calls may be chained.
     */
    fun with( annotationClass: Class< out Annotation > ): FieldFinder
    {
        val iter = fields.iterator();
        while ( iter.hasNext() )
        {
            val value = iter.next();
            if ( !value.isAnnotationPresent( annotationClass ) ) iter.remove();
        }

        return this;
    }

    /**
     * Filters out all fields that don't inherit from the given class.
     *
     * @param[superClass]
     *          The class that the remaining fields will inherit from.
     * @return This FieldFinder so that calls may be chained.
     */
    fun inheritsFrom( superClass: Class< * > ): FieldFinder
    {
        val iter = fields.iterator();
        while ( iter.hasNext() )
        {
            val field = iter.next();
            if ( !superClass.isAssignableFrom( field.type ) ) iter.remove();
        }

        return this;
    }

    /**
     * Filters out all fields that don't have the required modifiers. If `strict`
     * is true, then *all* of the given modifiers must be present on the field for it to
     * not be filtered out; otherwise, only one of the flags must be present in order
     * for the field to remain.
     *
     * @param[modifiers]
     *          The modifiers (from [java.lang.reflect.Modifier]) to require from the fields.
     * @param[strict]
     *          If all flags are required (`true`) or if only one is (`false`) to remain in the
     *          list (by default, all flags are required).
     * @return This FieldFinder, so that calls may be chained.
     */
    fun `is`(vararg modifiers: Int, strict: Boolean = true ): FieldFinder
    {
        var modifier = 0;
        modifiers.forEach { flag -> modifier = modifier or flag };

        val iter = fields.iterator();
        while ( iter.hasNext() )
        {
            val field = iter.next();

            if (strict) // fields most possess all of the modifiers
            {
                if ( field.modifiers and modifier != modifier ) iter.remove();
            }
            else // must have at least one flag
            {
                if ( field.modifiers and modifier == 0 ) iter.remove();
            }
        }

        return this;
    }

    /**
     * Filters out all fields that have the given modifiers. If `lenient` is true, then
     * *all* of the given modifiers must be present on the field for it to be removed;
     * otherwise, only one of the flags are required to remove the field from the list.
     *
     * @param[modifiers]
     *          The modifiers (from [java.lang.reflect.Modifier] to require from the fields.
     * @param[lenient]
     *          If all the modifiers are required (`true`) or if only one is (`false`) to remain
     *          in the list (by default, all flags are required).
     * @return This FieldFinder, so that calls may be chained.
     */
    fun isNot( vararg modifiers: Int, lenient: Boolean = true ): FieldFinder
    {
        var modifier = 0;
        modifiers.forEach { flag -> modifier = modifier or flag };

        val iter = fields.iterator();
        while ( iter.hasNext() )
        {
            val field = iter.next();

            if ( lenient ) // if all flags are required to remove the field
            {
                if ( field.modifiers and modifier == modifier ) iter.remove();
            }
            else // if only one of the flags is required to remove the field
            {
                if ( field.modifiers and modifier != 0 ) iter.remove();
            }
        }

        return this;
    }

}