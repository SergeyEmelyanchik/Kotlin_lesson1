package ru.geekbrains.kotlin_lesson1.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    var city: City = getDefaultCity(),
    var temperature: Int = 0,
    val feelsLike: Int = 0,
    val icon: String = "bkn_n"
) : Parcelable

fun getDefaultCity() = City("Москва", 55.75, 37.61)

@Parcelize
data class City(
    var cityName: String,
    val lat: Double,
    val lon: Double
) : Parcelable

fun getWorldCities(): List<Weather> = listOf(
    Weather(City("Лондон", 51.5085300, -0.1257400), 1, 2),
    Weather(City("Токио", 35.6895000, 139.6917100), 3, 4),
    Weather(City("Париж", 48.8534100, 2.3488000), 5, 6),
    Weather(City("Берлин", 52.52000659999999, 13.404953999999975), 7, 8),
    Weather(City("Рим", 41.9027835, 12.496365500000024), 9, 10),
    Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
    Weather(City("Стамбул", 41.0082376, 28.97835889999999), 13, 14),
    Weather(City("Вашингтон", 38.9071923, -77.03687070000001), 15, 16),
    Weather(City("Киев", 50.4501, 30.523400000000038), 17, 18),
    Weather(City("Пекин", 39.90419989999999, 116.40739630000007), 19, 20)
)

fun getRussianCities(): List<Weather> = listOf(
    Weather(City("Минск", 53.90453979999999, 27.561524400000053), 11, 12),
    Weather(City("Брест", 59.9342802, 30.335098600000038), 3, 3),
    Weather(City("Витебск", 55.00835259999999, 82.93573270000002), 5, 6),
    Weather(City("Гомель", 56.83892609999999, 60.60570250000001), 7, 8),
    Weather(City("Гродно", 56.2965039, 43.936059), 9, 10),
    Weather(City("Могилев", 55.8304307, 49.06608060000008), 11, 12),
    Weather(City("Гомель", 56.83892609999999, 60.60570250000001), 7, 8),
    Weather(City("Гродно", 56.2965039, 43.936059), 9, 10),
    Weather(City("Могилев", 55.8304307, 49.06608060000008), 11, 12),
)