package com.pmmh.themovie.di

import android.app.Activity
import com.pmmh.themovie.base.BaseActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object ActivityContextProvider {

    @Singleton
    @Provides
    fun providerBaseActivity(activity: Activity): BaseActivity {
        check(activity is BaseActivity)
        return activity as BaseActivity
    }
}