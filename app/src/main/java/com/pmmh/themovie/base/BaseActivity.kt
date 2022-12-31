package com.pmmh.themovie.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pmmh.themovie.utilities.LoadingDialog
import com.pmmh.themovie.utilities.ResponseDialogUtil
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity() {

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var responseDialog: ResponseDialogUtil

    abstract fun observeViewModel()
    protected abstract fun initViewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }
}