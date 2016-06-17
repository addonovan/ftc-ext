package com.addonovan.ftcext

import com.addonovan.ftcext.control.Register

/**
 * A small file for odds and ends that don't really belong anywhere else.
 *
 * @author addonovan
 * @since 6/12/16
 */

//
// Annotations
//

/**
 * @param[clazz]
 *          The class with a [Register] annotation.
 * @return The value of the [Register.name].
 */
fun getRegisterName( clazz: Class< * > ) = clazz.getAnnotation( Register::class.java ).name;