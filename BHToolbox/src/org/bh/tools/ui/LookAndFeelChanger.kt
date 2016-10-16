package org.bh.tools.ui

import com.sun.java.swing.plaf.motif.MotifLookAndFeel
import org.bh.tools.ui.LafChangeErrorType.*
import org.bh.tools.util.MutableArrayPP
import java.awt.Window
import java.awt.event.ActionListener
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.plaf.metal.MetalLookAndFeel
import javax.swing.plaf.nimbus.NimbusLookAndFeel

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * A convenience class for changing the Java Look-And-Feel.
 *
 * @author Ben Leggiero
 * @since 2016-10-01
 * @version 2.0.0
 */
class LookAndFeelChanger {
    companion object {
        private var _changeListeners = MutableArrayPP<LafChangeListener>()
        internal val _defaultLaf = UIManager.getLookAndFeel()

        fun setLookAndFeel(lafEnum: LookAndFeelEnum,
                           force: Boolean = false,
                           errorHandler: LafChangeErrorHandler = NullLafChangeErrorHandler) {
            setLookAndFeel(className = lafEnum.lafClassName, force = force, errorHandler = errorHandler)
        }

        @Throws(UnsupportedLookAndFeelException::class, ClassNotFoundException::class, InstantiationException::class, IllegalAccessException::class)
        fun setLookAndFeel(className: CharSequence, force: Boolean = false, errorHandler: LafChangeErrorHandler) {
            if (!force && UIManager.getLookAndFeel().javaClass.name == className) {
                return
            }

            val previousLaf = UIManager.getLookAndFeel()
            try {
                UIManager.setLookAndFeel(className.toString())
                val windows = Window.getWindows()
                var isFrame: Boolean
                var state: Int
                for (i in windows.indices) {
                    val r = java.awt.Rectangle(windows[i].bounds)//Changed to new rather than assignment {Feb 22, 2012 (1.1.1) for Marian}
                    isFrame = windows[i] is java.awt.Frame
                    state = if (isFrame) (windows[i] as java.awt.Frame).state else java.awt.Frame.NORMAL
                    //        windows[i].pack();//Commented out Feb 27, 2012 (1.1.1) for Marian - Though it makes the window look better in the end, it introduces unneeded lag and interfered with CompAction animations
                    javax.swing.SwingUtilities.updateComponentTreeUI(windows[i])
                    if (isFrame) {
                        (windows[i] as java.awt.Frame).state = state
                    }
                    if (!isFrame || state != java.awt.Frame.MAXIMIZED_BOTH) {
                        windows[i].bounds = r
                    }
                }
            } catch (ex: UnsupportedLookAndFeelException) {
                UIManager.setLookAndFeel(previousLaf)
            } catch (t: Throwable) {
                try {
                    UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName())
                } catch (t2: ClassNotFoundException) {
                    errorHandler(NotALaf)
                } catch (t2: InstantiationException) {
                    errorHandler(CouldNotCreateLaf)
                } catch (t2: IllegalAccessException) {
                    errorHandler(InaccessibleLafConstructor)
                } catch (t2: UnsupportedLookAndFeelException) {
                    errorHandler(UnsupportedLaf)
                } catch (t2: Throwable) {
                    errorHandler(UnknownError)
                }
            }

            val evt = LafChangeEvent()
            for (changeListener in _changeListeners)
                changeListener?.lafChanged(evt)
        }

//        val _systemLaf: LookAndFeel by lazy { Class.forName(UIManager.getSystemLookAndFeelClassName()).newInstance() as LookAndFeel }
    }
}

enum class LookAndFeelEnum(val lafClassName: CharSequence) {
    Default(LookAndFeelChanger._defaultLaf.javaClass.name),
    System(UIManager.getSystemLookAndFeelClassName()),
    Metal(MetalLookAndFeel::class.java.name),
    Nimbus(NimbusLookAndFeel::class.java.name),
    Motif(MotifLookAndFeel::class.java.name),
}

typealias LafChangeErrorHandler = (type: LafChangeErrorType) -> Unit
val NullLafChangeErrorHandler: LafChangeErrorHandler = { x -> }

enum class LafChangeErrorType {
    NotALaf,
    CouldNotCreateLaf,
    InaccessibleLafConstructor,
    UnsupportedLaf,
    UnknownError
}

class LafChangeEvent() {

}

interface LafChangeListener : ActionListener {
    fun lafChanged(event: LafChangeEvent)
}
