package com.pmmh.themovie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        activityViewModel.fetchTopRateMovies("", 1);
        hideLayout()

        activityBinding.popularMovieSeeAllMovieFrag.setOnClickListener {
            val intent = Intent(this, SeeAllMovieActivity::class.java)
            intent.putExtra("ComeFrom", "PopularMovies")
            startActivity(intent)
        }

        activityBinding.topRatedMovieSeeAllMovieFrag.setOnClickListener {
            val intent = Intent(this, SeeAllMovieActivity::class.java)
            intent.putExtra("ComeFrom", "TopRatedMovies")
            startActivity(intent)
        }
        showLayout()
    }

    private fun hideLayout() {

        image_slider_movie.visibility = View.GONE
        activityBinding.popularMovieLayoutMovieFrag.visibility = View.GONE
        activityBinding.topRatedMovieLayoutMovieFrag.visibility = View.GONE
        activityBinding.spinKitMovieFrag.visibility = View.VISIBLE
    }

    private fun showLayout() {
        activityBinding.spinKitMovieFrag.visibility = View.GONE
        image_slider_movie.visibility = View.VISIBLE
        activityBinding.popularMovieLayoutMovieFrag.visibility = View.VISIBLE
        activityBinding.topRatedMovieLayoutMovieFrag.visibility = View.VISIBLE
    }

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
                movieSliderAdapter.onItemClick = { movieId ->
                    val bundle = Bundle()
                    bundle.putString("MovieIdPass", movieId)
                    val fragment = DetailFragment()
                    fragment.arguments = bundle
                    transactionFragment(fragment)
                }
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
                popularMovieAdapter.onItemClick = { movieId ->
                    val bundle = Bundle()
                    bundle.putString("MovieIdPass", movieId)
                    val fragment = DetailFragment()
                    fragment.arguments = bundle
                    transactionFragment(fragment)
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
                topRatedMovieAdapter.onItemClick = { movieId ->
                    val bundle = Bundle()
                    bundle.putString("MovieIdPass", movieId)
                    val fragment = DetailFragment()
                    fragment.arguments = bundle
                    transactionFragment(fragment)
                }
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun transactionFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.frame_layout,
                fragment
            ).addToBackStack(null).commit()
    }
}