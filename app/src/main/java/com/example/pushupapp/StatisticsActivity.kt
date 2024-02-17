package com.example.pushupapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statisctics)

        val pushupEntries = intent.getParcelableArrayExtra("entries")?.map { it as PushupEntry }
        val lineChartBoth = findViewById<LineChart>(R.id.lineChart1)
        val lineChartPlayerOne = findViewById<LineChart>(R.id.lineChart2)
        val lineChartPlayerTwo = findViewById<LineChart>(R.id.lineChart3)

        val entriesBoth = ArrayList<Entry>()
        val entriesPlayerOne = ArrayList<Entry>()
        val entriesPlayerTwo = ArrayList<Entry>()

        entriesBoth.add(Entry(0f, 0f))
        entriesPlayerOne.add(Entry(0f, 0f))
        entriesPlayerTwo.add(Entry(0f, 0f))

        var total: Int = 0
        var totalPlayerOne: Int = 0
        var totalPlayerTwo: Int = 0
        pushupEntries?.map {
            total += it.amount
            entriesBoth.add(Entry(it.time.toFloat(), total.toFloat()))
            if (it.player == 1) {
                totalPlayerOne += it.amount
                entriesPlayerOne.add(Entry(it.time.toFloat(), totalPlayerOne.toFloat()))
            }

            if (it.player == 2) {
                totalPlayerTwo += it.amount
                entriesPlayerTwo.add(Entry(it.time.toFloat(), totalPlayerTwo.toFloat()))
            }
        }

        val lineDataBoth = LineData(LineDataSet(entriesBoth, "Both").apply {
            color = Color.RED
        })

        val lineDataPlayerOne = LineData(LineDataSet(entriesPlayerOne, "Avtist 1").apply {
            color = Color.BLUE
        })

        val lineDataPlayerTwo = LineData(LineDataSet(entriesPlayerTwo, "Avtist 2").apply {
            color = Color.GREEN
        })

        // Set LineData to the LineChart views
        lineChartBoth.data = lineDataBoth
        lineChartPlayerOne.data = lineDataPlayerOne
        lineChartPlayerTwo.data = lineDataPlayerTwo

        // Refresh the charts
        lineChartBoth.invalidate()
        lineChartPlayerOne.invalidate()
        lineChartPlayerTwo.invalidate()
    }
}

// chart: add the totals together