# ftc-ext

ftc-ext (pronounced: *fit kekst*) is an extension library built on the the
[standard ftc api](https://github.com/ftctechnh/ftc_app) that aims to make it easier to write OpModes by
simplifying life cycles and providing useful tools among other things.
This library was written entirely in [Kotlin](https://kotlinlang.org/) and its api will usually look a lot
nicer if you choose to use it as well; however, Java will work just as well.

The [wiki](https://github.com/addonovan/ftc-ext/wiki) is far more in-depth about installation and how to 
use individual API tools; however, below is a list of some neat features.

## [Hardware Access](https://github.com/addonovan/ftc-ext/wiki/Hardware%20Access)

The OpMode lifecycle when using ftc-ext has been simplified. No longer do you have to separate the declaration
from the initialization when creating hardware devices, it can be done immediately, as the hardware map has a
value the moment that the OpMode is created.

On top of the immediate access to the hardware map, there are many convenient methods for accessing the hardware
map's device mappings, meaning the lines take less time to write. This was done because access to the underlying
hardware map in Java now requires a method call (`getHardwareMap()` instead of simply `hardwareMap`).
[Here](https://github.com/addonovan/ftc-ext/wiki/Hardware-Access:-Standard-Methods) is a list of methods to get
the correct hardware device with the new way.

#### Old Way
**Java**
```java
public class MyOpMode extends OpMode
{
    private final DcMotor motor;
    
    @Override
    public void init()
    {
        motor = hardwareMap.dcMotor.get( "motor_name" );
    }
}
```

#### New Way
**Java**
```java
public class MyOpMode extends OpMode
{
    private final DcMotor motor1 = motor( "motor_name" );
    private final DcMotor motor2 = ( DcMotor ) getDevice( "motor_name", DcMotor.class );
}
```
**Kotlin**
```kotlin
class MyOpMode : OpMode()
{
    private val motor1 = motor( "motor_name" );
    private val motor2: DcMotor = getDevice( "motor_name" );
}
```
`motor1` and `motor2` will have the same value, this just shows the different ways to access the same thing.  

## [Profiles & Configuration](https://github.com/addonovan/ftc-ext/wiki/Profiles%20&%20Configuration).

All OpModes support configuration without any extra effort. Variables can be assigned a value based on values in
a configuration file (which is easily editable inside the robot controller app itself!), and it will resort to a
default value if need be.

The configuration api supports 4 data types: `long`, `double`, `boolean`, and `String`. Together, these values
should be able to cover almost everything you would ever need.

**Java**
```java
private long waitTime = get( "wait_time", 1000 );
private double powers = get( "powers", 0.5 );
private boolean isRed = get( "is_red", false );
private String myName = get( "my_name", "Freddo" );
```
**Kotlin**
```kotlin
private val waitTime = get( "wait_time", 1000L );
private val motorPower = get( "motor_power", 0.5 );
private val isRed = get( "is_red", false );
private val myName = get( "my_name", "Freddo" );
```

## [Hardware Extension](https://github.com/addonovan/ftc-ext/wiki/Advanced%20Hardware)

Hardware extensions are new classes that add new functionality to old hardware devices. Take, for
example, the `ToggleServo` class, which is an ftc-ext standard class. This class extends a `Servo`
but also allows for the toggling between two positions (with a timer to make sure that a minimum
amount of time passes before it toggles again. This time is also configurable). Or the
`ContinuousToggleServo` class which extends the new `ContinuousServo` class (which extends `Servo`).
This class allows for essentially the same functionality as the `ToggleServo` does; however, instead
of moving to a position when toggle, this moves in a direction for a (configurable) amount of time.

To access a hardware extension, simply use the `getDevice` method for accessing hardware as shown above
(sorry Java users, it has to be done this way).  

**Java**
```java
private final ToggleServo toggleServo = ( ToggleServo ) getDevice( "toggle_servo", ToggleServo.class );
private final ContinuousToggleServo cts = ( ContinuousToggleServo ) getDevice( "cts", ContinuousToggleServo.class );
```
**Kotlin**
```kotlin
private val toggleServo: ToggleServo = getDevice( "toggle_servo" );
private val cts: ContinuousToggleServo = getDevice( "cts" );
```

## [Task Management](https://github.com/addonovan/ftc-ext/wiki/Task%20Management)

TODO write





















