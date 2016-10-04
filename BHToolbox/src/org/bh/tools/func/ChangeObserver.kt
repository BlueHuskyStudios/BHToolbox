package org.bh.tools.func

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Copyright BHStudios ©2016 BH-1-PS. Made for BH Tic Tac Toe IntelliJ Project.
 *
 * @author Ben Leggiero
 * @since 2016-10-03
 */
class ChangeObserver<in ThisRefType, PropertyType>(
        var initialValue: PropertyType,
        val willSet: WillSetBlock<PropertyType> = NullWSB(),
        val shouldSet: ShouldSetBlock<PropertyType> = NullSSB(),
        val didSet: DidSetBlock<PropertyType> = NullDSB()
): ReadWriteProperty<ThisRefType, PropertyType> {
    val syncLock = Any()

    override fun getValue(thisRef: ThisRefType, property: KProperty<*>): PropertyType {
        return initialValue
    }

    override fun setValue(thisRef: ThisRefType, property: KProperty<*>, value: PropertyType) {
        synchronized(syncLock) {
            if (shouldSet(initialValue, value)) {
                willSet(initialValue, value)
                initialValue = value
                didSet(initialValue, value)
            }
        }
    }
}

/**
 * The kind of block called before something is set
 */
typealias WillSetBlock<PropertyType> =  ___SetBlock<PropertyType, Unit>

/**
 * The kind of block called after something is set
 */
typealias ShouldSetBlock<PropertyType> =  ___SetBlock<PropertyType, Boolean>

/**
 * The kind of block called after something is set
 */
typealias DidSetBlock<PropertyType> = ___SetBlock<PropertyType, Unit>

/**
 * Some kind of block called at some point when something is set
 */
private typealias ___SetBlock<PropertyType, ReturnType> = (oldValue: PropertyType, newValue: PropertyType) -> ReturnType


/**
 * A non-functioning [WillSetBlock] that would otherwise do something before setting
 */
fun <PropertyType> NullWSB(): WillSetBlock<PropertyType> = {a, b -> }

/**
 * A non-functioning [ShouldSetBlock] that would otherwise do something after setting
 */
fun <PropertyType> NullSSB(): ShouldSetBlock<PropertyType> = {a, b -> true}

/**
 * A non-functioning [DidSetBlock] that would otherwise do something after setting
 */
fun <PropertyType> NullDSB(): DidSetBlock<PropertyType> = {a, b -> }


/**
 * Allows you to use Swift-like `willSet`, `shouldSet`, and `didSet` values
 */
fun <PropertyType> observing(initialValue: PropertyType,
                             willSet: WillSetBlock<PropertyType> = NullWSB(),
                             shouldSet: ShouldSetBlock<PropertyType> = NullSSB(),
                             didSet: DidSetBlock<PropertyType> = NullDSB()
) = object : ObservableProperty<PropertyType>(initialValue) {
    override fun beforeChange(property: KProperty<*>, oldValue: PropertyType, newValue: PropertyType): Boolean
            = if (shouldSet(oldValue, newValue)) { willSet(oldValue, newValue); true } else { false }

    override fun afterChange(property: KProperty<*>, oldValue: PropertyType, newValue: PropertyType)
            = didSet(oldValue, newValue)
}
