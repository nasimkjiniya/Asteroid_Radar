package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

@Entity(tableName = "asteroids_table")
data class DatabaseAsteriod(

    @PrimaryKey(autoGenerate = true)
    var id: Long =0L,

    @ColumnInfo(name = "asteroid_id")
    var asteroid_id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "absolute_magnitude")
    var absolute_magnitude: Double,

    @ColumnInfo(name = "estimated_diameter_max")
    var estimated_diameter_max: Double,

    @ColumnInfo(name = "is_potentially_hazardous_asteroid")
    var is_potentially_hazardous_asteroid: Boolean,

    @ColumnInfo(name = "close_approach_date")
    var close_approach_date: String,

    @ColumnInfo(name = "relative_velocity_kilometers_per_second")
    var relative_velocity_kilometers_per_second: Double,

    @ColumnInfo(name = "miss_distance_astronomical")
    var miss_distance_astronomical: Double,

    )

fun List<DatabaseAsteriod>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid (
            id = it.asteroid_id,
            codename = it.name,
            closeApproachDate = it.close_approach_date,
            absoluteMagnitude = it.absolute_magnitude,
            estimatedDiameter = it.estimated_diameter_max,
            relativeVelocity = it.relative_velocity_kilometers_per_second,
            distanceFromEarth = it.miss_distance_astronomical,
            isPotentiallyHazardous = it.is_potentially_hazardous_asteroid)
    }
}

fun ArrayList<Asteroid>.asDatabaseModel(): List<DatabaseAsteriod> {
    return map {
        DatabaseAsteriod (
            asteroid_id = it.id,
            name = it.codename,
            close_approach_date = it.closeApproachDate,
            absolute_magnitude = it.absoluteMagnitude,
            estimated_diameter_max = it.estimatedDiameter,
            relative_velocity_kilometers_per_second = it.relativeVelocity,
            miss_distance_astronomical = it.distanceFromEarth,
            is_potentially_hazardous_asteroid = it.isPotentiallyHazardous)
    }
}