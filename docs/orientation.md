# Channel:Code Orthogonal Orientation Guidelines 
From the SEED 2.4 (August 2012) Manual, Appendix A

**Azimuth: degrees from north, clockwise.**   
**Dip: degrees from horizontal.**

The azimuth and dip describe the direction of an instrument's sensitive axis (if applicable). Motion in the same direction as the axis is positive. An instrument's reported dip and azimuth must reflect how an instrument is deployed. If instrument polarity is reversed in the field than the dip and azimuth must be consistent with this fact. Best practices in metadata exchange suggest that insturment orientation should never be corrected and subsquently exchanged using post processing techniques.

## Orthogonal Orientations
<br/>N — Dip 0, Azimuth 0 degrees (Reversed: Dip 0, Azimuth 180 degrees).<br/>E — Dip 0, Azimuth 90 degrees (Reversed: Dip 0, Azimuth 270 degrees).<br/>Z — Dip -90, Azimuth 0 degrees (Reversed: Dip 90, Azimuth 0 degrees).

If orthogonal instrument orientations are not within a 5 degree tolerance of N,E,Z, use non-traditional orthogonal orientations.


## Non-traditional Orthogonal Orientations

<br/>1 — Channel Azimuth is greater than 5 degrees from north (Reversed: south).<br/>2 — Channel Azimuth is greater than 5 degrees from east (Reversed: west).<br/>3 — Channel Dip is greater than 5 degrees from vertical.

[Updated 04-2020]
