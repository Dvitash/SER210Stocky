package com.example.stocky

data class InsiderSentimentItem(
    val symbol: String,
    val year: Int,
    val month: Int,
    val change: Double?,
    val mspr: Double?
)

data class InsiderSentimentResponse(
    val data: List<InsiderSentimentItem>,
    val symbol: String
) 