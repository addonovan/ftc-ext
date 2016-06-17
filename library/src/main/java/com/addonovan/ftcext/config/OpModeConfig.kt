package com.addonovan.ftcext.config

import android.os.Environment
import android.util.JsonWriter
import com.addonovan.ftcext.*
import com.addonovan.ftcext.control.AbstractOpMode
import org.json.JSONObject
import java.io.*
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
    // Constructors
    //

    /**
     * Constructs an OpModeconfig with the given name and variant.
     *
     * @param[opModeName]
     *          The name of the OpMode this is a configuration for.
     * @param[variant]
     *          The name of this configuration variant.
     */
    internal constructor( opModeName: String, variant: String ) : this()
    {
        _opModeName = opModeName;
        _variant = variant;
    }

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
    //      "dataMap": [
    //          [ "$key", "$value" ],
    //          ...
    //      ]
    // }

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
        _opModeName = json.getString( "opMode" );
        _variant = json.getString( "variant" );

        val map = json.getJSONArray( "dataMap" );
        for ( i in 0..map.length() )
        {
            val mapEntry = map.getJSONArray( i );
            dataMap[ mapEntry.getString( 0 ) ] = mapEntry.getString( 1 );
        }
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

//
// Package Functions
//

/** The standard config file. */
val CONFIG_FILE = File( Context.filesDir, "OpModeConfigs.json" );

/** The map of all OpModeConfigs */
private val configMap = HashMap< Pair< String, String >, OpModeConfig >();

/**
 * Gets the configuration for the given opmode name and variant. If one is
 * not present, a new one is created.
 *
 * @param[opModeName]
 *          The name of the OpMode to get the OpModeConfig for.
 * @param[variant]
 *          The variant of configurations for the OpMode.
 *
 * @return The OpModeConfig for the given [opModeName] and [variant].
 *
 * @throws IllegalArgumentException
 *          If, for some reason, there was no entry for the given [opModeName] and [variant]
 *          in the backing map, even after one was created and added.
 */
fun getOpModeConfig( opModeName: String, variant: String = "[default]" ): OpModeConfig
{
    val key = Pair( opModeName, variant ); // the opModeName and variant pair

    if ( !configMap.containsKey( key ) )
    {
        configMap[ key ] = OpModeConfig( opModeName, variant );
    }

    return configMap[ key ] ?: throw IllegalArgumentException( "configMap has no entry for the given opModeName and variant!" );
}

/**
 * Finds all the variants of OpModeConfigs for the given OpMode.
 *
 * @param[opModeName]
 *          The name of the OpMode
 *
 * @return A list of all variants of OpModeConfigs for the given OpMode.
 */
fun OpModeConfigs( opModeName: String ): ArrayList< OpModeConfig >
{
    // make sure there's at least the [default] variant
    getOpModeConfig( opModeName );

    val list = ArrayList< OpModeConfig >();

    // find all the OpModeConfigs for this OpMode
    for ( ( key, config ) in configMap )
    {
        if ( key.first == opModeName ) list += config;
    }

    return list;
}

// configs.json:
//
// "configs": [
//     ${OpModeConfig.toJson()},
//     ...
// ]

/**
 * Loads all the OpModeConfigs from the given file.
 *
 * @param[f]
 *          The json file to load.
 */
fun loadConfigs( f: File )
{
    val json = JSONObject( f.readText() ); // create the main JSON object
    val array = json.getJSONArray( "configs" ); // the main array of all objects

    for ( i in 0..array.length() )
    {
        // load the OpModeConfig at this index
        val config = OpModeConfig();
        config.fromJson( array.getJSONObject( i ) );
        configMap[ Pair( config.OpModeName, config.Variant ) ] = config; // add it to the map

        config.e( "Loaded OpModeConfig for ${config.OpModeName} (${config.Variant} variant)" );
    }
}

/**
 * Writes all the OpModeConfigs to the given file.
 *
 * @param[f]
 *          The json file to write to.
 */
fun writeConfigs( f: File )
{
    val writer = JsonWriter( OutputStreamWriter( FileOutputStream( f ) ) ); // write for creating the json files
    writer.setIndent( "   " ); // 3 spaces, because we're satan

    writer.name( "configs" ).beginArray(); // create our array of configs

    for ( ( key, config ) in configMap )
    {
        val ( name, variant ) = key;
        config.e( "Writing OPModeConfig for $name ($variant variant)" ); // log what we're doing
        config.toJson( writer );
    }

    writer.endArray(); // end our array
}