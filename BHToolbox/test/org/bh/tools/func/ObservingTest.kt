package org.bh.tools.func

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.

 * @author ben_s
 * *
 * @since 004 2016-10-04
 */
class ObservingTest {
    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }


    @Test
    fun testClassBasicFunctionality() {
        var hitPoints: MutableList<Int> = mutableListOf()
        var test: String by Observing("first value",
                shouldSet = { old, new ->
                    hitPoints.add(1)
                    true
                },
                willSet = { old, new ->
                    hitPoints.add(2)
                },
                didSet = { old, new ->
                    hitPoints.add(3)
                })

        test = "second value"

        Assert.assertEquals(test, "second value")
        Assert.assertEquals(hitPoints.size, 3)
        Assert.assertEquals(hitPoints[0], 1)
        Assert.assertEquals(hitPoints[1], 2)
        Assert.assertEquals(hitPoints[2], 3)
    }
}
