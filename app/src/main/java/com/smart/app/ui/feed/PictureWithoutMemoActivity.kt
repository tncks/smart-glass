package com.smart.app.ui.feed

import com.smart.app.R
import com.smart.app.databinding.ActivityPictureWithoutMemoBinding
import com.smart.app.ui.basewrapper.BaseActivity

class PictureWithoutMemoActivity :
    BaseActivity<ActivityPictureWithoutMemoBinding>(R.layout.activity_picture_without_memo) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel
            lifecycleOwner = this@PictureWithoutMemoActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.plainTextInput8.text = it.testOne
        }
    }
}