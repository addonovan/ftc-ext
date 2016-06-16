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
 * @return The value of the [Register.name].
 *
 * @throws NullPointerException
 *          If the class didn't have a Register annotation.
 */
fun getRegisterName( clazz: Class< * > ) = clazz.getAnnotation( Register::class.java ).name;