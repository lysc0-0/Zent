package com.flowstudent.app.ui.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.flowstudent.app.data.local.entity.Transaction
import com.flowstudent.app.ui.viewmodel.TransactionViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun StatsScreen(viewModel: TransactionViewModel) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("SEMANA", "MES", "AÑO")
    val transactions by viewModel.transactions.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Gráfica de Barras (MPAndroidChart)
        BarChartComposable(transactions, selectedTab)

        Spacer(modifier = Modifier.height(32.dp))

        // Resumen
        SummarySection(transactions, selectedTab)
    }
}

@Composable
fun BarChartComposable(transactions: List<Transaction>, period: Int) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                setDrawBarShadow(false)
                setTouchEnabled(false)
                
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    this.textColor = textColor
                    granularity = 1f
                }
                
                axisLeft.apply {
                    setDrawGridLines(true)
                    this.textColor = textColor
                }
                
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val entries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            
            when (period) {
                0 -> { // Semana
                    labels.addAll(listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"))
                    for (i in 0..6) entries.add(BarEntry(i.toFloat(), (100..500).random().toFloat()))
                }
                1 -> { // Mes
                    labels.addAll(listOf("Sem 1", "Sem 2", "Sem 3", "Sem 4"))
                    for (i in 0..3) entries.add(BarEntry(i.toFloat(), (500..2000).random().toFloat()))
                }
                2 -> { // Año
                    labels.addAll(listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"))
                    for (i in 0..11) entries.add(BarEntry(i.toFloat(), (2000..8000).random().toFloat()))
                }
            }

            val dataSet = BarDataSet(entries, "Gastos").apply {
                color = primaryColor
                valueTextColor = textColor
                setDrawValues(false)
            }
            
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.data = BarData(dataSet)
            chart.invalidate()
        }
    )
}

@Composable
fun SummarySection(transactions: List<Transaction>, period: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SummaryItem("Total del período", "$4,520.00")
            SummaryItem("Promedio diario", "$150.60")
            SummaryItem("Mayor gasto", "Comida")
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.secondary)
        Text(text = value, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
    }
}
