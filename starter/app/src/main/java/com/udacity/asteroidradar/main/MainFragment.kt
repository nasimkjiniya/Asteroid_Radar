package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModel.Factory(application)

        viewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        var adapter = MainListAdapter(MainListAdapter.AsteroidClickListener{ asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter=adapter
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { it->
            it.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToAsteroid.observe(viewLifecycleOwner, Observer {
            if(null!=it)
            {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.onNavigated()
            }
        })

        viewModel.pictureUrl.observe(viewLifecycleOwner, Observer {
            if(it!=null)
            {
                Picasso.get()
                    .load(it)
                    .placeholder(R.drawable.placeholder_picture_of_day)
                    .error(R.drawable.placeholder_picture_of_day)
                    .into(binding.activityMainImageOfTheDay);
            }
        })

        viewModel.viewTodayAsteroids.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                viewModel.viewTodayAsteroids()
                viewModel.doneViewTodayAsteroids()
            }
        })

        viewModel.viewWeekAsteroids.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                viewModel.viewWeekAsteroids()
                viewModel.doneViewWeekAsteroids()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.title.equals("View today asteroids"))
        {
            viewModel.viewTodayAsteroids.value=true
        }
        else
        {
            viewModel.viewWeekAsteroids.value=true
        }

        return true
    }
}
