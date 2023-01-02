package com.pmmh.themovie.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pmmh.themovie.R
import com.pmmh.themovie.adapter.SeeAllMovieAdapter
import com.pmmh.themovie.base.BaseActivity
import com.pmmh.themovie.databinding.ActivitySeeAllMovieBinding
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.repository.Resource
import com.pmmh.themovie.utilities.observeLiveData
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.concurrent.schedule

@AndroidEntryPoint
class SeeAllMovieActivity : BaseActivity() {
    private var comeFrom = ""
    private lateinit var activitySeeAllBinding: ActivitySeeAllMovieBinding
    private val activityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }
    private lateinit var layoutManagerGrid: GridLayoutManager
    private lateinit var seeAllMovieAdapter: SeeAllMovieAdapter
    var page = 1


    override fun observeViewModel() {
        observeLiveData(activityViewModel.popularMovies, ::handlePopularMovieList)
        observeLiveData(activityViewModel.topRateMovies, ::handleTopRateMovieList)
    }

    override fun initViewBinding() {
        activitySeeAllBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_see_all_movie
        )
        activitySeeAllBinding.lifecycleOwner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        comeFrom = intent.getStringExtra("ComeFrom").toString()
        setSupportActionBar(activitySeeAllBinding.toolbarSeeAllMovies)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        activitySeeAllBinding.toolbarSeeAllMovies.setTitleTextColor(resources.getColor(R.color.white))
        activitySeeAllBinding.toolbarSeeAllMovies.setNavigationIconTint(resources.getColor(R.color.white))
        layoutManagerGrid = GridLayoutManager(this@SeeAllMovieActivity, 3)

        seeAllMovieAdapter = SeeAllMovieAdapter(this)
        activitySeeAllBinding.recyclerViewSeeAllMovies.apply {
            adapter = seeAllMovieAdapter
            layoutManager = layoutManagerGrid
            setHasFixedSize(false)
        }

        when (comeFrom) {
            "PopularMovies" -> {
                activitySeeAllBinding.toolbarSeeAllMovies.title = "Popular Movies"
                activityViewModel.fetchPopularMovies("", page)
                observeViewModel()
            }
            "TopRatedMovies" -> {
                activitySeeAllBinding.toolbarSeeAllMovies.title = "Top Rated Movies"
                activityViewModel.fetchTopRateMovies("", page)
                observeViewModel()
            }
            else -> {
                activitySeeAllBinding.toolbarSeeAllMovies.title = ""
            }

        }
    }

    private fun handlePopularMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                seeAllMovieAdapter.addMovie(it.results)
                navigateDetail()
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
                seeAllMovieAdapter.addMovie(it.results)
                navigateDetail()
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }

   /* private fun handleSearchMovieList(movieList: Resource<Movie>) {
        when (movieList) {
            is Resource.Loading -> loadingDialog.show()
            is Resource.Success -> movieList.data?.let {
                loadingDialog.dismiss()
                //seeAllMovieAdapter.addMovie(it.results)
            }
            is Resource.DataError -> {
                loadingDialog.dismiss()
                movieList.errorMessage?.let { responseDialog.showErrorDialog(it) }
            }
        }
    }*/

    private fun navigateDetail(){
        seeAllMovieAdapter.onItemClick = { movieId ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("MovieIdPass", movieId)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu?.findItem(R.id.action_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Type here to search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(query: String): Boolean {
                seeAllMovieAdapter?.filter?.filter(query)
                return true
            }

            override fun onQueryTextSubmit(newText: String): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}