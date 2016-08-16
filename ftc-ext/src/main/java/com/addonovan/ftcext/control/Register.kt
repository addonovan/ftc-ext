package com.addonovan.ftcext.control

/**
 * An annotation used to register an OpMode to be discovered
 * and used in the program.
 *
 * @author addonovan
 * @since 6/12/16
 */
@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.RUNTIME )
@MustBeDocumented
annotation class Register( val name: String );

/**
 * @param[clazz]
 *          The class with a [Register] annotation.
 * @return The value of the [Register.name].
 */
fun getRegisterName( clazz: Class< * > ): String = clazz.getAnnotation( Register::class.java ).name;