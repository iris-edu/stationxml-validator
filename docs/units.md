The [SEED Manual](http://www.fdsn.org/publications/) provides guidelines for specifying unit names<sup>[1](#seed-rules)</sup>.  Furthermore there exist rules and rules and conventions for SI units<sup>[2](#si-references)</sup>.  Combining these with common conventions and proper grammar we define a set of rules for validating unit names in StationXML documents as follows:

* Use SI units, base or derivative, and their standard abbreviations whenever possible.  Spell out and avoid abbreviations of non-SI units.
* Exponents for powers are specified with <b>\*\*</b>, e.g. "s\*\*2".
* Use parentheses sparingly, only when FORTRAN-like precedence would not be correct.
* Unit definitions are singular, not plural.
* Unit names should not mix symbols and full names, use one or the other, e.g. use *m/s* or *meter/second* but not *meter/s* or *m/second*.
* Units should be used case sensitively, using the case specified by SI rules.  Historically, in SEED, units are upper case.  For this reason validation will warn on case mismatches and only fail on complete mismatches.

These rules are intended as conventions for the unit name values, not necessarily for the unit description values, which are typically more verbose.

## Common unit names

Below are the common unit names checked by the IRIS validator.  The values are between double quotes and are in a list format to reduce human error when transferring to code.  We expect this list to grow as needed and conform to the rules above.

    "meter", "m", "m/s", "m/s**2",
    "centimeter", "cm", "cm/s", "cm/s**2",
    "millimeter", "mm", "mm/s", "mm/s**2", "mm/hour",
    "micrometer", "um", "um/s", "um/s**2",
    "nanometer", "nm", "nm/s", "nm/s**2",
    "second", "s", "millisecond", "ms", 
    "microsecond", "us", "nanosecond", "ns",
    "minute", "min",
    "hour",
    "radian", "rad", "microradian", "urad", 
    "nanoradian", "nrad",
    "rad/s", "rad/s**2",
    "degree", "deg",
    "kelvin", "K",
    "celsius", "degC",
    "candela", "cd",
    "pascal", "Pa", "kilopascal", "kPa", 
    "hectopascal", "hPa",
    "bar", "bars", "millibar", "mbar",
    "ampere", "amperes", "A", "milliamp", "mA",
    "volt", "V", "millivolt", "mV", 
    "microvolt", "uV",
    "ohm",
    "hertz", "Hz",
    "newton", "N",
    "joule", "J",
    "tesla", "T", "nanotesla", "nT",
    "strain", "m/m", "m**3/m**3", "cm/cm", "mm/mm", "um/um", "nm/nm", 
    "microstrain",
    "watt", "W", "milliwatt", "mW",
    "V/m",
    "W/m**2",
    "hit/(cm**2*hour)",
    "gap",
    "reboot",
    "byte","bit",
    "bit/s",
    "percent","%",
    "count","counts",
    "number",
    "unitless",
    "unknown"

[As of 2019-05-06]
## SEED rules

From the SEED 2.4 (August 2012) Manual, Blockette 34, field 4 (unit name) description:

    The basic unit name, formatted as FORTRAN-like equations with all alphabetic
    characters in upper case. Specify exponents by the “**” format, and use parentheses
    sparingly — only when normal FORTRAN precedence would not be correct. Use the
    standard exponential notation (e.g., “1E-9” not “1*10**-9”) for powers of 10.
    Use SI units and their standard abbreviations whenever possible; spell out and
    do not abbreviate non-SI units. For reasons of convertibility, abbreviations
    are represented in uppercase although this is not SI convention.

    Units of ground motion are typically defined as:
    Displacement M
    Velocity M/S
    Acceleration M/S**2


## SI References

Rules and style conventions from NIST: [http://physics.nist.gov/cuu/Units/checklist.html](http://physics.nist.gov/cuu/Units/checklist.html)

List of SI base and derivative units from NIST: [http://www.physics.nist.gov/cuu/Units/units.html](http://www.physics.nist.gov/cuu/Units/units.html)

Wikipedia page for SI units: [https://en.wikipedia.org/wiki/International_System_of_Units](https://en.wikipedia.org/wiki/International_System_of_Units)

