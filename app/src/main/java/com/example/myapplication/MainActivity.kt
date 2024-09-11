package com.example.myapplication


import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var resetButton: Button
    private val gridSize = 8
    private lateinit var cells: Array<Array<ImageView>>

    private var camelPosition: Pair<Int, Int> = Pair(0, 0)
    private var elephantPosition: Pair<Int, Int> = Pair(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        resetButton = findViewById(R.id.resetButton)
        cells = Array(gridSize) { row ->
            Array(gridSize) { col ->
                val imageView = ImageView(this)
                imageView.setBackgroundColor(Color.WHITE)
                imageView.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(row, 1f)
                    columnSpec = GridLayout.spec(col, 1f)
                }
                gridLayout.addView(imageView)
                imageView
            }
        }

        resetButton.setOnClickListener { resetGame() }
        resetGame()
    }

    private fun resetGame() {
        // Clear previous highlights
        clearGrid()

        // Set random initial positions for camel and elephant
        camelPosition = getRandomPosition()
        elephantPosition = getRandomPosition()

        // Ensure they don't start at the same position
        while (camelPosition == elephantPosition) {
            elephantPosition = getRandomPosition()
        }

        // Highlight predicted moves
        highlightMoves()
    }

    private fun clearGrid() {
        for (row in cells) {
            for (cell in row) {
                cell.setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun getRandomPosition(): Pair<Int, Int> {
        val row = Random.nextInt(gridSize)
        val col = Random.nextInt(gridSize)
        return Pair(row, col)
    }

    private fun highlightMoves() {
        val camelMoves = mutableListOf<Pair<Int, Int>>()
        val elephantMoves = mutableListOf<Pair<Int, Int>>()

        // Highlight camel's diagonal moves in all four directions
        val (camelRow, camelCol) = camelPosition

        // Top-left to bottom-right
        for (i in 0 until gridSize) {
            val diagonalRight = camelRow + i
            val diagonalLeft = camelRow - i

            if (diagonalRight in 0 until gridSize && camelCol + i in 0 until gridSize) {
                camelMoves.add(Pair(diagonalRight, camelCol + i))
            }
            if (diagonalLeft in 0 until gridSize && camelCol - i in 0 until gridSize) {
                camelMoves.add(Pair(diagonalLeft, camelCol - i))
            }
        }

        // Bottom-left to top-right and Top-right to bottom-left
        for (i in 0 until gridSize) {
            val diagonalBottomRight = camelRow + i
            val diagonalTopLeft = camelRow - i

            if (diagonalTopLeft in 0 until gridSize && camelCol + i in 0 until gridSize) {
                camelMoves.add(Pair(diagonalTopLeft, camelCol + i))
            }
            if (diagonalBottomRight in 0 until gridSize && camelCol - i in 0 until gridSize) {
                camelMoves.add(Pair(diagonalBottomRight, camelCol - i))
            }
        }

        // Highlight elephant's horizontal and vertical moves in yellow
        val (elephantRow, elephantCol) = elephantPosition
        for (i in 0 until gridSize) {
            elephantMoves.add(Pair(elephantRow, i))  // Horizontal
            elephantMoves.add(Pair(i, elephantCol))  // Vertical
        }

        // Highlight camel and elephant moves
        for (move in camelMoves) {
            cells[move.first][move.second].setBackgroundColor(Color.BLUE)
        }
        for (move in elephantMoves) {
            cells[move.first][move.second].setBackgroundColor(Color.YELLOW)
        }

        // Check for collisions and highlight them in red
        for (camelMove in camelMoves) {
            if (camelMove in elephantMoves) {
                cells[camelMove.first][camelMove.second].setBackgroundColor(Color.RED)
            }
        }

        // Place camel and elephant on the grid
        cells[camelRow][camelCol].setBackgroundColor(Color.BLUE)
        cells[elephantRow][elephantCol].setBackgroundColor(Color.YELLOW)
    }

}

