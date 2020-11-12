package com.example.smartpi.model

data class SignUpModel(
    val data: Data? = null,
    val meta: MetaSignUp? = null
)

data class MetaSignUp(
    val token: String? = null
)

data class Data(
    val skypeId: String? = null,
    val nik: String? = null,
    val role: String? = null,
    val activationCode: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val registered: String? = null,
    val id: Int? = null,
    val email: String? = null,
    val username: String? = null,
    val status: Int? = null
)

