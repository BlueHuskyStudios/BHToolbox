package org.bh.tools.math;



import static org.bh.tools.util.Do.S.s;

/**
 * Averager, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 *
 * @author Kyli and Ben of Blue Husky Programming
 * @version 2.2.0
 *          - 2.2.0 (2015-03-07) - Ben updated to work without version 1, cleaned up code
 *          - 2.0.0 (2015-03-07) - Kyli recreated Averager for BHToolbox 2
 * @since 2015-03-07
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Averager extends Number {
    private static final long serialVersionUID = 0x02_002_0001L;

    private double currentAverage;
    private long   timesAveraged;



    public Averager() {
        currentAverage = 0;
        timesAveraged = 0;
    }

    public Averager(double startingNumber) {
        currentAverage = startingNumber;
        timesAveraged = 1;
    }

    public strictfp Averager average(double... d) {
        for (double e : d) {
            average(e);
        }
        return this;
    }

    public strictfp Averager average(double d) {
        currentAverage = ((currentAverage * timesAveraged) + d) / ++timesAveraged;
        return this;
    }

    public Averager clear() {
        currentAverage = 0;
        timesAveraged = 0;
        return this;
    }

    public long getTimesAveraged() {
        return timesAveraged;
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return (long) doubleValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return getCurrentAverage();
    }

    public double getCurrentAverage() {
        return currentAverage;
    }

    @Override
    public String toString() {
        return s(doubleValue());
    }
}
