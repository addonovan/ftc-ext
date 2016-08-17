/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.addonovan.ftcext

/**
 * An exception for when an annotation had an illegal value assigned to it.
 *
 * @author addonovan
 * @since 6/26/16
 */
class IllegalAnnotationValueException( annotation: Class< out Annotation >, parameterName: String, value: Any )
        : RuntimeException( "Illegal annotation value of $value for parameter $parameterName in the annotation @${annotation.simpleName}" );

/**
 * An exception for when a class that needed to be set up a certain way
 * (so it could be accessed via reflections) was not.
 *
 * @author addonovan
 * @since 6/26/16
 */
class IllegalClassSetupException( clazz: Class< * >, cause: String )
        : RuntimeException( "Class ${clazz.simpleName} set up incorrectly: $cause" );

/**
 * An exception for when something is formatted in a way that it isn't supposed to be.
 *
 * @param[cause]
 *              The cause of the problem.
 *
 * @author addonovan
 * @since 7/17/16
 */
class IllegalFormatException( cause: String ) : RuntimeException( cause );