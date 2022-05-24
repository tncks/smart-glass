package com.smart.app.ui.feed

import com.smart.app.R
import com.smart.app.databinding.ActivityPictureWithMemoBinding
import com.smart.app.ui.basewrapper.BaseActivity

class PictureWithMemoActivity : BaseActivity<ActivityPictureWithMemoBinding>(R.layout.activity_picture_with_memo) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel
            lifecycleOwner = this@PictureWithMemoActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.plainTextInput9.setText(it.testOne)
        }
    }
}