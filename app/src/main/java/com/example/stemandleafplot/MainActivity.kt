package com.example.stemandleafplot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StemAndLeafPlotApp()
        }
    }
}

@Composable
fun StemAndLeafPlotApp() {
    var limit by remember { mutableStateOf(110) }
    val primes = remember(limit) { eratosthenesSieve(limit) }
    val stemLeafData = remember(primes) { createStemLeafMap(primes) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = "Stem and Leaf Plot - Prime Numbers",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                // Prime numbers
                Text(
                    text = "Prime numbers up to $limit:",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = primes.joinToString(", "),
                        modifier = Modifier.padding(8.dp),
                        fontSize = 12.sp
                    )
                }

                // Stem and Leaf Plot title
                Text(
                    text = "Stem and Leaf Plot:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Stem and leaf plot
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(stemLeafData.entries.sortedBy { it.key }) { entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${entry.key} |",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(40.dp)
                                )
                                Text(
                                    text = entry.value.sorted().joinToString(" "),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Button to change limit
                Button(
                    onClick = { limit = if (limit == 110) 150 else 110 },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Generate with Limit ${if (limit == 110) 150 else 110}")
                }
            }
        }
    }
}

fun eratosthenesSieve(limit: Int): List<Int> {
    // Generates primes up to limit using the Sieve of Eratosthenes
    val prime = BooleanArray(limit + 1) { true }
    prime[0] = false
    prime[1] = false

    for (i in 2..sqrt(limit.toDouble()).toInt()) {
        if (prime[i]) {
            var multiple = i * i
            while (multiple <= limit) {
                prime[multiple] = false
                multiple += i
            }
        }
    }

    // Collect all prime numbers
    return prime.mapIndexed { index, isPrime -> if (isPrime) index else null }
        .filterNotNull()
}

fun createStemLeafMap(data: List<Int>): Map<Int, List<Int>> {
    // Map to store stems and their corresponding leaves
    val stemLeafMap = mutableMapOf<Int, MutableList<Int>>()

    // Process each number in the data
    for (num in data) {
        val stem = num / 10
        val leaf = num % 10

        // Add leaf to the corresponding stem
        if (stem in stemLeafMap) {
            stemLeafMap[stem]?.add(leaf)
        } else {
            stemLeafMap[stem] = mutableListOf(leaf)
        }
    }

    return stemLeafMap
}