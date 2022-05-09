package ru.geekbrains.kotlin_lesson1

import android.app.Application
import androidx.room.Room
import ru.geekbrains.kotlin_lesson1.domain.room.HistoryDAO
import ru.geekbrains.kotlin_lesson1.domain.room.MyDB

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var dataBase: MyDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDAO(): HistoryDAO {
            if (dataBase == null) {
                if (appContext != null) {
                    dataBase = Room
                        .databaseBuilder(appContext!!, MyDB::class.java, "history_response_table")
                        .build()
                } else {
                    throw IllegalStateException("Что-то пошло не так! appContext пуст")
                }
            }
            return dataBase!!.historyDAO()
        }
    }
}