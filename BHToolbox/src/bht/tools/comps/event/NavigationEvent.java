package bht.tools.comps.event;

/**
 * NavigationEvent, made for BHToolbox, is made by and copyrighted to Blue Husky Programming, ©2012 BH-1-PS.<hr>
 *
 * @author Supuhstar of Blue Husky Programming
 * @since Apr 11, 2012
 * @version 1.1.0
 *		- 2014-08-19 (1.1.0) - Kyli Rouge created a NavigationState enum to use instead of a byte to represent state. A
 *			{@link NavigationState#STATE_BYTE} was put in for compatibility.
 */
public class NavigationEvent extends java.util.EventObject //NOTE: Must be compiled in UTF-8
{
	private final Number oldPosition, newPosition;
	private final Object oldObject, newObject;
	private final NavigationState state;

	/**
	 * Creates a new {@link NavigationEvent}, assigning all the required attributes to it.
	 *
	 * @param source The object on which the Event initially occurred.
	 * @param oldPosition The position before navigation occurred
	 * @param newPosition The position after navigation occurred
	 * @param oldObject The object that was being observed before navigation occurred
	 * @param newObject The object that was being observed before navigation occurred
	 * @param state The type of navigation. Valid inputs are {@link #STATE_GOING_BACKWARD} ({@value #STATE_GOING_BACKWARD}),
	 * {@link #STATE_GOING_FORWARD} ({@value #STATE_GOING_FORWARD}), and {@link #STATE_SKIPPING} ({@value #STATE_SKIPPING})
	 */
	public NavigationEvent(Object source, Number oldPosition, Number newPosition, Object oldObject, Object newObject, NavigationState state)
	{
		super(source);

		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.oldObject = oldObject;
		this.newObject = newObject;
		this.state = state;
	}

	public Number getNewPosition()
	{
		return newPosition;
	}

	public Number getOldPosition()
	{
		return oldPosition;
	}

	public Object getNewObject()
	{
		return newObject;
	}

	public Object getOldObject()
	{
		return oldObject;
	}

	public NavigationState getState()
	{
		return state;
	}

	public static enum NavigationState
	{
		/** Signifies that we are navigating backward (previous, undo, etc.). {@link #STATE_BYTE} {@code = 0b00} */
		STATE_GOING_BACKWARD(0b00),
		/** Signifies that we are navigating forward (next, redo, etc.). {@link #STATE_BYTE} {@code = 0b01} */
		STATE_GOING_FORWARD(0b01),
		/** Signifies that we are skipping several items backward. {@link #STATE_BYTE} {@code = 0b10} */
		STATE_SKIPPING_BACKWARD(0b10),
		/** Signifies that we are skipping several items forward. {@link #STATE_BYTE} {@code = 0b11} */
		STATE_SKIPPING_FORWARD(0b11);
		/**
		 * A way of measuring this state with a byte. This will be constrained within {@link #STATE_MASK}.
		 * <table>
		 *	<caption>Bit Breakdown</caption>
		 *	<thead>
		 *		<tr><td></td> <th>Stepping</th> <th>Skipping</th></tr>
		 *	</thead>
		 *	<tbody>
		 *		<tr><th>Going backward</th> <td><code>0b00</code></td> <td><code>0b10</code></td></tr>
		 *		<tr><th>Going forward</th> <td><code>0b01</code></td> <td><code>0b11</code></td></tr>
		 *	</tbody>
		 * </table>
		 */
		public final byte STATE_BYTE;
		/**
		 * The constraint of all {@link #STATE_BYTE}s.
		 * They will all be between {@code 0b00} and {@code 0b11}.
		 * <br/>
		 * States that represent going backward will have a least-significant bit of {@code 0} ({@code ob?0}).
		 * States that represent going forward will have a least-significant bit of {@code 1} ({@code ob?1}).
		 * States that represent stepping will have a most-significant bit of {@code 0} ({@code ob0?}).
		 * States that represent skipping will have a most-significant bit of {@code 1} ({@code ob1?}).
		 * <br/>
		 * <table>
		 *	<caption>Bit Breakdown</caption>
		 *	<thead>
		 *		<tr><td></td> <th>Stepping</th> <th>Skipping</th></tr>
		 *	</thead>
		 *	<tbody>
		 *		<tr><th>Going backward</th> <td><code>0b00</code></td> <td><code>0b10</code></td></tr>
		 *		<tr><th>Going forward</th> <td><code>0b01</code></td> <td><code>0b11</code></td></tr>
		 *	</tbody>
		 * </table>
		 */
		public static final byte STATE_MASK = 0b11;
		public static final byte FORWARD_MASK = 0b01;
		public static final byte SKIPPING_MASK = 0b10;

		private NavigationState(int stateByte)
		{
			STATE_BYTE = (byte) stateByte;
		}
		
		public static NavigationState stateForByte(byte stateByte)
		{
			if ((stateByte|STATE_MASK) != STATE_MASK)
				throw new IllegalArgumentException("stateByte must be between 0 and " + STATE_MASK);
			return
				(stateByte|FORWARD_MASK) != 0 // forward?
					? (stateByte|SKIPPING_MASK) != 0 // skipping?
						? STATE_SKIPPING_FORWARD
						: STATE_GOING_FORWARD
					: (stateByte|SKIPPING_MASK) != 0 // skipping?
						? STATE_SKIPPING_BACKWARD
						: STATE_GOING_BACKWARD
			;
		}
	};
}
