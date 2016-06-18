package com.addonovan.ftcext.config

import android.util.JsonWriter
import android.util.Log
import com.addonovan.ftcext.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*

/**
 * TODO OpModeConfig documentation
 *
 * @author addonovan
 * @since 6/16/16
 */
class OpModeConfig internal constructor( name: String ) : Jsonable
{

    //
    // Values
    //

    /** The name of the OpMode for which this is a configuration. */
    val OpModeName: String = name;


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
    internal constructor( opModeName: String, variant: String ) : this( opModeName )
    {
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
    //      "variant": "$variant",
    //      "dataMap": [
    //          [ "$key", "$value" ],
    //          ...
    //      ]
    // }

    override fun toJson( writer: JsonWriter )
    {
        writer.beginObject(); // start OpModeConfig

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
            String::class.java  -> return string             as T;
            Int::class.java     -> return string.toInt()     as T;
            Long::class.java    -> return string.toLong()    as T;
            Double::class.java  -> return string.toDouble()  as T;
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

/** The map of the opmode names vs. their active variants. */
private val activeVariants = HashMap< String, String >();

/**
 * @return The names of all the OpModes with configurations.
 */
fun getOpModeNames() = activeVariants.keys;

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
 */
fun getOpModeConfig( opModeName: String, variant: String = "default" ): OpModeConfig
{
    val key = Pair( opModeName, variant ); // the opModeName and variant pair

    if ( !configMap.containsKey( key ) )
    {
        configMap[ key ] = OpModeConfig( opModeName, variant );
    }

    return configMap[ key ]!!;
}

/**
 * Finds the active variant for the given opmode name. If there
 * was no active variant, then default is used.
 *
 * @param[opModeName]
 *          The name of the opmode to fetch the config for.
 * @return The active variant of the given opmode.
 */
fun getActiveConfig( opModeName: String ): OpModeConfig
{
    return getOpModeConfig( opModeName, getActiveVariant( opModeName ) );
}

/**
 * Finds the active variant name. If there was none, then `default`
 * is created and added.
 *
 * @param[opModeName]
 *          The name of the opmode to fetch the config for.
 * @return The active variant name of the given opmode.
 */
fun getActiveVariant( opModeName: String ): String
{
    if ( !activeVariants.containsKey( opModeName ) )
    {
        activeVariants[ opModeName ] = "default";
    }

    return activeVariants[ opModeName ]!!;
}

/**
 * Finds all the variants of OpModeConfigs for the given OpMode.
 *
 * @param[opModeName]
 *          The name of the OpMode
 *
 * @return A list of all variants of OpModeConfigs for the given OpMode.
 */
fun getOpModeConfigs( opModeName: String ): ArrayList< OpModeConfig >
{
    val list = ArrayList< OpModeConfig >();

    // make sure there's at least the [default] variant
    // also makes sure that [default] is the first choice
    list += getOpModeConfig( opModeName );

    // find all the OpModeConfigs for this OpMode
    for ( ( key, config ) in configMap )
    {
        if ( key.first == opModeName && key.second != "default" )
        {
            list += config;
        }
    }

    return list;
}

// configs.json:
//
//  "configs": [
//      [
//          $OpModeName,
//          $ActiveVariant,
//          ${OpModeConfig.toJson()},
//          ...
//      ],
//      ...
//  ]

/**
 * Loads all the OpModeConfigs from the given file.
 *
 * @param[f]
 *          The json file to load.
 */
fun loadConfigs( f: File )
{
    // just create the file if it doesn't exist
    if ( !f.exists() )
    {
        f.createNewFile();
        return;
    }

    try
    {
        val json = JSONObject( f.readText() ); // create the main JSON object
        val configArray = json.getJSONArray( "configs" ); // the main array of all objects

        for ( i in 0..configArray.length() )
        {
            val opModeArray = configArray.getJSONArray( i );

            // the first two elements are metadata, not configs
            val opModeName = opModeArray.getString( 0 );
            val activeVariant = opModeArray.getString( 1 );

            activeVariants[ opModeName ] = activeVariant; // set the active variant for this opmode

            // add all the variants to the map
            for ( j in 2..opModeArray.length() )
            {
                val config = OpModeConfig( opModeName );
                config.fromJson( opModeArray.getJSONObject( i ) );
                configMap[ Pair( opModeName, config.Variant ) ] = config; // add it to the map

                config.e( "Loaded OpModeConfig for ${config.OpModeName} (${config.Variant} variant)" );
            }
        }
    }
    catch ( e: JSONException )
    {
        Log.e( "ftcext.OpModeConfig", "JSONException while loading configs!", e );
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

    for ( ( opMode, variant ) in activeVariants )
    {
        writer.beginArray(); // begin the array of variants for our opmode

        writer.value( opMode ); // [ 0 ] = opMode name
        writer.value( variant ); // [ 1 ] = active variant

        // [ 2 ]..[ n ] = variants
        for ( config in getOpModeConfigs( opMode ) )
        {
            config.toJson( writer );
        }

        writer.endArray(); // end the variant array
    }

    writer.endArray(); // end our array
}