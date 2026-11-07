package com.example.stockmarket

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var tvSelectedStock: TextView
    private lateinit var tvSelectedPrice: TextView
    private lateinit var rvStockTrades: RecyclerView
    private lateinit var stockAdapter: StockAdapter

    private val stocks = mutableListOf(
        StockTrade("AAPL", "Apple Inc.", 178.50, 1.25, mutableListOf(170f, 172f, 169f, 174f, 173f, 178.5f)),
        StockTrade("TSLA", "Tesla Inc.", 242.20, -0.85, mutableListOf(250f, 248f, 245f, 240f, 241f, 242.2f)),
        StockTrade("NVDA", "NVIDIA Corp.", 460.10, 3.40, mutableListOf(440f, 445f, 450f, 452f, 458f, 460.1f)),
        StockTrade("AMZN", "Amazon.com", 132.40, 0.45, mutableListOf(128f, 129f, 130f, 131f, 131.5f, 132.4f)),
        StockTrade("GOOGL", "Alphabet Inc.", 138.80, -1.10, mutableListOf(142f, 141f, 140f, 139f, 138.5f, 138.8f))
    )

    private var selectedStock: StockTrade = stocks[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        lineChart = findViewById(R.id.lineChart)
        tvSelectedStock = findViewById(R.id.tvSelectedStock)
        tvSelectedPrice = findViewById(R.id.tvSelectedPrice)
        rvStockTrades = findViewById(R.id.rvStockTrades)

        rvStockTrades.layoutManager = LinearLayoutManager(this)
        stockAdapter = StockAdapter(stocks) { clickedStock ->
            selectedStock = clickedStock
            updateSelectedStockUI()
        }
        rvStockTrades.adapter = stockAdapter

        updateSelectedStockUI()
        startLiveStockUpdates()
    }

    private fun updateSelectedStockUI() {
        tvSelectedStock.text = "${selectedStock.symbol} - ${selectedStock.companyName}"
        tvSelectedPrice.text = String.format("$%.2f", selectedStock.currentPrice)
        renderChart(selectedStock.priceHistory)
    }

    private fun renderChart(prices: List<Float>) {
        val entries = prices.mapIndexed { index, price -> Entry(index.toFloat(), price) }
        val dataSet = LineDataSet(entries, "").apply {
            color = Color.parseColor("#00E676")
            valueTextColor = Color.TRANSPARENT // Hides price text above dots
            lineWidth = 2.5f
            setCircleColor(Color.parseColor("#00E676"))
            circleRadius = 3f
            setDrawCircleHole(false)
            setDrawFilled(true)
            fillColor = Color.parseColor("#1B5E20")
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        lineChart.data = LineData(dataSet)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // Hides legend box

        // Clean X-Axis
        lineChart.xAxis.isEnabled = false // Hides top X-axis numbers

        // Clean Y-Axis
        lineChart.axisLeft.apply {
            textColor = Color.LTGRAY
            setDrawGridLines(true)
            gridColor = Color.parseColor("#333333")
        }
        lineChart.axisRight.isEnabled = false

        // Padding to prevent edge text clipping
        lineChart.setExtraOffsets(12f, 12f, 12f, 12f)
        lineChart.invalidate()
    }

    private fun startLiveStockUpdates() {
        lifecycleScope.launch {
            while (true) {
                delay(2000) // Ticks every 2 seconds
                stocks.forEach { stock ->
                    val fluctuation = (Random.nextDouble(-1.5, 1.5))
                    stock.currentPrice = (stock.currentPrice + fluctuation).coerceAtLeast(1.0)
                    stock.changePercent += (fluctuation / stock.currentPrice) * 100
                    stock.priceHistory.add(stock.currentPrice.toFloat())
                    if (stock.priceHistory.size > 20) stock.priceHistory.removeAt(0)
                }
                stockAdapter.notifyDataSetChanged()
                updateSelectedStockUI()
            }
        }
    }
}