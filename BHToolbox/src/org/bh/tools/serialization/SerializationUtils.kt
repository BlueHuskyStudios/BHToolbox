package org.bh.tools.serialization

import org.bh.tools.util.containsIgnoreCase
import org.bh.tools.util.plus
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * Constants and static methods used for saving in a Blue Husky way
 *
 * @author Ben Leggiero
 * @since 2016-10-09
 */
@Suppress("unused")
class SerializationUtilsWrapper {
    companion object SerializationUtils {
        val SANDBOX_DIR_STRING: String
        val COMPANY_SANDBOX_NAME = "Blue Husky"
        val SANDBOX_DIR_FILE: File

        init {
            SANDBOX_DIR_STRING =
                    (if (System.getProperty("os.name").containsIgnoreCase("windows"))
                        System.getenv("APPDATA")
                    else
                        System.getProperty("java.io.tmpdir")
                            ) + "\\$COMPANY_SANDBOX_NAME\\"
            SANDBOX_DIR_FILE = File(SANDBOX_DIR_STRING)
        }

        fun createSaveDirFor(programName: CharSequence, saveFileName: CharSequence): String
                = SANDBOX_DIR_STRING + programName + "\\" + saveFileName

        fun saveFileFor(programName: CharSequence, saveFileName: CharSequence): File
                = File(SANDBOX_DIR_FILE, programName + "\\" + saveFileName)

        fun createOutputStreamFor(programName: CharSequence, saveFileName: CharSequence): FileOutputStream {
            val outputFile = saveFileFor(programName, saveFileName)
            outputFile.parentFile.mkdirs()
            outputFile.createNewFile()
            try {
                return FileOutputStream(outputFile)
            } catch (ex: FileNotFoundException) {
                Logger.getLogger(SerializationUtils::class.java.name).log(Level.SEVERE, "Impossible FileNotFound exception", ex)
                throw AccessDeniedException(outputFile,
                        reason = "The file I just made wasn't found... Somehow. Perhaps I have write but not read permission?")
            }
        }

        fun createInputStreamFor(programName: CharSequence, fileName: CharSequence): FileInputStream
                = FileInputStream(saveFileFor(programName, fileName))
    }
}