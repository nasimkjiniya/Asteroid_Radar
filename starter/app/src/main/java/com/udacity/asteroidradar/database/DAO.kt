package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {

    @Insert
    fun insert(asteroid: DatabaseAsteriod)

    @Update
    fun updateNight(asteroid: DatabaseAsteriod)

    @Query("Select * FROM asteroids_table WHERE id = :key")
    fun getAsteroid(key : Long) : DatabaseAsteriod?

    @Query("Select * from asteroids_table")
    fun getAsteroids() : LiveData<List<DatabaseAsteriod>>

    @Query("DELETE FROM asteroids_table")
    fun clearAsteroidTableDetails()

    @Insert
    fun insertAll(asteroids: List<DatabaseAsteriod>)

}