package com.flowstudent.app.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.flowstudent.app.data.local.dao.TransactionDao
import com.flowstudent.app.data.local.entity.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(application: Application, private val transactionDao: TransactionDao) : AndroidViewModel(application) {
    
    private val prefs = application.getSharedPreferences("zent_prefs", Context.MODE_PRIVATE)

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance.asStateFlow()

    private val _monthlyIncome = MutableStateFlow(0.0)
    val monthlyIncome: StateFlow<Double> = _monthlyIncome.asStateFlow()

    private val _monthlyExpense = MutableStateFlow(0.0)
    val monthlyExpense: StateFlow<Double> = _monthlyExpense.asStateFlow()

    private val _isSurveyCompleted = MutableStateFlow(prefs.getBoolean("encuestaCompletada", false))
    val isSurveyCompleted: StateFlow<Boolean> = _isSurveyCompleted.asStateFlow()

    init {
        loadTransactions()
        calculateMonthlyBalance()
    }

    fun setSurveyCompleted() {
        prefs.edit().putBoolean("encuestaCompletada", true).apply()
        _isSurveyCompleted.value = true
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionDao.getAllTransactions().collect { list ->
                _transactions.value = list
            }
        }
    }

    private fun calculateMonthlyBalance() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        
        val endTime = System.currentTimeMillis()

        viewModelScope.launch {
            transactionDao.getTotalIncomeInRange(startTime, endTime).collect { income ->
                _monthlyIncome.value = income ?: 0.0
                updateBalance()
            }
        }
        viewModelScope.launch {
            transactionDao.getTotalExpenseInRange(startTime, endTime).collect { expense ->
                _monthlyExpense.value = expense ?: 0.0
                updateBalance()
            }
        }
    }

    private fun updateBalance() {
        _totalBalance.value = _monthlyIncome.value - _monthlyExpense.value
    }

    fun addTransaction(amount: Double, category: String, note: String, type: String) {
        viewModelScope.launch {
            val transaction = Transaction(
                amount = amount,
                category = category,
                timestamp = System.currentTimeMillis(),
                note = note,
                type = type
            )
            transactionDao.insertTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.deleteTransaction(transaction)
        }
    }

    fun searchTransactions(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                loadTransactions()
            } else {
                transactionDao.searchTransactions(query).collect { list ->
                    _transactions.value = list
                }
            }
        }
    }
}
