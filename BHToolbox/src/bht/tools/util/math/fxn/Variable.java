package bht.tools.util.math.fxn;

public class Variable implements Operation
{
	private double value;
	
	public Variable(double initValue)
	{
		value = initValue;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public Variable setValue(double newValue)
	{
		value = newValue;
		return this;
	}
}