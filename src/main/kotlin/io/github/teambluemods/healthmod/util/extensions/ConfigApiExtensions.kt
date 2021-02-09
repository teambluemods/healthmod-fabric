package io.github.teambluemods.healthmod.util.extensions

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder
import me.sargunvohra.mcmods.autoconfig1u.serializer.ConfigSerializer

inline fun <reified T : ConfigData> registerConfig(serializerFactory: ConfigSerializer.Factory<T>): ConfigHolder<T> =
    AutoConfig.register(T::class.java, serializerFactory)

inline fun <reified T : ConfigData> getConfig(): T = AutoConfig.getConfigHolder(T::class.java).get()