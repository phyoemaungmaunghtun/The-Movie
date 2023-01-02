package com.pmmh.themovie.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave
import com.pmmh.themovie.adapter.MovieCastAdapter
import com.pmmh.themovie.adapter.MovieCrewAdapter
import com.pmmh.themovie.base.BaseFragment
import com.pmmh.themovie.databinding.FragmentMovieDetailsBinding
import com.pmmh.themovie.model.movieCredit.MovieCredit
import com.pmmh.themovie.model.movieDetails.MovieDetails
import com.pmmh.themovie.repository.Resource
import com.pmmh.themovie.utilities.Utils
import com.pmmh.themovie.utilities.observeLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment() {
    private lateinit var fragmentBinding: FragmentMovieDetailsBinding
    //lateinit var activityViewModel: MainActivityViewModel
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private var movieId: String = ""

    lateinit var title_single_movie_Details: TextView
    lateinit var adultCheck_movie_slider: TextView
    lateinit var date_single_movie_Details: TextView
    lateinit var genre1_movie_Details: TextView
    lateinit var genre2_movie_Details: TextView
    lateinit var movieOverview_MovieDetails: TextView
    lateinit var popularity_movieDetails: TextView
    lateinit var imageView_single_movie_Details: ImageView
    lateinit var trailer_movieDetails: ImageView
    lateinit var genre2Layout_movie_Details: LinearLayout
    lateinit var progress_bar_MovieDetails: ProgressBar
    lateinit var castRecView_movieDetails: RecyclerView
    lateinit var crewRecView_movieDetails: RecyclerView
    lateinit var spin_kit_movieDetails: ProgressBar

    lateinit var movieCastAdapter: MovieCastAdapter
    lateinit var movieCrewAdapter: MovieCrewAdapter
    lateinit var linearLayout_movieTrailer: LinearLayout
    lateinit var adultCheckLayout_movieDetails: LinearLayout
    lateinit var linearLayout2_title_movieDetails: LinearLayout
    lateinit var descLayout: LinearLayout

    override fun initViewBinding() {
        fragmentBinding = FragmentMovieDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        movieId = arguments?.getString("MovieIdPass").toString()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val backBtn_movie_Details: ImageView = fragmentBinding.backBtnMovieDetails
        title_single_movie_Details = fragmentBinding.titleSingleMovieDetails
        imageView_single_movie_Details = fragmentBinding.imageViewSingleMovieDetails
        adultCheck_movie_slider = fragmentBinding.adultCheckMovieSlider
        date_single_movie_Details = fragmentBinding.dateSingleMovieDetails
        genre1_movie_Details = fragmentBinding.genre1MovieDetails
        genre2_movie_Details = fragmentBinding.genre2MovieDetails
        genre2Layout_movie_Details = fragmentBinding.genre2LayoutMovieDetails
        movieOverview_MovieDetails = fragmentBinding.movieOverviewMovieDetails
        progress_bar_MovieDetails = fragmentBinding.progressBarMovieDetails
        popularity_movieDetails = fragmentBinding.popularityMovieDetails
        castRecView_movieDetails = fragmentBinding.castRecViewMovieDetails
        crewRecView_movieDetails = fragmentBinding.crewRecViewMovieDetails
        spin_kit_movieDetails = fragmentBinding.spinKitMovieDetails
        linearLayout_movieTrailer = fragmentBinding.linearLayoutMovieTrailer
        adultCheckLayout_movieDetails = fragmentBinding.adultCheckLayoutMovieDetails
        linearLayout2_title_movieDetails = fragmentBinding.linearLayout2TitleMovieDetails
        trailer_movieDetails = fragmentBinding.trailerMovieDetails
        descLayout = fragmentBinding.descLayout
        val doubleBounce: Sprite = Wave()
        spin_kit_movieDetails.indeterminateDrawable = doubleBounce
        hideAllLayouts()


        backBtn_movie_Details.setOnClickListener {
            activity?.onBackPressed()
        }

        activityViewModel.fetchMovieDetails(movieId, "en-US")
        activityViewModel.fetchMovieCredit(movieId, "en-US")

        trailer_movieDetails.setOnClickListener {
            val intent = Intent(activity, YoutubeVideoPlayerActivity::class.java)
            intent.putExtra("MovieIdPass", movieId)
            startActivity(intent)
        }

        Handler().postDelayed({

            showAllLayouts()

        }, 600)

        return fragmentBinding.root
    }

    override fun observeViewModel() {
        //activityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        observeLiveData(activityViewModel.movieDetail, ::handleMovieDetail)
        observeLiveData(activityViewModel.movieCredit, ::handleMovieCredit)
    }

    private fun handleMovieDetail(movieList: Resource<MovieDetails>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                title_single_movie_Details.text = it.title
                Glide.with(this).load(Utils.posterUrlMake(it.posterPath))
                    .into(imageView_single_movie_Details)

                if (it.adult) {
                    adultCheck_movie_slider.text = "18+"
                } else {
                    adultCheck_movie_slider.text = "13+"
                }

                date_single_movie_Details.text = it.releaseDate
                movieOverview_MovieDetails.text = it.overview

                genre1_movie_Details.text = it.genres[0].name
                if (it.genres.size > 1) {
                    genre2_movie_Details.text = it.genres[1].name
                    genre2Layout_movie_Details.visibility = View.VISIBLE
                } else {
                    genre2Layout_movie_Details.visibility = View.INVISIBLE
                }

                progress_bar_MovieDetails.progress = (it.voteAverage * 10).toInt()
                popularity_movieDetails.text = "${it.voteAverage} Rating"
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun handleMovieCredit(movieList: Resource<MovieCredit>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                movieCastAdapter = MovieCastAdapter(requireContext(), it.cast)
                castRecView_movieDetails.apply {
                    adapter = movieCastAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(false)
                }
                movieCrewAdapter = MovieCrewAdapter(requireContext(), it.crew)
                crewRecView_movieDetails.apply {
                    adapter = movieCrewAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(false)
                }
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

    private fun hideAllLayouts() {
        spin_kit_movieDetails.visibility = View.VISIBLE
        imageView_single_movie_Details.visibility = View.INVISIBLE
        linearLayout_movieTrailer.visibility = View.INVISIBLE
        descLayout.visibility = View.INVISIBLE
        adultCheckLayout_movieDetails.visibility = View.INVISIBLE
        linearLayout2_title_movieDetails.visibility = View.INVISIBLE
    }

    private fun showAllLayouts() {
        spin_kit_movieDetails.visibility = View.INVISIBLE
        imageView_single_movie_Details.visibility = View.VISIBLE
        linearLayout_movieTrailer.visibility = View.VISIBLE
        descLayout.visibility = View.VISIBLE
        linearLayout2_title_movieDetails.visibility = View.VISIBLE
    }
}