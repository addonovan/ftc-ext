package com.addonovan.ftcext

import com.addonovan.ftcext.control.Register

/**
 * A small tool for extracting the information out of
 * Reflections objects that have annotations on them.
 *
 * @author addonovan
 * @since 6/12/16
 */

/**
 * @param[clazz]
 *          The class with a [Register] annotation.
 * @return The value of the [Register.name], or `null` if the annoation isn't present.
 */
fun getRegisterName( clazz: Class< * > ): String?
{
    if ( !clazz.isAnnotationPresent( Register::class.java ) ) return null;

    return clazz.getAnnotation( Register::class.java ).name;
}