package com.sudo.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sudo.data.local.database.dao.JoginguDao
import com.sudo.data.local.database.models.NotificationDB
import com.sudo.data.local.database.models.RunDB
import com.sudo.data.util.Converter

@Database(
    entities = [
        RunDB::class,
        NotificationDB::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class JoginguDatabase : RoomDatabase(){
    abstract val joginguDao: JoginguDao

    companion object{
        @Volatile
        private var INSTANCE: JoginguDatabase? = null

        fun getInstance(context: Context): JoginguDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        fun buildDatabase(context: Context): JoginguDatabase{
            return Room.databaseBuilder(
                context,
                JoginguDatabase::class.java,
                "jogingu-db"
            ).build()
        }

    }

}