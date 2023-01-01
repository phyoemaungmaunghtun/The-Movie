package com.pmmh.themovie.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pmmh.themovie.R
import com.pmmh.themovie.base.BaseActivity
import com.pmmh.themovie.databinding.ActivityYoutubeVideoPlayerBinding
import com.pmmh.themovie.model.youtubeTrailer.MovieTrailer
import com.pmmh.themovie.repository.Resource
import com.pmmh.themovie.utilities.observeLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YoutubeVideoPlayerActivity : BaseActivity() {
    private lateinit var activityYoutubeVideoPlayerActivity: ActivityYoutubeVideoPlayerBinding
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }
    private var movieId: String = ""
    private var videoId = ""
    private lateinit var youtube_player_view: YouTubePlayerView
    private lateinit var backBtn_youtubeVideoPlayerPage: ImageView
    override fun observeViewModel() {
        observeLiveData(activityViewModel.movieTrailer, ::handleMovieTrailer)
    }

    override fun initViewBinding() {
        activityYoutubeVideoPlayerActivity = DataBindingUtil.setContentView(
            this, R.layout.activity_youtube_video_player
        )
        activityYoutubeVideoPlayerActivity.lifecycleOwner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        movieId = intent.getStringExtra("MovieIdPass").toString()
        youtube_player_view = findViewById(R.id.youtube_player_view)
        backBtn_youtubeVideoPlayerPage = findViewById(R.id.backBtn_youtubeVideoPlayerPage)

        backBtn_youtubeVideoPlayerPage.visibility = View.INVISIBLE
        activityViewModel.fetchMovieTrailer(movieId, "en-US")
        observeViewModel()

        lifecycle.addObserver(youtube_player_view)

        backBtn_youtubeVideoPlayerPage.setOnClickListener {
            onBackPressed()
        }
    }

    private fun handleMovieTrailer(movieList: Resource<MovieTrailer>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                youtube_player_view.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        videoId = if (it.results[0].type == "Trailer") {
                            it.results[0].key
                        } else {
                            it.results[1].key
                        }
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }
}