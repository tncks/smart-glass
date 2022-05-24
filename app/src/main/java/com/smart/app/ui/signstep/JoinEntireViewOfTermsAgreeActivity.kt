package com.smart.app.ui.signstep

import com.smart.app.R
import com.smart.app.databinding.ActivityJoinEntireViewOfTermsAgreeBinding
import com.smart.app.entitytype.TermDataModel
import com.smart.app.ui.basewrapper.TemplateActivity

class JoinEntireViewOfTermsAgreeActivity :
    TemplateActivity<ActivityJoinEntireViewOfTermsAgreeBinding, TermDataModel>(R.layout.activity_join_entire_view_of_terms_agree) {

    override fun initView() {
        super.initView()

        binding.apply {
            termDataModel = viewModel
            lifecycleOwner = this@JoinEntireViewOfTermsAgreeActivity
        }

    }

    override fun initViewModel() {
        super.initViewModel()


        viewModel.isUpdate.observe(this) {
            binding.textTest.text = it.testOne
        }
    }

    override fun initListener() {
        super.initListener()

        binding.apply {
            okyesconfirm.setOnClickListener {
                // later write code here add other work do rest things
                // Log.d("confirmed", "user confirmed term")
                finish()
            }
        }
    }
}