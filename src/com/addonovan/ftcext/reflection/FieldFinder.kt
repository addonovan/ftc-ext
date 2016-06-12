package com.addonovan.ftcext.reflection

import java.lang.reflect.Field
import java.util.*

/**
 * Class used for finding specific fields in a class.
 *
 * @author addonovan
 * @since 6/12/16
 */
class FieldFinder( private val `class`: Class< * >, private val reference: Any? = null )
{

    //
    // Constructors
    //

    /**
     * Secondary constructor for construct a FieldFinder from an object reference.
     *
     * @param[reference]
     *          The object reference.
     */
    constructor( reference: Any ) : this( reference.javaClass, reference );

    //
    // Vals
    //

    /** The fields that haven't been filtered out. */
    private val fields: ArrayList< Field > = ArrayList();

    //
    // Cosntructor
    //

    init
    {
        fields.addAll( `class`.fields ); // add all of the fields from the class to it
    }

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
        // remove all fields that don't have the annotation
        fields.forEach { field ->
            if ( !field.isAnnotationPresent( annotationClass ) ) fields.remove( field );
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
    fun inheritsFrom( superClass: Class< Any > ): FieldFinder
    {
        fields.forEach { field ->
            if ( !field.type.isAssignableFrom( superClass ) ) fields.remove( field );
        }

        return this;
    }

    /**
     * Filters out all fields that don't have the required modifiers. If `restrictive`
     * is true, then *all* of the given modifiers must be present on the field for it to
     * not be filtered out; otherwise, only one of the flags must be present in order
     * for the field to remain.
     *
     * @param[modifiers]
     *          The modifiers (from [java.lang.reflect.Modifier]) to require from the fields.
     * @param[restrictive]
     *          If all flags are required (`true`) or if only one is (`false`) to remain in the
     *          list (by default, all flags are required).
     * @return This FieldFinder, so that calls may be chained.
     */
    fun `is`( vararg modifiers: Int, restrictive: Boolean = true ): FieldFinder
    {
        var modifier = 0;
        modifiers.forEach { flag -> modifier = modifier or flag };

        if ( restrictive ) // fields most possess all of the modifiers
        {
            fields.forEach { field ->
                if ( field.modifiers and modifier != modifier ) fields.remove( field );
            };
        }
        else // must have at least one flag
        {
            fields.forEach { field ->
                if ( field.modifiers and modifier == 0 ) fields.remove( field );
            };
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

        if ( lenient ) // if all flags are required to remove the field
        {
            fields.forEach { field ->
                if ( field.modifiers and modifier == modifier ) fields.remove( field );
            };
        }
        else
        {
            fields.forEach { field ->
                if ( field.modifiers and modifier != 0 ) fields.remove( field );
            };
        }

        return this;
    }

}