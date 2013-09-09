package bht.tools.util.math;

import java.util.Random;

/**
 * Averager, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012. License is default.<hr/>
 * Averages very many numbers while only using 128 bits of memory (one {@code double} and one {@code long}), maximum, to store
 * the average information. This also allows for a more accurate result than adding all and dividing by the number of inputs.
 * The downside is that you sacrifice speed. As the build-in main method shows, rigorous tests put this method at up to 10
 * times slower. However, when working with a reasonable number of values (up to 1000 values), apeeds are much closer
 * (this is only about 1.5 times slower on average)
 * because there's no chance of an overflow
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 6, 2012
 * @version 1.0.0
 */
public class Averager extends Number //NOTE: Must be compiled in UTF-8
{
  private double currentAverage;
  private long timesAveraged;

  public Averager()
  {
    currentAverage = 0;
    timesAveraged = 0;
  }
  
  public Averager(double startingNumber)
  {
    currentAverage = startingNumber;
    timesAveraged = 1;
  }
  
  public strictfp Averager addToAverage(double... d)
  {
    for (double e : d)
      addToAverage(e);
    return this;
  }
  public strictfp Averager addToAverage(double d)
  {
    currentAverage = ((currentAverage * timesAveraged) + d) / ++timesAveraged;
    return this;
  }
  
  public double getCurrentAverage()
  {
    return currentAverage;
  }

  public long getTimesAveraged()
  {
    return timesAveraged;
  }
  
  public Averager clearAverage()
  {
    currentAverage = 0;
    timesAveraged = 0;
    return this;
  }
  
  public static void main(String[] args)
  {
    System.out.println("Proof of concept:\n");
    System.out.print("How many values should be put into this test? >");
    double array[] = new double[new java.util.Scanner(System.in).nextInt()], range = Double.MAX_VALUE / Short.MAX_VALUE, mRes, tRes;
    System.out.println("\nAveraging " + Numbers.groupDigits(array.length) + " random floating-point numbers between 0 and " + range + "...");
    java.util.Random r = new Random();
    for (int i = 0; i < array.length; i++)
    {
      array[i] = r.nextDouble() * range;
    }
    
    long mStart, mEnd, tStart, tEnd, mTotal, tTotal;
    System.out.print("The traditional method says ");
    mStart = System.nanoTime();
    double temp = 0;
    for (double d : array)
    {
      temp += d;
    }
    System.out.print(mRes = (temp / array.length));
    mEnd = System.nanoTime();
    System.out.println(" \t(took " + Numbers.groupDigits(mTotal = Math.abs((mEnd - mStart))) + "ns)");
    
    
    System.out.print("This method says            ");
    tStart = System.nanoTime();
    System.out.print(tRes = (new Averager().addToAverage(array).getCurrentAverage()));
    tEnd = System.nanoTime();
    System.out.println(" \t(took " + Numbers.groupDigits(tTotal = Math.abs((tEnd - tStart))) + "ns)");
    
    double resDiff = mRes - tRes, resDiffAbs = Math.abs(resDiff);
    if (resDiff == 0)
      System.out.println("Results were identical!");
    else if (resDiffAbs < 0.00001)
      System.out.println("Results were nearly identical!");
    else
      System.out.println((resDiff < 0 ? "This" : "The traditional") + " method's result was smaller by " + resDiffAbs);
    
    long totalDiff = tTotal - mTotal;
    boolean thisWasFaster;
    System.out.println(((thisWasFaster = totalDiff < 0) ? "This" : "The traditional") +
                       " method was faster by " + Numbers.groupDigits(Math.abs(totalDiff)) + "ns (" +
                       (thisWasFaster ? (double)mTotal / tTotal : (double)tTotal / mTotal) +
                       " times faster); average time was " + Numbers.groupDigits(Numbers.mean(tTotal, mTotal)) + "ns");
  }

  @Override
  public int intValue()
  {
    return (int)longValue();
  }

  @Override
  public long longValue()
  {
    return (long)doubleValue();
  }

  @Override
  public float floatValue()
  {
    return (float)doubleValue();
  }

  @Override
  public double doubleValue()
  {
    return getCurrentAverage();
  }
}
