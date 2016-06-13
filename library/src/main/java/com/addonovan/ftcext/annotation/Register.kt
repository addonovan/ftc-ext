package com.addonovan.ftcext.annotation

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
annotation class Register( val name: String )
