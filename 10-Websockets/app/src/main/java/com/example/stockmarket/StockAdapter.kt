package com.example.stockmarket

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StockAdapter(
    private val stockList: List<StockTrade>,
    private val onStockClick: (StockTrade) -> Unit
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSymbol: TextView = view.findViewById(R.id.tvStockSymbol)
        val tvCompany: TextView = view.findViewById(R.id.tvCompanyName)
        val tvPrice: TextView = view.findViewById(R.id.tvStockPrice)
        val tvChange: TextView = view.findViewById(R.id.tvStockChange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_trade, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stockList[position]
        holder.tvSymbol.text = stock.symbol
        holder.tvCompany.text = stock.companyName
        holder.tvPrice.text = String.format("$%.2f", stock.currentPrice)

        val changeSign = if (stock.changePercent >= 0) "+" else ""
        holder.tvChange.text = String.format("%s%.2f%%", changeSign, stock.changePercent)

        if (stock.changePercent >= 0) {
            holder.tvChange.setTextColor(Color.parseColor("#00E676"))
        } else {
            holder.tvChange.setTextColor(Color.parseColor("#FF5252"))
        }

        holder.itemView.setOnClickListener { onStockClick(stock) }
    }

    override fun getItemCount(): Int = stockList.size
}