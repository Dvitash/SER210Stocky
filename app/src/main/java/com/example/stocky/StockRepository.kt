package com.example.stocky

import android.util.Log

class StockRepository(private val watchlistDao: WatchlistDao) {
    private val api = ApiInterface.create()

    suspend fun searchStocks(query: String): List<Stock>? {
        val response = api.searchStocks(query)
        return if (response.isSuccessful) {
            response.body()?.result
        } else {
            null
        }
    }

    suspend fun getStockDetails(symbol: String): Stock? {
        return try {
            val response = api.searchStocks(symbol)

            if (!response.isSuccessful) {
                Log.e("StockRepository", "API search failed for $symbol: ${response.errorBody()?.string()}")
                return null
            }

            val results = response.body()?.result
            val matchedStock = results?.firstOrNull {
                it.symbol.equals(symbol, ignoreCase = true)
            }

            Log.d("StockRepository", "Stock match for $symbol: $matchedStock")

            matchedStock
        } catch (e: Exception) {
            Log.e("StockRepository", "Exception during getStockDetails($symbol)", e)
            null
        }
    }

    suspend fun getQuote(symbol: String): QuoteResponse? {
        return try {
            val response = api.getQuote(symbol)

            if (response.isSuccessful) {
                val quote = response.body()
                Log.d("StockRepository", "Quote for $symbol: $quote")
                quote
            } else {
                Log.e("StockRepository", "Quote failed for $symbol: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("StockRepository", "Exception during getQuote($symbol)", e)
            null
        }
    }

    // watchlist operations
    suspend fun addStockToWatchlist(symbol: String) {
        val entry = WatchlistEntry(symbol = symbol, addedAt = System.currentTimeMillis())
        watchlistDao.insertEntry(entry)
    }

    suspend fun removeStockFromWatchlist(symbol: String) {
        watchlistDao.deleteBySymbol(symbol)
    }

    suspend fun getAllWatchlistEntries(): List<WatchlistEntry> {
        val entries = watchlistDao.getAllEntries()
        return entries
    }

    suspend fun getInsiderSentiment(symbol: String, from: String, to: String): InsiderSentimentResponse? {
        return try {
            val response = api.getInsiderSentiment(symbol, from, to)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("StockRepository", "Insider sentiment failed for $symbol: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("StockRepository", "Exception during getInsiderSentiment($symbol)", e)
            null
        }
    }

}
