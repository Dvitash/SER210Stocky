package com.example.stocky.models

import android.util.Log
import androidx.lifecycle.*
import com.example.stocky.QuoteResponse
import com.example.stocky.Stock
import com.example.stocky.StockRepository
import kotlinx.coroutines.launch
import com.example.stocky.InsiderSentimentResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailViewModel(private val repository: StockRepository) : ViewModel() {
    private val _stock = MutableLiveData<Stock?>()
    val stock: LiveData<Stock?> = _stock

    private val _quote = MutableLiveData<QuoteResponse?>()
    val quote: LiveData<QuoteResponse?> = _quote

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _sentiment = MutableLiveData<InsiderSentimentResponse?>()
    val sentiment: LiveData<InsiderSentimentResponse?> = _sentiment

    private val _isSentimentLoading = MutableLiveData(false)
    val isSentimentLoading: LiveData<Boolean> = _isSentimentLoading

    fun loadStockDetails(symbol: String) {
        viewModelScope.launch {
            Log.d("DetailViewModel", "Loading details for $symbol...")
            _isLoading.value = true
            try {
                Log.d("DetailViewModel", "Fetching stock details for $symbol")
                _stock.value = repository.getStockDetails(symbol)
                Log.d("DetailViewModel", "Fetching quote for $symbol")
                _quote.value = repository.getQuote(symbol)
                // Fetch insider sentiment for the last 12 months
                _isSentimentLoading.value = true
                val now = LocalDate.now()
                val from = now.minusMonths(12).format(DateTimeFormatter.ISO_DATE)
                val to = now.format(DateTimeFormatter.ISO_DATE)
                _sentiment.value = repository.getInsiderSentiment(symbol, from, to)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error loading details for $symbol", e)
            }
            _isLoading.value = false
            _isSentimentLoading.value = false
            Log.d("DetailViewModel", "Finished loading details for $symbol")
        }
    }
}
