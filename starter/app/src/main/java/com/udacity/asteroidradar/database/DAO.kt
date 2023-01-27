package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface DAO {

    @Insert
    fun insert(asteroid: DatabaseAsteriod)

    @Query("Select * FROM asteroids_table WHERE id = :key")
    fun getAsteroid(key : Long) : DatabaseAsteriod?

    @Query("Select * from asteroids_table")
    fun getAsteroids() : LiveData<List<DatabaseAsteriod>>

    @Query("DELETE FROM asteroids_table")
    fun clearAsteroidTableDetails()

    @Query("Select * FROM asteroids_table WHERE close_approach_date = :date")
    fun getTodayAsteroids(date : String) : LiveData<List<DatabaseAsteriod>>

    @Insert
    fun insertAll(asteroids: List<DatabaseAsteriod>)

}