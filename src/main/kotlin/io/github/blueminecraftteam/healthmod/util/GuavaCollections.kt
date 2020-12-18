package io.github.blueminecraftteam.healthmod.util

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet

fun <T> immutableSetOf(vararg elements: T): ImmutableSet<T> = ImmutableSet.copyOf(elements)

fun <T> immutableListOf(vararg elements: T): ImmutableList<T> = ImmutableList.copyOf(elements)

fun <A, B> immutableMapOf(vararg elements: Pair<A, B>): ImmutableMap<A, B> = ImmutableMap.copyOf(elements.toMap())