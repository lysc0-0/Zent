package com.flowstudent.app

import android.app.Application
import com.flowstudent.app.data.local.AppDatabase

class ZentApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
