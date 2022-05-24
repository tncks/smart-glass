package com.smart.app.ui.schedule

import com.smart.app.R
import com.smart.app.databinding.ActivityScheduleWriteEditBinding
import com.smart.app.ui.basewrapper.BaseActivity

class ScheduleWriteEditActivity :
    BaseActivity<ActivityScheduleWriteEditBinding>(R.layout.activity_schedule_write_edit) {

    override fun initView() {
        super.initView()

        binding.apply {

            liveDataModel = viewModel
            lifecycleOwner = this@ScheduleWriteEditActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.tvSimpleCompleteEditSubmit.text = it.testOne
        }
    }
}