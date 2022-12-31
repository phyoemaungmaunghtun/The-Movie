package com.pmmh.themovie.utilities

import androidx.lifecycle.*

fun <T> LifecycleOwner.observeLiveData(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}