package com.example.flowbook.data.database

import androidx.room.TypeConverter
import com.example.flowbook.data.model.ExpenseCategory
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toExpenseCategory(category: String): ExpenseCategory {
        return ExpenseCategory.valueOf(category)
    }
}
