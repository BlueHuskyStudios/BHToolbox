package bht.test.tools.fx;

import bht.test.tools.fx.ani.AnimationEvent;
import bht.test.tools.fx.ani.AnimationListener;
import bht.test.tools.fx.ani.CompActionAnimation;
import bht.test.tools.fx.ani.DefCompActionAnimation;
import bht.test.tools.fx.ani.ExpandAnimation;
import bht.tools.util.BHTimer;
import bht.tools.util.dynamics.Bézier2D;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * CompAction5, made for BHToolbox, is copyright Blue Husky Programming ©2013
 *
 * @author Supuhstar of Blue Husky Programming
 * @version 1.0.0
 * @since 2013-05-13
 */
public class CompAction5
{
	BHTimer timer;

	public void animate(final Component component, final CompActionAnimation animation, final double targetDuration,
						double targetFPS, final AnimationListener animationListener)
	{
		final AnimationEvent EVT = new AnimationEvent(this);
		timer = new BHTimer(new ActionListener()
		{
			long START = -1, now, targetMillis = (long) (targetDuration * 1000);

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (START < 0) START = System.currentTimeMillis();
				double progress = (double) ((now = System.currentTimeMillis()) - START) / targetMillis;
				System.out.println("progress: " + progress);
				if (progress >= 1) // If the target duration has been exceeded
				{
					animation.setAnimationPosition(1);
					component.setBounds(animation.findLocation());
					if (animationListener != null)
						animationListener.animationCompleted(EVT);
					timer.stop();
					timer.reset();
				}
				else
				{
					animation.setAnimationPosition(progress);
					if (animation.changesLoation())
						component.setBounds(animation.findLocation());
					if (animation.changesOpacity() && component instanceof Window)
						((Window) component).setOpacity((float) animation.findOpacity());
				}
			}
		}, targetFPS);
		if (animationListener != null)
			animationListener.animationStarting(EVT);
		timer.start();
	}

	public static void main(String[] args)
	{
		final CompAction5 CA = new CompAction5();
		final DefCompActionAnimation anim;

		final JFrame FRAME = new JFrame("Animation Test");
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton button = new JButton("Animate!");
		FRAME.setContentPane(button);
		FRAME.pack();
//		anim = new DefCompActionAnimation(Bézier2D.DEFAULT, true, new Rectangle(0, 0, 256, 256), new Rectangle(512, 256, 512, 512), false, 1, 1);
		anim = new ExpandAnimation(ExpandAnimation.CENTER, FRAME, Bézier2D.DEFAULT, true);
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CA.animate(FRAME, anim, 3, 30, null);
			}
		});
		FRAME.setVisible(true);
	}
}
