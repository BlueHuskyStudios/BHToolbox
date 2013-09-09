package bht.tools.util.math.fxn;

public class Function extends Number implements Operation
{
	private Operation operation;
	
	public Function(Operation initOperation)
	{
		operation = initOperation;
	}
	
	@Override
	public double getValue()
	{
		return operation.getValue();
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
		return getValue();
	}
}