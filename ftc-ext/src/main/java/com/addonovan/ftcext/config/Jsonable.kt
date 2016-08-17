package com.addonovan.ftcext.config

import android.util.JsonWriter
import org.json.JSONObject

/**
 * An awful, awful name for a type of class that is able to be
 * serialized into a .json file.
 *
 * There is also an implied requirement to
 *
 * @author addonovan
 * @since 6/16/16
 */
interface Jsonable
{

    /**
     * Serializes this class into the json file via [writer].
     *
     * @param[writer]
     *          The JsonWriter to write this class to.
     */
    fun toJson( writer: JsonWriter );

}