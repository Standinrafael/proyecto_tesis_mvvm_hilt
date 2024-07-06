package com.example.proyecto_tesis.data.entities

data class User(
    val id: Int,
    val userId: String,
    val uniquePassword: String
) {
    companion object {
        var nextId: Int = 1
    }

    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to this.id,
            "user_id" to this.userId,
            "uniquePassword" to this.uniquePassword
        )
    }
}
