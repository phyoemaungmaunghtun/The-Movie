package com.pmmh.themovie.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmmh.themovie.R
import com.pmmh.themovie.adapter.MovieSliderAdapter
import com.pmmh.themovie.adapter.PopularMovieAdapter
import com.pmmh.themovie.base.BaseActivity
import com.pmmh.themovie.databinding.ActivityMainBinding
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.Resource
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

    private lateinit var activityBinding: ActivityMainBinding
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    override fun observeViewModel() {
        observeLiveData(activityViewModel.upcomingMovies, ::handleUpcomingMovieList)
        observeLiveData(activityViewModel.popularMovies, ::handlePopularMovieList)
        observeLiveData(activityViewModel.topRateMovies, ::handleTopRateMovieList)

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
        activityViewModel.getUpcomingMovies()
        activityViewModel.fetchPopularMovies("", 1)
        activityViewModel.fetchTopRateMovies("",1);
        // showLayout()
    }

    /*private fun hideLayout() {

        image_slider_movieFragment.visibility = View.GONE
        popular_MovieLayout_movieFrag.visibility = View.GONE
        topRated_MovieLayout_movieFrag.visibility = View.GONE
        spin_kit_movieFrag.visibility = View.VISIBLE
    }

    private fun showLayout(){

        spin_kit_movieFrag.visibility = View.GONE
        image_slider_movie.visibility = View.VISIBLE
        popular_MovieLayout_movieFrag.visibility = View.VISIBLE
        topRated_MovieLayout_movieFrag.visibility = View.VISIBLE
    }*/

    private fun handleUpcomingMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let { list ->
                loadingDialog.dismiss()
                movieSliderAdapter = MovieSliderAdapter(this, list.results)
                image_slider_movie.setSliderAdapter(movieSliderAdapter)
                image_slider_movie.setIndicatorAnimation(IndicatorAnimationType.WORM)
                image_slider_movie.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION)
                image_slider_movie.startAutoCycle()
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
                popularMovieAdapter = PopularMovieAdapter(this, it.results)
                activityBinding.popularMovieRecViewMoviesFragment.apply {
                    adapter = popularMovieAdapter
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    setHasFixedSize(false)
                }
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
                topRatedMovieAdapter = PopularMovieAdapter(this, it.results)
                activityBinding.topRatedMovieRecViewMoviesFragment.apply {
                    adapter = topRatedMovieAdapter
                    layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL, false
                    )
                    setHasFixedSize(false)
                }
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }
}