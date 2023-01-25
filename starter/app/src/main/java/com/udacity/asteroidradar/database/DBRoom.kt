package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteriod::class], version = 1, exportSchema = false)
abstract class DBRoom : RoomDatabase()
{

    abstract val sleepDatabaseDao : DAO

    //In kotlin instead of const we use companion object
    companion object{
        @Volatile
        private var INSTANCE : DBRoom? =null

        fun getInstance(context : Context) : DBRoom
        {
            //at a time only single thread can access
            synchronized(this)
            {
                var instance = INSTANCE
                if(instance==null)
                {
                    instance= Room.databaseBuilder(context.applicationContext,DBRoom::class.java,
                        "asteroid_details_database").fallbackToDestructiveMigration().build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}