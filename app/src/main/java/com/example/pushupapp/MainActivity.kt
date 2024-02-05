package com.example.pushupapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

    private val entries: Stack<Entry> = Stack<Entry>()

    private var isRunning: Boolean = false


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

    }

    private fun addScore(amount: Int, player: Int) {
        if (player == 1) {
            playerOneScore += amount
            entries.push(Entry(player, amount))
        } else {
            playerTwoScore += amount
            entries.push(Entry(player, amount))
        }
        updateTotal()
    }

    private fun subtractScore(entry: Entry) {
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
        perMinuteText.text = calculatePerMinute().toString() + " / min"
    }

    private fun calculatePerMinute(): Double {
        return try {
            val elapsedTime = ((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toDouble()
            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60
            val remainder: Double = seconds / 60
            val totalTime = minutes + remainder
            Log.i("aa", totalTime.toString())
            (total / totalTime).toDouble()
        } catch (e: Exception) {
            0.0
        }

    }

}

