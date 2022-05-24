package com.smart.app.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.app.R
import com.smart.app.databinding.DevbyteItemBinding
import com.smart.app.databinding.FragmentDevByteBinding
import com.smart.app.domain.DevByteVideo
import com.smart.app.viewmodels.DevByteViewModel


/*
* Note
* This is Test Fragment
* Should be ignored
* */


class DevByteFragment : Fragment() {

    private val viewModel: DevByteViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "travelz app 404 access lazy problem in process"
        }
        ViewModelProvider(this, DevByteViewModel.Factory(activity.application))[DevByteViewModel::class.java]
    }


    private var viewModelAdapter: DevByteAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDevByteBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dev_byte,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModelAdapter = DevByteAdapter(VideoClick {


            val packageManager = context?.packageManager ?: return@VideoClick


            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
            if (intent.resolveActivity(packageManager) == null) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }


        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }



        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlist.observe(viewLifecycleOwner, Observer { videos ->
            videos?.apply {
                viewModelAdapter?.videos = videos
            }
        })
    }


    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Log.d("NetworkError", "Network Error")
            viewModel.onNetworkErrorShown()
        }
    }


    private val DevByteVideo.launchUri: Uri
        get() {
            val httpUri = Uri.parse(url)
            return Uri.parse("vnd.youtube:" + httpUri.getQueryParameter("v"))
        }
}


class VideoClick(val block: (DevByteVideo) -> Unit) {

    fun onClick(video: DevByteVideo) = block(video)
}


class DevByteAdapter(private val callback: VideoClick) : RecyclerView.Adapter<DevByteViewHolder>() {


    var videos: List<DevByteVideo> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevByteViewHolder {
        val withDataBinding: DevbyteItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            DevByteViewHolder.LAYOUT,
            parent,
            false
        )
        return DevByteViewHolder(withDataBinding)
    }

    override fun getItemCount() = videos.size


    override fun onBindViewHolder(holder: DevByteViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.video = videos[position]
            it.videoCallback = callback
        }
    }

}


class DevByteViewHolder(val viewDataBinding: DevbyteItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.devbyte_item
    }
}