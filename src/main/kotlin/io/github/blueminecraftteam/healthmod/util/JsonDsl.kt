package io.github.blueminecraftteam.healthmod.util

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class JsonDsl {
    private val nested: MutableMap<String, JsonDsl> = mutableMapOf()
    private val properties: MutableMap<String, JsonPrimitive> = mutableMapOf()

    fun `object`(name: String, apply: JsonDsl.() -> Unit) = apply(JsonDsl().also { nested[name] = it })

    private fun internalProperty(key: String, value: Any) {
        when {
            Boolean::class.isInstance(value) -> properties[key] = JsonPrimitive(value as Boolean)
            Number::class.isInstance(value) -> properties[key] = JsonPrimitive(value as Number)
            String::class.isInstance(value) -> properties[key] = JsonPrimitive(value as String)
            Char::class.isInstance(value) -> properties[key] = JsonPrimitive(value as Char)
            else -> error("begone")
        }
    }

    fun property(key: String, value: Boolean) {
        internalProperty(key, value)
    }

    fun property(key: String, value: Number) {
        internalProperty(key, value)
    }

    fun property(key: String, value: String) {
        internalProperty(key, value)
    }

    fun property(key: String, value: Char) {
        internalProperty(key, value)
    }

    fun toJsonElement(): JsonElement = JsonObject().apply {
        for ((name, property) in properties) this.add(name, property)
        for ((name, nestedDsl) in nested) this.add(name, nestedDsl.toJsonElement())
    }
}

fun json(apply: JsonDsl.() -> Unit) = JsonDsl().apply { apply(this) }