package com.addonovan.ftcext

import com.addonovan.ftcext.annotation.Hardware
import com.addonovan.ftcext.annotation.Register
import com.addonovan.ftcext.control.OpMode
import java.lang.reflect.Field

/**
 * A small tool for extracting the information out of
 * Reflections objects that have annotations on them.
 *
 * @author addonovan
 * @since 6/12/16
 */

fun getHardwareName( field: Field ) = field.getAnnotation( Hardware::class.java ).name;

fun getRegisterName( clazz: Class< * > ) = clazz.getAnnotation( Register::class.java ).name;
fun getRegisterName( opMode: OpMode ) = getRegisterName( opMode.javaClass );