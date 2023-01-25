package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.DBRoom
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: DBRoom) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.sleepDatabaseDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun getPictureOfDay() : String
    {
        var url=""
        withContext(Dispatchers.IO){
            try {
                val picture=Network.api.getPictureOfTheDay().await()
                if(picture.mediaType.equals("image"))
                {
                    url=picture.url
                }
            }catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

        return url
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {

                val format =  SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
                val calendar= Calendar.getInstance()
                val start= format.format(calendar.getTime())
                calendar.add(Calendar.DAY_OF_YEAR,7)
                val end= format.format(calendar.getTime())

                val asteroidslist = Network.api.getAsteroids(start,end,Constants.API_KEY).await()
                val jsonObject =JSONObject(asteroidslist)
                val parsedList= parseAsteroidsJsonResult(jsonObject)
                try{
                    database.sleepDatabaseDao.clearAsteroidTableDetails()
                    database.sleepDatabaseDao.insertAll(parsedList.asDatabaseModel())
                }catch (e: java.lang.Exception)
                {
                    e.printStackTrace()
                }
            }
            catch (e : java.lang.Exception)
            {
                e.printStackTrace()
            }
        }
    }
}