package com.smart.app.ui.basewrapper

import android.util.Log
import androidx.lifecycle.ViewModel

/****
 * LiveDataModel 의 부모 베이스, 추후에 추가적으로 다른 뷰모델 자식들의 부모가 될 수 있음 나머지는 그때 가서 활용
 */


// 아직은 속이 빈 껍데기일 뿐이지만 곧 기본 기능을 protected and open 으로 작성해서
// 자식 뷰모델에서 인터페이스처럼 사용할 수 있게 할 예정

open class BaseViewModel : ViewModel() {

    override fun onCleared() {
        Log.i("dummy", "dummy")
        super.onCleared()
    }
}