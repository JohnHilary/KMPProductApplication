package com.john.kmpapplication

import androidx.room.Room
import androidx.room.RoomDatabase
import com.john.kmpapplication.db.AppDatabase
import com.john.kmpapplication.db.AppDatabaseConstructor
import com.john.kmpapplication.util.DatabaseConstant
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val path = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )?.path ?: ""

    return Room.databaseBuilder<AppDatabase>(
        name = "$path/${DatabaseConstant.APP_DATABASE_NAME}",
        factory = { AppDatabaseConstructor.initialize() }
    )
}
actual fun platformModule(): Module = module {
    single {    getDatabaseBuilder() }
    single<TokenStorage> { IosTokenStorage() }
}