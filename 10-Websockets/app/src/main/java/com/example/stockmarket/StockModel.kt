package com.example.stockmarket

data class StockTrade(
    val symbol: String,
    val companyName: String,
    var currentPrice: Double,
    var changePercent: Double,
    val priceHistory: MutableList<Float>
)