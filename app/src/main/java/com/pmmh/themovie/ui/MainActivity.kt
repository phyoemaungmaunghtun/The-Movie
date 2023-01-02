package com.pmmh.themovie.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmmh.themovie.R
import com.pmmh.themovie.adapter.MovieSliderAdapter
import com.pmmh.themovie.adapter.PopularMovieAdapter
import com.pmmh.themovie.base.BaseActivity
import com.pmmh.themovie.databinding.ActivityMainBinding
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.Result
import com.pmmh.themovie.repository.Resource
import com.pmmh.themovie.utilities.NetworkConnection
import com.pmmh.themovie.utilities.observeLiveData
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    lateinit var image_slider_movie: SliderView
    lateinit var movieSliderAdapter: MovieSliderAdapter
    lateinit var popularMovieAdapter: PopularMovieAdapter
    lateinit var topRatedMovieAdapter: PopularMovieAdapter
    private val upComingType = 1
    private val popularType = 2
    private val topRateType = 3
    private var networkState = true

    private lateinit var activityBinding: ActivityMainBinding
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    override fun observeViewModel() {
        observeLiveData(activityViewModel.upcomingMovies, ::handleUpcomingMovieList)
        observeLiveData(activityViewModel.popularMovies, ::handlePopularMovieList)
        observeLiveData(activityViewModel.topRateMovies, ::handleTopRateMovieList)
        observeLiveData(activityViewModel.localUpComingMovies, ::showLocalUpcoming)
        observeLiveData(activityViewModel.localPopularMovies, ::showLocalPopular)
        observeLiveData(activityViewModel.localTopRateMovies, ::showLocalTopRate)
    }

    override fun initViewBinding() {
        activityBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        activityBinding.lifecycleOwner = this
        image_slider_movie = activityBinding.imageSliderMovieFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) { isConnected ->
            networkState = isConnected
            if (isConnected) {
                if (savedInstanceState == null) {
                    activityViewModel.getUpcomingMovies()
                    activityViewModel.fetchPopularMovies("", 1)
                    activityViewModel.fetchTopRateMovies("", 1)
                }
                //Toast.makeText(this, "Internet Connected!", Toast.LENGTH_LONG).show()
            } else {
                if (savedInstanceState == null) {
                    activityViewModel.getLocalMovie(upComingType)
                    activityViewModel.getLocalMovie(popularType)
                    activityViewModel.getLocalMovie(topRateType)
                }
                //Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_LONG).show()
            }
        }
        activityBinding.popularMovieSeeAllMovieFrag.setOnClickListener {
            if (networkState) {
                val intent = Intent(this, SeeAllMovieActivity::class.java)
                intent.putExtra("ComeFrom", "PopularMovies")
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    R.string.internet_warning_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        activityBinding.topRatedMovieSeeAllMovieFrag.setOnClickListener {
            if (networkState) {
                val intent = Intent(this, SeeAllMovieActivity::class.java)
                intent.putExtra("ComeFrom", "TopRatedMovies")
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    R.string.internet_warning_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun handleUpcomingMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                activityViewModel.cacheMovies(upComingType, it.results)
                slideAdapterSetup(it.results)
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun handlePopularMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                activityViewModel.cacheMovies(popularType, it.results)
                popularAdapterSetup(it.results)
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun handleTopRateMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                activityViewModel.cacheMovies(topRateType, it.results)
                topRateAdapterSetup(it.results)
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun slideAdapterSetup(result: List<Result>) {
        movieSliderAdapter = MovieSliderAdapter(this, result)
        image_slider_movie.setSliderAdapter(movieSliderAdapter)
        image_slider_movie.setIndicatorAnimation(IndicatorAnimationType.WORM)
        image_slider_movie.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
        image_slider_movie.startAutoCycle()
        movieSliderAdapter.onItemClick = { movieId ->
            if (networkState) {
                val bundle = Bundle()
                bundle.putString("MovieIdPass", movieId)
                val fragment = DetailFragment()
                fragment.arguments = bundle
                transactionFragment(fragment)
            } else {
                Toast.makeText(
                    this,
                    R.string.internet_warning_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun popularAdapterSetup(results: List<Result>) {
        popularMovieAdapter = PopularMovieAdapter(this, results)
        activityBinding.popularMovieRecViewMoviesFragment.apply {
            adapter = popularMovieAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            setHasFixedSize(false)
        }
        popularMovieAdapter.onItemClick = { movieId ->
            if (networkState) {
                val bundle = Bundle()
                bundle.putString("MovieIdPass", movieId)
                val fragment = DetailFragment()
                fragment.arguments = bundle
                transactionFragment(fragment)
            } else {
                Toast.makeText(
                    this,
                    R.string.internet_warning_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun topRateAdapterSetup(results: List<Result>) {
        topRatedMovieAdapter = PopularMovieAdapter(this, results)
        activityBinding.topRatedMovieRecViewMoviesFragment.apply {
            adapter = topRatedMovieAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            setHasFixedSize(false)
        }

        topRatedMovieAdapter.onItemClick = { movieId ->
            if (networkState) {
                val bundle = Bundle()
                bundle.putString("MovieIdPass", movieId)
                val fragment = DetailFragment()
                fragment.arguments = bundle
                transactionFragment(fragment)
            } else {
                Toast.makeText(this, R.string.internet_warning_message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showLocalUpcoming(movies: List<Result>) {
        slideAdapterSetup(movies)
    }

    private fun showLocalPopular(movies: List<Result>) {
        popularAdapterSetup(movies)
    }

    private fun showLocalTopRate(movies: List<Result>) {
        topRateAdapterSetup(movies)
    }

    private fun transactionFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.frame_layout,
                fragment
            ).addToBackStack(null).commit()
    }
}