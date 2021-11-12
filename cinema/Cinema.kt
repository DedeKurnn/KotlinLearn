package cinema

import java.lang.IndexOutOfBoundsException

var seatingChart: MutableList<MutableList<Char>> = mutableListOf(
    mutableListOf(' ')
)

var ticketPurchased = 0
var cost = 0
var currentIncome = 0

fun main() {

    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row")
    val seats = readLine()!!.toInt()

    cinemaChart(rows, seats)

    var isInputMenu = true

    while (isInputMenu) {
        println("""
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
        """.trimIndent())

        var inputMenu: Int = readLine()!!.toInt()
        when (inputMenu) {
            1 -> displaySeating()
            2 -> {
                var isOccupied = true
                while (isOccupied) {
                    println("Enter a row number:")
                    val row = readLine()!!.toInt()
                    println("Enter a seat number in that row:")
                    val seat = readLine()!!.toInt()

                    try {
                        if (seatingChart[row][seat] == 'B') {
                            println("That ticket has already been purchased!")
                        } else {
                            seatCost(row, rows, seats)
                            seatSelection(row, seat)
                            isOccupied = false
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        println("Wrong input!")
                    }
                }


            }
            3 -> statistics(rows, seats)
            0 -> isInputMenu = false
        }
    }
}

/**
 * User enters the number of rows and seats to create a 2D matrix for the seating chart.
 */
fun cinemaChart(rows: Int, seats: Int) {
    for (i in 0..rows) {
        // Create a new mutableList
        val newRow = mutableListOf(i.digitToChar())
        // Adds the seats into the mutableList
        for (j in 1..seats) {
            // add Numbers for the first list in the matrix
            if (i == 0) seatingChart[0].add(j.digitToChar())
            // Add seats to the list.
            newRow.add(j, 'S')
        }

        // Add mutableList to the 2D matrix.
        if (i != 0) seatingChart.add(newRow)
    }
}

/**
 * Find the cost for the selected seated based on the the size of the cinema
 * and the location selected.
 */
fun seatCost(row: Int, rows: Int, seats: Int) {
    cost = if (rows * seats < 60) 10
    else {
        if (row <= rows / 2) 10
        else 8
    }
    currentIncome += cost
    println("Ticket price: $$cost")
}

/**
 * Add seat selection to the matrix.
 */
fun seatSelection(row: Int, seat: Int) {
        seatingChart[row][seat] = 'B'
        ++ticketPurchased

}

/**
 * Display the cinema seating chart.
 */
fun displaySeating() {
    println("Cinema:")
    for (i in seatingChart) {
        println(i.joinToString(" "))
    }
}

fun statistics(rows: Int, seatsInRow: Int) {
    println("Number of purchased tickets: $ticketPurchased")

    var percentage = ticketPurchased.toDouble() / (rows * seatsInRow) * 100.0
    println("Percentage: %.2f".format(percentage) + "%")
    println("Current income: $$currentIncome")
    var profit = 0
    val totalSeats = rows * seatsInRow

    if (rows * seatsInRow <= 60) {
        profit = rows * seatsInRow * 10
    } else if (rows * seatsInRow > 60) {
        if (rows % 2 == 0) {
            profit = (totalSeats / 2 * 10) + (totalSeats / 2 * 8)
        } else {
            profit = (rows / 2 * seatsInRow) * 10 + ((rows / 2) + 1) * seatsInRow * 8
        }
    }
    println("Total income: $$profit")
}

