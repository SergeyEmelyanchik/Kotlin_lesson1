package ru.geekbrains.kotlin_lesson1.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,/*TODO HW первичный ключ связка city+timestamp*/
    var temperature: Int,
    val feelsLike: Int,
    val icon: String
) {
}