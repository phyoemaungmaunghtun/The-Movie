package com.pmmh.themovie.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pmmh.themovie.R
import com.pmmh.themovie.base.BaseActivity
import com.pmmh.themovie.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity() {
    private lateinit var activityDetailBinding: ActivityDetailBinding
    override fun observeViewModel() {
    }

    override fun initViewBinding() {
        activityDetailBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_detail
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = intent.getStringExtra("MovieIdPass").toString()
        navigateDetail(movieId)
    }

    private fun navigateDetail(movieId: String) {
        val bundle = Bundle()
        bundle.putString("MovieIdPass", movieId)
        val fragment = DetailFragment()
        fragment.arguments = bundle
        transactionFragment(fragment)
    }

    private fun transactionFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.detail_frame_layout,
                fragment
            ).commit()
    }
}