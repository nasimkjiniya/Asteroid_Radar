package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.DBRoom
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val database = DBRoom.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _viewWeekAsteroids = MutableLiveData<Boolean>()
    val viewWeekAsteroids
        get() = _viewWeekAsteroids

    private val _viewTodayAsteroids = MutableLiveData<Boolean>()
    val viewTodayAsteroids
        get() = _viewTodayAsteroids

    private val _pictureUrl = MutableLiveData<String?>()
    val pictureUrl
        get() = _pictureUrl

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay
        get() = _pictureOfDay


    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun onNavigated() {
        _navigateToAsteroid.value = null
    }


    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {

        _viewTodayAsteroids.value=false
        _viewWeekAsteroids.value=false


        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }

        asteroidRepository.sortingMethod.value="WEEK"
        viewModelScope.launch {
            pictureOfDay.value=asteroidRepository.getPictureOfDay()
            if(!pictureOfDay.value!!.url.equals("") && pictureOfDay.value!!.mediaType == "image")
            {
                _pictureUrl.value= pictureOfDay.value!!.url
            }
        }
    }

    val asteroidList = asteroidRepository.asteroids

    fun viewWeekAsteroids()
    {
        asteroidRepository.sortingMethod.value="WEEK"
    }

    fun viewTodayAsteroids()
    {
        asteroidRepository.sortingMethod.value="TODAY"
    }

    fun doneViewWeekAsteroids()
    {
        _viewWeekAsteroids.value=false
    }

    fun doneViewTodayAsteroids()
    {
        _viewTodayAsteroids.value=false
    }



    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}