package com.john.kmpapplication

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.john.kmpapplication.db.AppDatabase
import com.john.kmpapplication.db.AppDatabaseConstructor
import com.john.kmpapplication.util.DatabaseConstant
import org.koin.dsl.module

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DatabaseConstant.APP_DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
        factory = { AppDatabaseConstructor.initialize() }
    )
}


val platformModule = module {
    single { getDatabaseBuilder(get()) }
}