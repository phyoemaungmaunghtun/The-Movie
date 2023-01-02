package com.pmmh.themovie.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.pmmh.themovie.TestCoroutineRule
import com.pmmh.themovie.model.Movie
import com.pmmh.themovie.model.Result
import com.pmmh.themovie.repository.DataRepository
import com.pmmh.themovie.repository.LocalRepository
import com.pmmh.themovie.repository.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private lateinit var viewModel: MainActivityViewModel

    @Mock
    private lateinit var remoteRepository: DataRepository

    @Mock
    private lateinit var localRepository: LocalRepository

    @Before
    fun setUp() {
        viewModel = MainActivityViewModel(remoteRepository, localRepository)
    }


    @Test
    fun `when fetching remoteData is ok then return a empty model successfully`() {
        testCoroutineRule.runBlockingTest {
            val emptyModel = Resource.Success(Movie())
            whenever(remoteRepository.getUpcomingMovies(any(), any())).thenAnswer {
                emptyModel
            }
            viewModel.getUpcomingMovies()
            assertNotNull(viewModel.upcomingMovies.value)
            assertEquals(emptyModel, viewModel.upcomingMovies.value)
        }
    }

    @Test
    fun `when fetching localData is ok then return empty list successfully`() {
        testCoroutineRule.runBlockingTest {
            val emptyList = emptyList<Result>()
            whenever(localRepository.getMovies(any())).thenAnswer {
                emptyList
            }
            viewModel.getLocalMovie(1)
            assertNotNull(viewModel.localUpComingMovies.value)
            assertEquals(emptyList, viewModel.localUpComingMovies.value)
        }
    }

}