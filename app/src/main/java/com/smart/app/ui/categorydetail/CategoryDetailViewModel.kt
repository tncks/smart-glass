package com.smart.app.ui.categorydetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.app.model.Schedule
import com.smart.app.model.TopSelling
import com.smart.app.repository.categorydetail.CategoryDetailRepository
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet

class CategoryDetailViewModel(
    private val categoryDetailRepository: CategoryDetailRepository
) : ViewModel() {

    private val _topSelling = MutableLiveData<TopSelling>()
    val topSelling: LiveData<TopSelling> = _topSelling

    private val _basicSchedule = MutableLiveData<List<Schedule>>()
    val basicSchedule: LiveData<List<Schedule>> = _basicSchedule


    init {
        loadCategoryDetail()
        loadSchedule()
    }

    private fun loadCategoryDetail() {

        viewModelScope.launch {
            try {
                if (CheckInternet().check()) {
                    val categoryDetail = categoryDetailRepository.getCategoryDetail()
                    _topSelling.value = categoryDetail.topSelling
                } else {
                    Log.i("dummy", "dummy")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // later
            }
        }

    }

    private fun loadSchedule() {

        viewModelScope.launch {
            try {
                if (CheckInternet().check()) {
                    val loadedSchedules = listOf(
                        Schedule("탐방일정오늘한시", "12시"), Schedule("먹방정오늘세시", "16시")
                    )
                    /*
                        Schedule("일정오늘한시", "22시"), Schedule("먹방오세시", "19시"),
                        Schedule("탐방일정", "17시"), Schedule("방일정오늘세시", "18시"),
                        Schedule("탐정늘한시", "13시"), Schedule("먹늘세시", "11시")
                     */
                    _basicSchedule.value = loadedSchedules
                } else {
                    Log.i("dummy", "dummy")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // later
            }
        }

    }

}