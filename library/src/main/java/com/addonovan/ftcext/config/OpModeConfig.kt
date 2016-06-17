package com.addonovan.ftcext.config

import android.util.JsonWriter
import com.addonovan.ftcext.*
import org.json.JSONObject
import java.util.*

/**
 * TODO OpModeConfig documentation
 *
 * @author addonovan
 * @since 6/16/16
 */
class OpModeConfig internal constructor() : Jsonable
{

    //
    // Values
    //

    /** The backing field for [OpModeName], this can be updated, but only inside the class. */
    private var _opModeName: String = "Uh-Oh!";

    /** The name of the OpMode for which this is a configuration. */
    val OpModeName: String
        get() = _opModeName;


    /** The backing field for [Variant], this can be updated, but only inside the class. */
    private var _variant: String = "[default]";

    /** The variant of configuration for the OpMode. */
    val Variant: String
        get() = _variant;

    /** The configuration map. Everything is a string and must be converted when read. */
    private val dataMap = HashMap< String, String >();

    //
    // Actions
    //

    /**
     * Clears the configuration so that old values are ignored.
     */
    fun clear() = dataMap.clear();

    //
    // Json Serialization
    //

    // Write to the json file
    // the JSONObject for an OpModeConfig will look like this:
    // {
    //      "opMode": "$OpModeName",
    //      "variant": "$variant",
    //      "dataMap": {
    //          "$key" = "$value"
    //      }
    // },
    override fun toJson( writer: JsonWriter )
    {
        writer.beginObject(); // start OpModeConfig

        writer.name( "opMode" ).value( OpModeName );
        writer.name( "variant" ).value( Variant );

        // write the data map
        writer.name( "dataMap" ).beginObject();
        for ( ( key, value ) in dataMap )
        {
            writer.name( key ).value( value ); // log the key value pair
        }

        writer.endObject(); // end dataMap
        writer.endObject(); // end OpModeConfig
    }

    override fun fromJson( json: JSONObject )
    {
        
    }

    //
    // Operator Overloads
    //

    /**
     * The nullable variant of the other [get]s, this accepts no default parameter,
     * and thus must use type generics to determine what type of value to return.
     * Possible types for the generics are the same as they are for the other
     * get/set methods: [String], [Int], [Long], [Double], [Boolean]. Trying
     * to use any other types will result in a runtime exception.
     *
     * @return The value of key in the OpModeConfig, or `null` if there is none.
     *
     * @throws IllegalArgumentException
     *          If an unsupported type was passed via the generics.
     */
    @Suppress( "unchecked_cast" )
    operator fun < T > get( key: String ): T?
    {
        val string = dataMap[ key ] ?: return null;
        val tClass = getGenericType( ArrayList< T >() ); // this hack again (see: AbstractOpMode.getDevice< T >)

        // unchecked casts in here are checked via reflections
        when ( tClass )
        {
            // supported data types
            String::class.java -> return string as T;
            Int::class.java -> return string.toInt() as T;
            Long::class.java -> return string.toLong() as T;
            Double::class.java -> return string.toDouble() as T;
            Boolean::class.java -> return string.toBoolean() as T;

            else ->
            {
                e( "get< ${tClass.name} >( $key ):" );
                e( "  Illegal generic type: ${tClass.simpleName} (${tClass.canonicalName})" );
                e( "  Valid types are String, int, long, double, boolean" );

                throw IllegalArgumentException( "Invalid generic type: ${tClass.simpleName}!" );
            }
        }
    }

    // Non-nullable Gets
    operator fun get( key: String, default: String )  = dataMap[ key ]              ?: default;
    operator fun get( key: String, default: Int )     = dataMap[ key ]?.toInt()     ?: default;
    operator fun get( key: String, default: Long )    = dataMap[ key ]?.toLong()    ?: default;
    operator fun get( key: String, default: Double )  = dataMap[ key ]?.toDouble()  ?: default;
    operator fun get( key: String, default: Boolean ) = dataMap[ key ]?.toBoolean() ?: default;

    // Sets
    operator fun set( key: String, value: String )  { dataMap[ key ] = value;            }
    operator fun set( key: String, value: Int )     { dataMap[ key ] = value.toString(); }
    operator fun set( key: String, value: Long )    { dataMap[ key ] = value.toString(); }
    operator fun set( key: String, value: Double )  { dataMap[ key ] = value.toString(); }
    operator fun set( key: String, value: Boolean ) { dataMap[ key ] = value.toString(); }

}