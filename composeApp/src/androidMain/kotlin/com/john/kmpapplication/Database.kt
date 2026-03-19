package com.john.kmpapplication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.john.kmpapplication.db.AppDatabase
import com.john.kmpapplication.db.AppDatabaseConstructor
import com.john.kmpapplication.util.DatabaseConstant
import org.koin.core.module.Module
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


actual fun platformModule(): Module = module  {
    single { getDatabaseBuilder(get()) }
    single<TokenStorage> { AndroidTokenStorage(get()) }
    single<DataStore<Preferences>> { createDataStore(get()) }
}