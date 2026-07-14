package com.flowstudent.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flowstudent.app.data.local.dao.TransactionDao

class TransactionViewModelFactory(
    private val application: Application,
    private val transactionDao: TransactionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(application, transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
