package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.DBRoom
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val database = DBRoom.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToAsteroid = MutableLiveData<Asteroid?>()
    val navigateToAsteroid
        get() = _navigateToAsteroid

    private val _pictureUrl = MutableLiveData<String?>()
    val pictureUrl
        get() = _pictureUrl


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

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }

        viewModelScope.launch {
            val url=asteroidRepository.getPictureOfDay()
            if(!url.equals(""))
            {
                _pictureUrl.value=url
            }
        }
    }

    val asteroidList = asteroidRepository.asteroids



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