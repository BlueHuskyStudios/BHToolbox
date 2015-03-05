package bht.tools.util.math;

import static bht.tools.util.Do.s;

/**
 * Averager, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr/>
 * Averages very many numbers while only using 128 bits of memory (one {@code double} and one {@code long}), maximum, to store
 * the average information. This also allows for a more accurate result than adding all and dividing by the number of inputs.
 * The downside is that you sacrifice speed. As the build-in main method shows, rigorous tests put this method at up to 10
 * times slower. However, when working with a reasonable number of values (up to 1000 values), speeds are much closer
 * (this is only about 1.5 times slower on average)
 * because there's no chance of an overflow
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 6, 2012
 * @version 1.1.0
 * <pre>
 *		- 1.1.0
 *			+ Kyli added {@link #toString()}
 *			! Kyli spellchecked documentation
 *			! Kyli specified license
 *			! Kyli organized code structure
 *			+ Kyli added serial version UID
 *			. Kyli reinstated old tests
 * </pre>
 */
public class Averager extends Number //NOTE: Must be compiled in UTF-8
{
	private static final long serialVersionUID = 01_001_0001L;

	@SuppressWarnings({"UseOfSystemOutOrSystemErr", "NestedAssignment", "ConfusingArrayVararg",
					   "PrimitiveArrayArgumentToVariableArgMethod"})
	public static strictfp void main(String[] args) {

		System.out.println("Proof of concept:\n");
		System.out.print("How many values should be put into this test? >");
		double array[] = new double[new java.util.Scanner(System.in).nextInt()], // allocate the array of numbers
				range = Double.MAX_VALUE / Short.MAX_VALUE, // Range is huge, but not so huge that we risk overflow to Infinity
				mRes, // traditional method
				tRes; // this method
		System.out.println("\nAveraging " + Numbers.groupDigits(array.length) + " random floating-point numbers between 0 and "
								   + range + "...");
		java.util.Random r = new java.util.Random();
		for (int i = 0; i < array.length; i++) {
			array[i] = r.nextDouble() * range;
		}

		long mStart, mEnd, tStart, tEnd, mTotal, tTotal;
		System.out.print("The traditional method says ");
		mStart = System.nanoTime();
		double temp = 0;
		for (double d : array) {
			temp += d;
		}
		System.out.print(mRes = (temp / array.length));
		mEnd = System.nanoTime();
		System.out.println(" \t(took " + Numbers.groupDigits(mTotal = Math.abs((mEnd - mStart))) + "ns)");


		System.out.print("This method says ");
		tStart = System.nanoTime();
		System.out.print(tRes = (new Averager().addToAverage(array).getCurrentAverage()));
		tEnd = System.nanoTime();
		System.out.println(" \t(took " + Numbers.groupDigits(tTotal = Math.abs((tEnd - tStart))) + "ns)");

		double resDiff = mRes - tRes, resDiffAbs = Math.abs(resDiff);
		if (resDiff == 0) {
			System.out.println("Results were identical!");
		}
		else if (resDiffAbs < 0.00001) {
			System.out.println("Results were nearly identical!");
		}
		else {
			System.out.println((resDiff < 0 ? "This" : "The traditional") + " method's result was smaller by " + resDiffAbs);
		}

		long totalDiff = tTotal - mTotal;
		boolean thisWasFaster;
		System.out.println(((thisWasFaster = totalDiff < 0) ? "This" : "The traditional") + " method was faster by " + Numbers
				.groupDigits(Math.abs(totalDiff)) + "ns (" + (thisWasFaster ? (double) mTotal / tTotal : (double) tTotal
																										 / mTotal)
								   + " times faster); average time was " + Numbers.groupDigits(Numbers.mean(tTotal, mTotal))
						   + "ns");

		Averager a = new Averager(13);
		System.out.println("13.0 == " + a.getCurrentAverage());
		a.addToAverage(3);
		System.out.println("8.0 == " + a.getCurrentAverage());
		a.addToAverage(5, 12, 8, 7.3);
		System.out.println("8.05 == " + a.getCurrentAverage());

		Averager b = new Averager();
		System.out.println("0.0 == " + b.getCurrentAverage());
		b.addToAverage(13);
		System.out.println("13.0 == " + b.getCurrentAverage());
		b.addToAverage(55, 712.197, 18, 99);
		System.out.println("179.4394 == " + b.getCurrentAverage());

		Averager c = new Averager();
		c.addToAverage(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
		System.out.println("-0.5 == " + c.getCurrentAverage());

		System.out.println(a.getTimesAveraged());
		System.out.println(b.getTimesAveraged());
		System.out.println(c.getTimesAveraged());
	}
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

	public Averager clearAverage()  {
		currentAverage = 0;
		timesAveraged = 0;
		return this;
	}

	@Override
	public double doubleValue()  {
	  return getCurrentAverage();
  }

	@Override
	public float floatValue()  {
	  return (float) doubleValue();
  }

	public double getCurrentAverage()  {
		return currentAverage;
	}

	public long getTimesAveraged() {
		return timesAveraged;
	}

  @Override
  public int intValue()
  {
	  return (int) longValue();
  }

  @Override
  public long longValue()
  {
	  return (long) doubleValue();
  }

  @Override
	public String toString()  {
	  return s(doubleValue());
  }

}
