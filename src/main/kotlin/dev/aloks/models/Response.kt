package dev.aloks.models

data class Success (
    val status: Int,
    val message: String,
    val data: Any
    )

data class Error (
    val status: Int,
    val message: String,
    val error: Any
    )