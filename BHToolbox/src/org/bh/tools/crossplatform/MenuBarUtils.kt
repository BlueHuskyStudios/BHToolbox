package org.bh.tools.crossplatform

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
class MenuBarUtils {
    companion object {
        /**
         * Sets whether the OSX menu bar is used. If the platform is not OS X, nothing happens.
         * Future implementations may allow for the Ubuntu Unity menu bar to be used.
         *
         * @param uses if true, then this application uses the OS X menu bar.
         *
         * @param applicationName The name of the application. If it is null, the name is not changed.
         *
         * @version 2.0.0
         *          ! 2016-10-09 - Ben Leggiero migrated this to BHToolbox version 2 and translated it to Kotlin
         */
        fun setUsesOSMenuBar(uses: Boolean, applicationName: CharSequence) {
            System.setProperty("apple.laf.useScreenMenuBar", uses.toString())
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", applicationName.toString())

            //TODO: Unity menu bar, too
        }
    }
}
