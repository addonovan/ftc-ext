package com.addonovan.ftcext.annotation

/**
 * An annotation placed on a property or field that represents
 * a piece of hardware that's configured with a name in the
 * hardware map.
 *
 * @author addonovan
 * @since 6/12/16
 */
@Target( AnnotationTarget.PROPERTY, AnnotationTarget.FIELD )
@Retention( AnnotationRetention.RUNTIME )
@MustBeDocumented
annotation class Hardware( val name: String = "" )