package com.ahmety.pokedex.model

data class ErrorResponse(
    val error: ErrorModel?
): Throwable()

data class ErrorModel(
    val code: String?,
    val message: String?,
    val details: String?,
    val data: HashMap<String, String>?,
    val validationErrors: List<ValidationErrors>?
)

data class ValidationErrors(
    val message: String?,
    val members: List<String>?,
)
