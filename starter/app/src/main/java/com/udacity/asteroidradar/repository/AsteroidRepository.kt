package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.database.DBRoom
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.DatabaseAsteriod
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: DBRoom) {

    private val calendar =Calendar.getInstance()
    private val formatter =  SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)

    val sortingMethod = MutableLiveData<String>()
    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(sortingMethod) {
        when(it)
        {
            "TODAY"-> Transformations.map(database.sleepDatabaseDao.getTodayAsteroids(formatter.format(calendar.time))){ data->
                data.asDomainModel()
            }
            else->  Transformations.map(database.sleepDatabaseDao.getAsteroids()){ data->
                data.asDomainModel()
            }
        }
    }

    suspend fun getPictureOfDay() : PictureOfDay
    {
        var pictureOfDay = PictureOfDay("","","")
        withContext(Dispatchers.IO){
            try {
                val picture=Network.api.getPictureOfTheDay().await()
                if(picture!=null)
                {
                    pictureOfDay=PictureOfDay(picture.mediaType,picture.title,picture.url)
                }
            }catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

        return pictureOfDay
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {

                val format =  SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
                val calendar= Calendar.getInstance()
                val start= format.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR,7)
                val end= format.format(calendar.time)

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