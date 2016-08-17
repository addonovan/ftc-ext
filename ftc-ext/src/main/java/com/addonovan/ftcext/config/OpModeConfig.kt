/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Austin Donovan (addonovan)
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
package com.addonovan.ftcext.config

import android.util.JsonWriter
import com.addonovan.ftcext.*
import com.addonovan.ftcext.MalformedJsonException
import org.json.JSONObject
import java.util.*

/**
 * !Description!
 *
 * Serializes to
 * ```json
 * {
 *   name: "name",
 *   activeProfile: "activeProfileName",
 *   profiles: [
 *     {
 *       name: "name",
 *       config: [
 *         [ "name", type, value ],
 *         ...
 *       ]
 *     },
 *     ...
 *   ]
 * }
 * ```
 *
 * @param[Name]
 *          The name of the OpMode.
 *
 * @author addonovan
 * @since 8/17/16
 */
class OpModeConfig private constructor( val Name: String ) : Jsonable, ILog by getLog( "OpModeConfig.$Name" )
{

    companion object
    {
        /** The name of the default profile. */
        const val DEFAULT_NAME = "[default]";

        /**
         * Creates a new OpModeConfig from the given JSONObject.
         *
         * @param[json]
         *          The json to parse into an OpModeConfig.
         *
         * @throws MalformedJsonException
         *          If the json was set up in an incorrect manner.
         *
         * @return The resultant OpModeConfig.
         */
        fun fromJson( json: JSONObject ): OpModeConfig
        {
            // check for the length of the object
            if ( json.length() != 3 ) throw MalformedJsonException( "Expected 3 children, received ${json.length()}" );

            // check for the tags
            if ( !json.has( "name" ) ) throw MalformedJsonException( "Missing name tag" );
            if ( !json.has( "activeProfile" ) ) throw MalformedJsonException( "Missing activeProfile tag" );
            if ( !json.has( "profiles" ) ) throw MalformedJsonException( "Missing profiles tag" );


            val name = json.getString( "name" );
            val opModeConfig = OpModeConfig( name );

            // load the profiles
            val profiles = json.getJSONArray( "profiles" );
            for ( i in 0..profiles.length() - 1 )
            {
                val profile = Profile.fromJson( opModeConfig, profiles.getJSONObject( i ) );

                // if it's the default profile, add it to the very beginning, as it should always be index 0
                if ( profile.Name == DEFAULT_NAME )
                {
                    opModeConfig.profiles.add( 0, profile );
                }
                // otherwise, throw them on at the end, it doesn't matter
                else
                {
                    opModeConfig.profiles += profile;
                }

                opModeConfig.v( "Loaded Profile: ${profile.Name}" );
            }

            // if, somehow, we don't have the default profile, add it to the initial position
            if ( opModeConfig.profiles[ 0 ].Name != DEFAULT_NAME )
            {
                opModeConfig.profiles.add( 0, Profile.fromRaw( opModeConfig, DEFAULT_NAME ) );
                opModeConfig.w( "Had to create new default profile. Did someone mess around in the config?" );
            }

            opModeConfig.i( "Loaded ${profiles.length()} profiles" );

            // active the profile as listed in the json
            val activeProfileName = json.getString( "activeProfile" );
            opModeConfig.i( "Setting active profile to: $activeProfileName" );
            opModeConfig.setActiveProfile( activeProfileName );

            return opModeConfig;
        }

    }

    //
    // Vals
    //

    /** The list of profiles that are available */
    private val profiles = ArrayList< Profile >();

    /** The index of the active profile. 0=&#91;default&#93; */
    private var activeProfileName = DEFAULT_NAME;

    /** The profile that's active and will be used by the OpMode. */
    val ActiveProfile: Profile
        get()
        {
            // find the matching one
            for ( profile in profiles )
            {
                if ( profile.Name == activeProfileName )
                {
                    return profile;
                }
            }

            return profiles[ 0 ]; // return [default] by default
        }

    //
    // Actions
    //

    /**
     * Sets the active profile to the one with the name [name].
     *
     * If no profile was found with the given name, then this will
     * instead switch to the default profile.
     *
     * @param[name]
     *          The name of the profile to switch to.
     *
     * @return `true` if the new profile has the name [name], `false`
     *         if it had to be set to the [DEFAULT_NAME] instead.
     */
    fun setActiveProfile( name: String ): Boolean
    {
        for ( profile in profiles )
        {
            // we found a match, so switch the profile and exit
            if ( profile.Name == name )
            {
                v( "Switching active profile to: $name" );
                activeProfileName = name;
                return true;
            }
        }

        // if we got to here, then there was no active profile by the name
        // set it to default and return false
        e( "Failed to switch to profile: $name! Defaulting to $DEFAULT_NAME instead!" );
        activeProfileName = DEFAULT_NAME;
        return false;
    }

    //
    // Overrides
    //

    override fun toJson( writer: JsonWriter )
    {
        writer.beginObject();
        writer.name( "name" ).value( Name );
        writer.name( "activeProfile" ).value( activeProfileName );

        // write the profiles
        writer.name( "profiles" ).beginArray();
        profiles.forEach { it.toJson( writer ); };
        writer.endArray();

        writer.endObject();
    }

}
