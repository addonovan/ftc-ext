package com.addonovan.ftcext

import com.addonovan.ftcext.control.AbstractOpMode
import com.addonovan.ftcext.control.Register

/**
 * A small tool for extracting the information out of
 * Reflections objects that have annotations on them.
 *
 * @author addonovan
 * @since 6/12/16
 */

fun getRegisterName( clazz: Class< * > ) = clazz.getAnnotation( Register::class.java ).name;
fun getRegisterName( opMode: AbstractOpMode) = getRegisterName( opMode.javaClass );