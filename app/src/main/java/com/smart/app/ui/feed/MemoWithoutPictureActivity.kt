package com.smart.app.ui.feed

import com.smart.app.R
import com.smart.app.databinding.ActivityMemoWithoutPictureBinding
import com.smart.app.ui.basewrapper.BaseActivity

class MemoWithoutPictureActivity :
    BaseActivity<ActivityMemoWithoutPictureBinding>(R.layout.activity_memo_without_picture) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel
            lifecycleOwner = this@MemoWithoutPictureActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()

        
        viewModel.isUpdate.observe(this) {
            binding.plainTextInput3.setText(it.testOne)
        }
    }
}
