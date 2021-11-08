import java.util.*

class OutBoundsMoveException : Exception()
class OccupiedCoordinateException :
    Exception()

class Coordinates(x: Int, y: Int) {
    val x: Int
    val y: Int

    init {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw OutBoundsMoveException()
        }
        this.x = x
        this.y = y
    }
}

object Main {
    fun getRows(gameState: Array<String?>?): Array<Array<String?>> {
        val rows = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            System.arraycopy(gameState, i * 3, rows[i], 0, 3)
        }
        return rows
    }

    fun getCols(gameState: Array<String?>): Array<Array<String?>> {
        val cols = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            for (j in 0..2) {
                cols[i][j] = gameState[i + 3 * j]
            }
        }
        return cols
    }

    fun isLineOf(player: String, line: Array<String?>?): Boolean {
        val expectedLine = arrayOf(player, player, player)
        return Arrays.equals(line, expectedLine)
    }

    fun isLinesOf(player: String, lines: Array<Array<String?>>): Boolean {
        for (line in lines) {
            if (isLineOf(player, line)) {
                return true
            }
        }
        return false
    }

    fun isRowOf(player: String, gameState: Array<String?>?): Boolean {
        return isLinesOf(player, getRows(gameState))
    }

    fun isColOf(player: String, gameState: Array<String?>): Boolean {
        return isLinesOf(player, getCols(gameState))
    }

    fun isRowOfXs(gameState: Array<String?>?): Boolean {
        return isRowOf("X", gameState)
    }

    fun isRowOfOs(gameState: Array<String?>?): Boolean {
        return isRowOf("O", gameState)
    }

    fun isColOfXs(gameState: Array<String?>): Boolean {
        return isColOf("X", gameState)
    }

    fun isColOfOs(gameState: Array<String?>): Boolean {
        return isColOf("O", gameState)
    }

    fun getRightDiagonal(gameState: Array<String?>): Array<String?> {
        return arrayOf(gameState[0], gameState[4], gameState[8])
    }

    fun getLeftDiagonal(gameState: Array<String?>): Array<String?> {
        return arrayOf(gameState[2], gameState[4], gameState[6])
    }

    fun isRightDiagonalOf(player: String, gameState: Array<String?>): Boolean {
        return isLineOf(player, getRightDiagonal(gameState))
    }

    fun isLeftDiagonalOf(player: String, gameState: Array<String?>): Boolean {
        return isLineOf(player, getLeftDiagonal(gameState))
    }

    fun isRightDiagonalOfXs(gameState: Array<String?>): Boolean {
        return isRightDiagonalOf("X", gameState)
    }

    fun isRightDiagonalOfOs(gameState: Array<String?>): Boolean {
        return isRightDiagonalOf("O", gameState)
    }

    fun isLeftDiagonalOfXs(gameState: Array<String?>): Boolean {
        return isLeftDiagonalOf("X", gameState)
    }

    fun isLeftDiagonalOfOs(gameState: Array<String?>): Boolean {
        return isLeftDiagonalOf("O", gameState)
    }

    fun isDiagonalOfXs(gameState: Array<String?>): Boolean {
        return isRightDiagonalOfXs(gameState) || isLeftDiagonalOfXs(gameState)
    }

    fun isDiagonalOfOs(gameState: Array<String?>): Boolean {
        return isRightDiagonalOfOs(gameState) || isLeftDiagonalOfOs(gameState)
    }

    fun isXWins(gameState: Array<String?>): Boolean {
        return isRowOfXs(gameState) || isColOfXs(gameState) || isDiagonalOfXs(gameState)
    }

    fun isOWins(gameState: Array<String?>): Boolean {
        return isRowOfOs(gameState) || isColOfOs(gameState) || isDiagonalOfOs(gameState)
    }

    fun movesAvailable(gameState: Array<String?>): Boolean {
        return Arrays.asList(*gameState).contains(" ")
    }

    fun hasNoWinner(gameState: Array<String?>): Boolean {
        return !isXWins(gameState) && !isOWins(gameState)
    }

    fun count(player: String, gameState: Array<String?>): Int {
        var count = 0
        for (move in gameState) {
            if (move == player) {
                count++
            }
        }
        return count
    }

    fun wrongNumberOfMoves(gameState: Array<String?>): Boolean {
        val movesDiff = count("X", gameState) - count("O", gameState)
        return movesDiff < -1 || movesDiff > 1
    }

    fun isDraw(gameState: Array<String?>): Boolean {
        return hasNoWinner(gameState) && !movesAvailable(gameState)
    }

    fun isNotFinished(gameState: Array<String?>): Boolean {
        return hasNoWinner(gameState) && movesAvailable(gameState)
    }

    fun isImpossible(gameState: Array<String?>): Boolean {
        return isXWins(gameState) && isOWins(gameState) || wrongNumberOfMoves(gameState)
    }

    fun printBoard(gameState: Array<String?>) {
        println("---------")
        for (i in 0..2) {
            print("| ")
            for (j in 0..2) {
                System.out.printf("%s ", gameState[3 * i + j])
            }
            println("|")
        }
        println("---------")
    }

    fun getStatus(gameState: Array<String?>): String {
        var status = "Unknown game state"
        if (isImpossible(gameState)) {
            status = "Impossible"
        } else if (isNotFinished(gameState)) {
            status = "Game not finished"
        } else if (isXWins(gameState)) {
            status = "X wins"
        } else if (isOWins(gameState)) {
            status = "O wins"
        } else if (isDraw(gameState)) {
            status = "Draw"
        }
        return status
    }

    @Throws(OutBoundsMoveException::class)
    fun readUserMove(): Coordinates {
        val scanner = Scanner(System.`in`)
        val y = scanner.nextInt() - 1
        val x = scanner.nextInt() - 1
        return Coordinates(x, y)
    }

    @Throws(OccupiedCoordinateException::class)
    fun makeUserMove(coordinates: Coordinates, gameState: Array<String?>, player: String?) {
        val target = gameState[coordinates.y * 3 + coordinates.x]
        if (target == "X" || target == "O") {
            throw OccupiedCoordinateException()
        }
        gameState[coordinates.y * 3 + coordinates.x] = player
    }

    fun processUserMove(gameState: Array<String?>, player: String?) {
        try {
            print("Enter the coordinates: ")
            val coordinates = readUserMove()
            makeUserMove(coordinates, gameState, player)
        } catch (e: OutBoundsMoveException) {
            println("Coordinates should be from 1 to 3!")
            processUserMove(gameState, player)
        } catch (e: OccupiedCoordinateException) {
            println("This cell is occupied! Choose another one!")
            processUserMove(gameState, player)
        } catch (e: InputMismatchException) {
            println("You should enter numbers!")
            processUserMove(gameState, player)
        }
    }

    fun emptyBoard(): Array<String?> {
        val board = arrayOfNulls<String>(9)
        Arrays.fill(board, " ")
        return board
    }

    fun play() {
        val gameState = emptyBoard()
        var gameStatus = getStatus(gameState)
        var nextPlayer = "X"
        while (gameStatus == "Game not finished") {
            printBoard(gameState)
            processUserMove(gameState, nextPlayer)
            gameStatus = getStatus(gameState)
            nextPlayer = if (nextPlayer == "X") "O" else "X"
        }
        printBoard(gameState)
        println(gameStatus)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        play()
    }
}