package io.github.blueminecraftteam.healthmod.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

inline fun <reified T> logger(): Logger = LogManager.getLogger(T::class.java)

inline fun <reified T> warn(message: String) = logger<T>().warn(message)

inline fun <reified T> info(message: String) = logger<T>().info(message)

inline fun <reified T> debug(message: String) = logger<T>().debug(message)
