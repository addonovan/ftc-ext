package com.addonovan.ftcext.config

import android.os.Environment
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
class OpModeConfig internal constructor( name: String ) : Jsonable, ILog by getLog( OpModeConfig::class )
{

    //
    // Values
    //

    /** The name of the OpMode for which this is a configuration. */
    val OpModeName: String = name;

    /** The backing field for [VariantName], this can be updated, but only inside the class. */
    private var _variantName: String = "[default]";

    /** The variant of configuration for the OpMode. */
    val VariantName: String
        get() = _variantName;

    //
    // Data Maps
    //

    private val longMap = HashMap< String, Long >();

    private val doubleMap = HashMap< String, Double >();

    private val booleanMap = HashMap< String, Boolean >();

    private val stringMap = HashMap< String, String >();

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
        _variantName = variant;
    }

    //
    // Actions
    //

    /**
     * Clears the maps so that it will be reset to the default
     * values on the next run.
     */
    fun clear()
    {
        longMap.clear();
        doubleMap.clear();
        booleanMap.clear();
        stringMap.clear();
    }

    /**
     * Activates this configuration.
     */
    fun activate() = setActiveConfig( OpModeName, VariantName);

    /**
     * Deletes this configuration.
     */
    fun delete() = deleteVariant( OpModeName, VariantName);

    //
    // Json Serialization
    //

    // Write to the json file
    // the JSONObject for an OpModeConfig will look like this:
    // {
    //      "variant": $variant,
    //      "longMap" : [
    //          [ "$key", $value ],
    //          ...
    //      ],
    //      "doubleMap" : ...
    //      "booleanMap" : ...
    //      "stringMap" : ...
    // }

    override fun toJson( writer: JsonWriter )
    {
        writer.beginObject(); // start OpModeConfig

        writer.name( "variant" ).value(VariantName);

        val maps = mapOf(
                Pair( "longMap", longMap ),
                Pair( "doubleMap", doubleMap ),
                Pair( "booleanMap", booleanMap ),
                Pair( "stringMap", stringMap )
        );

        // write the maps
        for ( ( mapName, map ) in maps )
        {
            writer.name( mapName ).beginArray(); // start the array
            for ( ( key, value ) in map )
            {
                writer.beginArray().value( key ).value( value.toString() ).endArray(); // each pair get its own array
            }
            writer.endArray(); // end the map array
        }

        writer.endObject(); // end OpModeConfig
    }

    override fun fromJson( json: JSONObject )
    {
        _variantName = json.getString( "variant" );

        val types = arrayOf( "longMap", "doubleMap", "booleanMap", "stringMap" );

        for ( typeName in types )
        {
            val array = json.getJSONArray( typeName ); // find the array for the given type

            for ( i in 0..array.length() - 1 )
            {
                val kvArray = array.getJSONArray( i ); // [0] = key, [1] = value

                val key = kvArray.getString( 0 );
                val value = kvArray.getString( 1 );

                // insert the key and value into the correct map
                when ( typeName )
                {
                    "longMap"    -> longMap.put( key, value.toLong() );
                    "doubleMap"  -> doubleMap.put( key, value.toDouble() );
                    "booleanMap" -> booleanMap.put( key, value.toBoolean() );
                    "stringMap"  -> stringMap.put( key, value );
                }
            }
        }
    }

    //
    // Operator Overloads
    //

    // Gets
    operator fun get( key: String, default: Long )    = longMap[ key ]    ?: set( key, default );
    operator fun get( key: String, default: Double )  = doubleMap[ key ]  ?: set( key, default );
    operator fun get( key: String, default: Boolean ) = booleanMap[ key ] ?: set( key, default );
    operator fun get( key: String, default: String )  = stringMap[ key ]  ?: set( key, default );

    // Sets
    operator fun set( key: String, value: Long ): Long
    {
        longMap[ key ] = value;
        return value;
    }

    operator fun set( key: String, value: Double ): Double
    {
        doubleMap[ key ] = value;
        return value;
    }

    operator fun set( key: String, value: Boolean ): Boolean
    {
        booleanMap[ key ] = value;
        return value;
    }

    operator fun set( key: String, value: String ): String
    {
        stringMap[ key ] = value;
        return value;
    }

    //
    // Get map
    //

    /**
     * Gets all the entries in the msg maps as an alphabetically ordered
     * list by key name.
     *
     * @return A list of all key-value pairs in this config, sorted alphabetically
     *         by their key.
     */
    internal fun getData(): ArrayList< Pair< String, Any > >
    {
        val list = ArrayList< Pair< String, Any > >();

        for ( ( key, value ) in longMap )    list += Pair( key, value );
        for ( ( key, value ) in doubleMap )  list += Pair( key, value );
        for ( ( key, value ) in booleanMap ) list += Pair( key, value );
        for ( ( key, value ) in stringMap )  list += Pair( key, value );

        // sort the list alphabetically by the key in each pair
        Collections.sort( list, { pair1, pair2 -> pair1.first.compareTo( pair2.first ); } );

        return list;
    }

    //
    // Overrides
    //

    override fun toString() = "$OpModeName [$VariantName]";

}

//
// Package Functions
//

/** The standard config file. */
val CONFIG_FILE = File( Environment.getExternalStorageDirectory(), "/FIRST/OpModeConfigs.json" );

/** The map of all OpModeConfigs */
private val configMap = HashMap< Pair< String, String >, OpModeConfig >();

/** The map of the opmode names vs. their active variants. */
private val activeVariants = HashMap< String, String >();

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
internal fun getOpModeConfig( opModeName: String, variant: String ): OpModeConfig
{
    val key = Pair( opModeName, variant ); // the opModeName and variant pair

    if ( configMap[ key ] == null )
    {
        val config = OpModeConfig( opModeName, variant );
        config.i( "Created config for $opModeName, (variant: $variant)" );
        configMap[ key ] = config;

        // set this to the active variant if there is none
        if ( activeVariants[ opModeName ] == null )
        {
            config.i( "Setting active variant for $opModeName to $variant" );
            activeVariants[ opModeName ] = variant;
        }
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
 * Sets the active variant.
 *
 * @param[opModeName]
 *          The name of the opmode.
 * @param[variant]
 *          The variant of opmode to activate.
 */
internal fun setActiveConfig( opModeName: String, variant: String )
{
    getOpModeConfig( opModeName, variant ).i( "$opModeName active variant is now $variant" ); // ensure it exists
    activeVariants[ opModeName ] = variant; // mark the active variant
}

/**
 * Finds the active variant name. If there was none, then `default`
 * is created and added.
 *
 * @param[opModeName]
 *          The name of the opmode to fetch the config for.
 * @return The active variant name of the given opmode.
 */
internal fun getActiveVariant( opModeName: String ): String
{
    if ( !activeVariants.containsKey( opModeName ) )
    {
        activeVariants[ opModeName ] = "default";
    }

    return activeVariants[ opModeName ]!!;
}

/**
 * Deletes the given variant.
 *
 * @param[opModeName]
 *          The name of the opmode.
 * @param[variant]
 *          The variant of the opmode to delete.
 */
internal fun deleteVariant( opModeName: String, variant: String )
{
    // reset it back to default if it isn't already
    if ( activeVariants[ opModeName ] == variant )
    {
        activeVariants[ opModeName ] = "default";
    }

    configMap.remove( Pair( opModeName, variant ) ); // remove it from the map
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
    list += getOpModeConfig( opModeName, "default" );

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
        if ( !f.parentFile.exists() ) f.parentFile.mkdirs();

        f.createNewFile();
        return;
    }

    try
    {
        val json = JSONObject( f.readText() ); // create the main JSON object
        val configArray = json.getJSONArray( "configs" ); // the main array of all objects

        for ( i in 0..configArray.length() - 1 )
        {
            val opModeArray = configArray.getJSONArray( i );

            // the first two elements are metadata, not configs
            val opModeName = opModeArray.getString( 0 );
            val activeVariant = opModeArray.getString( 1 );

            activeVariants[ opModeName ] = activeVariant; // set the active variant for this opmode

            // add all the variants to the map
            for ( j in 2..opModeArray.length() - 1 )
            {
                val config = OpModeConfig( opModeName );
                config.fromJson( opModeArray.getJSONObject( j ) );
                configMap[ Pair( opModeName, config.VariantName) ] = config; // add it to the map

                config.v( "Loaded OpModeConfig for ${config.OpModeName} (${config.VariantName} variant)" );
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

    writer.beginObject(); // apparently we have to be in a big-ol' object

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
    writer.endObject(); // end our big-ol' object

    writer.close(); // close the writer because we're nice
}