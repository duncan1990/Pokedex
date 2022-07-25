package com.ahmety.myapplication.model

data class Result(
    val count: Long,
    val next: String,
    val previous: String,
    val results: List<Pokemon>
)