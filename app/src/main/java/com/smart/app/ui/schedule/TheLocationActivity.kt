package com.smart.app.ui.schedule

import com.smart.app.R
import com.smart.app.databinding.ActivityTheLocationBinding
import com.smart.app.ui.basewrapper.BaseActivity

class TheLocationActivity :
    BaseActivity<ActivityTheLocationBinding>(R.layout.activity_the_location) {

    override fun initView() {
        super.initView()

        binding.apply {
            liveDataModel = viewModel
            lifecycleOwner = this@TheLocationActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.plainTextInput0.setText(it.testOne)
        }
    }
}