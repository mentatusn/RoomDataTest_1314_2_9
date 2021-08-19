package ru.geekbrains.roomdatatest1

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.util.Log
import ru.geekbrains.roomdatatest1.CityMapper.toContentValues
import ru.geekbrains.roomdatatest1.CityMapper.toEntity

class HistorySource(
    private val contentResolver: ContentResolver // Работаем с Content Provider через этот класс
) {

    private var cursor: Cursor? = null

    // Получаем запрос
    fun query() {
        cursor = contentResolver.query(HISTORY_URI, null, null, null, null)
        Log.d("mylogs",cursor.toString())
        Log.d("mylogs", DatabaseUtils.dumpCursorToString(cursor))
    }

    fun getHistory() {
        // Отправляем запрос на получение таблицы с историей запросов и получаем ответ в виде Cursor
        cursor?.let { cursor ->
            for (i in 0..cursor.count) {
                // Переходим на позицию в Cursor
                if (cursor.moveToPosition(i)) {
                    // Берём из Cursor строку
                    toEntity(cursor)
                }
            }
        }
        cursor?.close()
    }

    // Получаем данные о запросе по позиции
    fun getCityByPosition(position: Int): HistoryEntity {
        return if (cursor == null) {
            HistoryEntity()
        } else {
            cursor?.moveToPosition(position)
            toEntity(cursor!!)
        }
    }

    // Добавляем новый город
    fun insert(entity: HistoryEntity) {
        contentResolver.insert(HISTORY_URI, toContentValues(entity))
        query() // Снова открываем Cursor для повторного чтения данных
    }

    // Редактируем данные
    fun update(entity: HistoryEntity) {
        val uri: Uri = ContentUris.withAppendedId(HISTORY_URI, entity.id.toLong())
        contentResolver.update(uri, toContentValues(entity), null, null)
        query() // Снова открываем Cursor для повторного чтения данных
    }

    // Удалить запись в истории запросов
    fun delete(entity: HistoryEntity) {
        val uri: Uri = ContentUris.withAppendedId(HISTORY_URI, entity.id.toLong())
        contentResolver.delete(uri, null, null)
        query() // Снова открываем Cursor для повторного чтения данных
    }

    companion object {
        // URI для доступа к Content Provider
        private val HISTORY_URI: Uri =
            Uri.parse("content://calculator.calulation.lesson2.lesson9.EducationContentProvider/HistoryEntity")
    }
}
