package bht.tools.util.math.fxn;

public class BinaryOperation implements Operation
{
	public Operation left, right;
	public Operator operator;
	
	public BinaryOperation(Operation initLeft, Operator initOperator, Operation initRight)
	{
		left = initLeft;
		operator = initOperator;
		right = initRight;
	}
	
	public double getValue()
	{
		return operator.evaluate(left, right);
	}
}