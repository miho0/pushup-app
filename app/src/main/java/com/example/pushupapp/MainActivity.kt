package com.example.pushupapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Stack

class MainActivity : AppCompatActivity() {
    private lateinit var chronometer: Chronometer
    private lateinit var playerOneScoreText: TextView
    private lateinit var playerTwoScoreText: TextView
    private lateinit var totalText: TextView
    private lateinit var perMinuteText: TextView

    private var playerOneScore: Int = 0
    private var playerTwoScore: Int = 0
    private var total: Int = 0

    private val entries: Stack<PushupEntry> = Stack<PushupEntry>()

    private var isRunning: Boolean = false

    private val handler: Handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById(R.id.chronometer)

        playerOneScoreText = findViewById(R.id.playerOneAmount)
        playerTwoScoreText = findViewById(R.id.playerTwoAmount)
        totalText = findViewById(R.id.totalAmount)
        perMinuteText = findViewById(R.id.perMinuteCalculation)

        val playerOneAddFive: Button = findViewById(R.id.plusFivePlayerOne)
        val playerOneAddTen: Button = findViewById(R.id.plusTenPlayerOne)
        val playerOneAddFifteen: Button = findViewById(R.id.plusFifteenPlayerOne)
        val playerOneAddTwenty: Button = findViewById(R.id.plusTwentyPlayerOne)

        val playerOneCustomAmount: EditText = findViewById(R.id.customAmountPlayerOne)
        val playerOneCustomAmountButton: Button = findViewById(R.id.customAmountPlayerOneButton)

        val playerTwoAddFive: Button = findViewById(R.id.plusFivePlayerTwo)
        val playerTwoAddTen: Button = findViewById(R.id.plusTenPlayerTwo)
        val playerTwoAddFifteen: Button = findViewById(R.id.plusFifteenPlayerTwo)
        val playerTwoAddTwenty: Button = findViewById(R.id.plusTwentyPlayerTwo)

        val playerTwoCustomAmount: EditText = findViewById(R.id.customAmountPlayerTwo)
        val playerTwoCustomAmountButton: Button = findViewById(R.id.customAmountPlayerTwoButton)

        playerOneAddFive.setOnClickListener { addScore(5, 1) }
        playerOneAddTen.setOnClickListener { addScore(10, 1) }
        playerOneAddFifteen.setOnClickListener { addScore(15, 1) }
        playerOneAddTwenty.setOnClickListener { addScore(20, 1) }

        playerOneCustomAmountButton.setOnClickListener {
            try {
                val amount: Int = playerOneCustomAmount.text.toString().toInt()
                addScore(amount, 1)
            } catch (e: Exception) {
                Toast.makeText(this, "Please input a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        playerTwoAddFive.setOnClickListener { addScore(5, 2) }
        playerTwoAddTen.setOnClickListener { addScore(10, 2) }
        playerTwoAddFifteen.setOnClickListener { addScore(15, 2) }
        playerTwoAddTwenty.setOnClickListener { addScore(20, 2) }

        playerTwoCustomAmountButton.setOnClickListener {
            try {
                val amount: Int = playerTwoCustomAmount.text.toString().toInt()
                addScore(amount, 2)
            } catch (e: Exception) {
                Toast.makeText(this, "Please input a valid number", Toast.LENGTH_SHORT).show()
            }
        }

        chronometer.format = "%s"

        val startButton: Button = findViewById(R.id.start)

        startButton.setOnClickListener {
            if (isRunning) {
                chronometer.stop()
                startButton.text = "Start"
            } else {
                playerOneScore = 0
                playerTwoScore = 0
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                startButton.text = "End"
                updateTotal()
            }
            isRunning = !isRunning
        }

        val undoButton: Button = findViewById(R.id.undo)

        undoButton.setOnClickListener {
            try {
                val entry = entries.pop()
                Log.d("abc", entry.amount.toString())
                Log.d("abc", entry.player.toString())
                subtractScore(entry)
            } catch (e: Exception) {
                Toast.makeText(this, "Already undone everything", Toast.LENGTH_SHORT).show()
            }
        }

        val statsButton: Button = findViewById(R.id.stats)

        statsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            intent.putExtra("entries", entries.toTypedArray())
            startActivity(intent)
        }

        runnable = object: Runnable {
            override fun run() {
                updatePerMinuteResult()
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(runnable)

    }

    private fun addScore(amount: Int, player: Int) {
        val elapsedTime = ((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toDouble()
        if (player == 1) {
            playerOneScore += amount
            entries.push(PushupEntry(player, amount, elapsedTime))
        } else {
            playerTwoScore += amount
            entries.push(PushupEntry(player, amount, elapsedTime))
        }
        updateTotal()
    }

    private fun subtractScore(entry: PushupEntry) {
        val player = entry.player
        val amount = entry.amount
        if (player == 1) {
            playerOneScore -= amount
        } else {
            playerTwoScore -= amount
        }
        updateTotal()
    }

    private fun updateTotal() {
        playerOneScoreText.text = playerOneScore.toString()
        playerTwoScoreText.text = playerTwoScore.toString()
        total = playerOneScore + playerTwoScore
        totalText.text = total.toString()
    }

    private fun updatePerMinuteResult() {
        if (isRunning) {
            var result = BigDecimal(0.0)
            try {
                val elapsedTime = ((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toDouble()
                result = BigDecimal((total / elapsedTime)*60).setScale(4, RoundingMode.HALF_UP)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            perMinuteText.text = "$result / min"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

}

