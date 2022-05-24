package com.smart.app.ui.tutorials.heart

import com.smart.app.R
import com.smart.app.databinding.ActivityLoveBinding
import com.smart.app.ui.basewrapper.TemplateActivity
import com.smart.app.ui.tutorials.entitytype.LiveDataModel


// 이 튜토리얼은 복사 붙여넣기 참고용이므로 삭제하지 않기
// 대표 예제 액티비티 레이아웃

class LoveActivity : TemplateActivity<ActivityLoveBinding, LiveDataModel>(R.layout.activity_love) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel  // 액티비티마다 ex) abcDataModel = viewModel
            lifecycleOwner = this@LoveActivity   //  ex) lifecycleOwner = this@JustActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()

        // 액티비티마다 달라져야 하는 observe 코드 부분
        viewModel.isUpdate.observe(this) {
            binding.textTest.text = it.testOne
        }
    }
}