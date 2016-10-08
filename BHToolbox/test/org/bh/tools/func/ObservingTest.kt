package org.bh.tools.func

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * @author Ben Leggiero
 * @since 2016-10-04
 */
class ObservingTest {
    companion object {
        val FirstValue = "first value"
        val SecondValue = "second value"
    }

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }


    var hitPointsByClass: MutableList<Int> = mutableListOf()
    var testByClass: String by Observing(FirstValue,
            shouldSet = { old, new ->
                hitPointsByClass.add(1)
                Assert.assertEquals(testByClass, FirstValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByClass.size, 1)
                Assert.assertEquals(hitPointsByClass[0], 1)
                true
            },
            willSet = { old, new ->
                hitPointsByClass.add(2)
                Assert.assertEquals(testByClass, FirstValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByClass.size, 2)
                Assert.assertEquals(hitPointsByClass[0], 1)
                Assert.assertEquals(hitPointsByClass[1], 2)
            },
            didSet = { old, new ->
                hitPointsByClass.add(3)
                Assert.assertEquals(testByClass, SecondValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByClass.size, 3)
                Assert.assertEquals(hitPointsByClass[0], 1)
                Assert.assertEquals(hitPointsByClass[1], 2)
                Assert.assertEquals(hitPointsByClass[2], 3)
            })

    @Test
    fun testByClassBasicFunctionality() {
        testByClass = SecondValue

        Assert.assertEquals(testByClass, SecondValue)
        Assert.assertEquals(hitPointsByClass.size, 3)
        Assert.assertEquals(hitPointsByClass[0], 1)
        Assert.assertEquals(hitPointsByClass[1], 2)
        Assert.assertEquals(hitPointsByClass[2], 3)
    }


    var hitPointsByMethod: MutableList<Int> = mutableListOf()
    var testByMethod: String by observing(FirstValue,
            shouldSet = { old, new ->
                hitPointsByMethod.add(1)
                Assert.assertEquals(testByMethod, FirstValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByMethod.size, 1)
                Assert.assertEquals(hitPointsByMethod[0], 1)
                true
            },
            willSet = { old, new ->
                hitPointsByMethod.add(2)
                Assert.assertEquals(testByMethod, FirstValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByMethod.size, 2)
                Assert.assertEquals(hitPointsByMethod[0], 1)
                Assert.assertEquals(hitPointsByMethod[1], 2)
            },
            didSet = { old, new ->
                hitPointsByMethod.add(3)
                Assert.assertEquals(testByMethod, SecondValue)
                Assert.assertEquals(old, FirstValue)
                Assert.assertEquals(new, SecondValue)
                Assert.assertEquals(hitPointsByMethod.size, 3)
                Assert.assertEquals(hitPointsByMethod[0], 1)
                Assert.assertEquals(hitPointsByMethod[1], 2)
                Assert.assertEquals(hitPointsByMethod[2], 3)
            })

    @Test
    fun testByMethodBasicFunctionality() {
        testByMethod = SecondValue

        Assert.assertEquals(testByMethod, SecondValue)
        Assert.assertEquals(hitPointsByMethod.size, 3)
        Assert.assertEquals(hitPointsByMethod[0], 1)
        Assert.assertEquals(hitPointsByMethod[1], 2)
        Assert.assertEquals(hitPointsByMethod[2], 3)
    }
}
