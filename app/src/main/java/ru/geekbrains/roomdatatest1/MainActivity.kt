package ru.geekbrains.roomdatatest1

import android.content.ContentValues
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.roomdatatest1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historySource = HistorySource(contentResolver)
        historySource.query()

        binding.insert.setOnClickListener {
            historySource.insert(HistoryEntity(1000, "Багамы", 35))
        }
        binding.get.setOnClickListener {
            historySource.getHistory()
        }
        binding.getByPosition.setOnClickListener {
            historySource.getCityByPosition(1)
        }
        binding.update.setOnClickListener {
            historySource.update(HistoryEntity(1, "Гонолулу", 53))
        }
        binding.delete.setOnClickListener {
            historySource.delete(HistoryEntity(2))
        }
    }
}

data class HistoryEntity(
    val id: Int = 0,
    val city: String = "",
    val temperature: Int = 0,
)

object CityMapper {
    private const val ID = "id"
    private const val NAME = "name"
    private const val TEMPERATURE = "temperature"

    fun toEntity(cursor: Cursor): HistoryEntity {
        return HistoryEntity(
            cursor.getInt(cursor.getColumnIndex(ID)),
            cursor.getString(cursor.getColumnIndex(NAME)),
            cursor.getInt(cursor.getColumnIndex(TEMPERATURE))
        )
    }

    fun toContentValues(student: HistoryEntity): ContentValues {
        return ContentValues().apply {
            put(ID, student.id)
            put(NAME, student.city)
            put(TEMPERATURE, student.temperature)
        }
    }
}
