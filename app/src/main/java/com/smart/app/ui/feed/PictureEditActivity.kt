package com.smart.app.ui.feed

import com.smart.app.R
import com.smart.app.databinding.ActivityPictureEditBinding
import com.smart.app.ui.basewrapper.BaseActivity

class PictureEditActivity :
    BaseActivity<ActivityPictureEditBinding>(R.layout.activity_picture_edit) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel
            lifecycleOwner = this@PictureEditActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.unrealgonetv.text = it.testOne
        }
    }
}