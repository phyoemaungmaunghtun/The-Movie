package com.pmmh.themovie.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pmmh.themovie.utilities.LoadingDialog
import com.pmmh.themovie.utilities.ResponseDialogUtil
import javax.inject.Inject

abstract class BaseFragment: Fragment() {

    @Inject
    lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var responseDialog: ResponseDialogUtil

    protected abstract fun initViewBinding()

    abstract fun observeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        observeViewModel()
    }

    override fun onSaveInstanceState(oldInstanceState: Bundle) {
        super.onSaveInstanceState(oldInstanceState)
        oldInstanceState.clear()
    }
}