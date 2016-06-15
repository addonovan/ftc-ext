package com.addonovan.ftcext.annotation

/**
 * An control that flags if a class is written in Kotlin or not.
 *
 * @author addonovan
 * @since 6/12/16
 */
@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.RUNTIME )
@MustBeDocumented
annotation class UsesKotlin;
