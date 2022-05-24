package com.smart.app.ui.basewrapper

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.smart.app.ui.tutorials.entitytype.LiveDataModel

/****
 * ViewModel 없는 버전 베이스, 단순 버전 (데이터 처리 불가)
 */


abstract class BaseActivity<T : ViewDataBinding>
    (@LayoutRes private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var binding: T
    protected lateinit var viewModel: LiveDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        beforeSetContentView()

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        viewModel = ViewModelProvider(this).get(LiveDataModel::class.java)

        initView()
        initViewModel()
        initListener()
        afterOnCreate()
    }

    protected open fun beforeSetContentView() {}
    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun initListener() {}
    protected open fun afterOnCreate() {}
}