package com.smart.app.ui.category

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.smart.app.R
import com.smart.app.common.BFLAG
import com.smart.app.common.KEY_CATEGORY_ID
import com.smart.app.common.KEY_CATEGORY_LABEL
import com.smart.app.common.KEY_CATEGORY_PERIOD
import com.smart.app.databinding.FragmentCategoryBinding
import com.smart.app.myjetcp.MainComposeActivity
import com.smart.app.ui.common.DialogStylingUtil
import com.smart.app.ui.common.EventObserver
import com.smart.app.ui.common.ViewModelFactory
import com.smart.app.util.NetworkStatus
import com.smart.app.util.NetworkStatusHelper
import com.smart.app.util.leftDrawable
import kotlinx.coroutines.*
import xyz.teamgravity.checkinternet.CheckInternet


class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModels { ViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentCategoryBinding
    private var flag: Boolean = false
    private var deleteModeOn: Boolean = false
    private lateinit var categoryAdapter: CategoryAdapter
    private var isCleanStatusNow: Boolean = true
    private lateinit var callback: OnBackPressedCallback
    private lateinit var myEndSnackbar: Snackbar
//    private lateinit var permissionsRequest: ActivityResultLauncher<Array<String>>

    /*---------------------------------------------*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        // permissionsRequest = getPermissionsRequest()
        webConnectionEstablishmentObserverSystemOS()
        return binding.root
    }

    private fun webConnectionEstablishmentObserverSystemOS() {
        NetworkStatusHelper(requireContext()).observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.Available -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (CheckInternet().check()) {
                            withContext(Dispatchers.Main) {
                                if (binding.ltswifi.visibility == View.VISIBLE) {
                                    Snackbar.make(binding.root, "화면을 새로고침 하시겠습니까?", 10000)
                                        .setAction("확인") {
                                            this@CategoryFragment.viewModel.externalReloadInitAgainOnOnlineStatus()
                                        }.show()
                                }
                                binding.ltswifi.visibility = View.INVISIBLE
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                binding.ltswifi.visibility = View.VISIBLE
                            }
                        }
                    }

                }
                NetworkStatus.Unavailable -> {
                    // this block current work half finished
                    // 현재 위에 코드 한줄이 조건이 성립이 안되는 문제가 존재 오프라인인데 온라인으로만 아직 인식되고있음
                    // 해결해야됨 -> update -> 해결하긴 했음
                    // Show Snackbar top or display CardView No status on Recyclerview Overlapped INVISIBLE
                    // -> change VISIBLE
                    // or use other way
                }
                else -> {
                    Log.d("networkerror", "networkerror")
                    // or throw exception error and close or safely handle this error
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val myMenu = binding.toolbarCategory
        setToggleMenuInFragment(myMenu)

        displayAddButtonWhenNoItemsStatus()

        CoroutineScope(Dispatchers.IO).launch {
            if (CheckInternet().check()) {
                withContext(Dispatchers.Main) {
                    binding.ltswifi.visibility = View.INVISIBLE
                }
            } else {
                withContext(Dispatchers.Main) {
                    binding.ltswifi.visibility = View.VISIBLE
                }
            }
        }

        doBasicAdapterSettingAndEventObserverSettings()
    }


    /*-----------------------------------------------------*/

    private fun doBasicAdapterSettingAndEventObserverSettings() {

        categoryAdapter = CategoryAdapter(viewModel)
        binding.rvCategoryList.adapter = categoryAdapter

        resetToOriginalObservers()

    }


    /*-----------------------------------------------------*/

    private fun displayAddButtonWhenNoItemsStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L)
            if (viewModel.isNothingToShow) {
                mustDisplayIt()
            } else {
                mustHideIt()
            }
        }
    }


    private suspend fun mustDisplayIt() {
        withContext(Dispatchers.Main) {
            binding.showonnothing.visibility = View.VISIBLE
            binding.showonnothing.setOnClickListener {
                justShowNothingMessageToasting()  // withContext IO 쓰거나 Main 쓰기
            }
        }
    }

    private suspend fun mustHideIt() {
        withContext(Dispatchers.Main) {
            binding.showonnothing.visibility = View.GONE
        }
    }

    private fun justShowNothingMessageToasting() {
        Toast.makeText(context, "임시 토스트, 코드변경시까지 사용", Toast.LENGTH_SHORT).show()
//        val smallIntentForFirstProfileAdd = Intent(context, ProfileAddEditActivity::class.java)
//        smallIntentForFirstProfileAdd.putExtra("mIndex", 0)
//        startActivity(smallIntentForFirstProfileAdd)
        // 일단은 임시 토스트 생성만, 나중에 다시 변경 -> intent ProfileNewFirstActivity start
        // 아무것도 없는 화면에서 완전 처음 생성하는거는 따로 뷰 만들고 따로 처리해줘야될듯, not profileaddedit
        // 지금 그대로 액티비티 실행시, ProfileAddEdit 액티비티에서 Exception 발생하고 앱 종료되는 문제있
    }

    private fun setToggleMenuInFragment(myMToolbar: Toolbar) {

        myMToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    addingSec()
                }
                R.id.menu_delete -> {
                    deletingSec(true)
                }
                R.id.menu_move -> {
                    movingSec()
                }
            }
            true
        }
    }


    private fun addingSec() {
        Toast.makeText(requireContext(), "추가", Toast.LENGTH_SHORT).show()
//        val smallIntentForFirstProfileAdd = Intent(context, ProfileAddEditActivity::class.java)
//        smallIntentForFirstProfileAdd.putExtra("mIndex", 0)
//        startActivity(smallIntentForFirstProfileAdd)
    }

    private fun deletingSec(isStart: Boolean) {
        presettingForDeleteMode(isStart)
        val bottomSheetPersistent: LinearLayout = binding.root.findViewById(R.id.bottom_sheet_persistent)
        val sheetBehavior = BottomSheetBehavior.from(bottomSheetPersistent)

        when (isStart) {
            true -> {
                bottomSheetPersistent.visibility = View.VISIBLE
                val tLeft = bottomSheetPersistent.findViewById<TextView>(R.id.take_image_label)
                val tCenter = bottomSheetPersistent.findViewById<TextView>(R.id.select_image_label)
                val tRight = bottomSheetPersistent.findViewById<TextView>(R.id.select_image_label2)
                sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        handleBottomSheetViewExceptionIfExist(newState, bottomSheetPersistent, sheetBehavior)
                    }
                })
                tLeft.setOnClickListener {
                    toggleSelectionsAndIcon()

                    refreshViewStatuses(sheetBehavior)
                }
                tCenter.setOnClickListener {

                    if (!categoryAdapter.getmItemListCheckEmpty()) {
                        confirmModalShowAndGetBool(bottomSheetPersistent)
                    }


                }
                tRight.setOnClickListener {
                    bottomSheetPersistent.visibility = View.INVISIBLE
                    bottomSheetPersistent.visibility = View.VISIBLE
                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    bottomSheetPersistent.visibility = View.VISIBLE
                    bottomSheetPersistent.visibility = View.INVISIBLE
                    bottomSheetPersistent.visibility = View.GONE

                    goBackToOriginalStatuses()

                    categoryAdapter = CategoryAdapter(viewModel)
                    categoryAdapter.setMode(false)
                    binding.rvCategoryList.adapter = categoryAdapter

                    resetToOriginalObservers()

                    deleteModeOn = false
                    bottomSheetPersistent.visibility = View.VISIBLE
                    bottomSheetPersistent.visibility = View.INVISIBLE
                    bottomSheetPersistent.visibility = View.VISIBLE
                    bottomSheetPersistent.visibility = View.GONE
                    bottomSheetPersistent.visibility = View.INVISIBLE
                }
            }
            else -> {
                bottomSheetPersistent.visibility = View.INVISIBLE
            }
        }

        doRestSettingForDeleteMode(sheetBehavior, isStart)
    }


    private fun presettingForDeleteMode(isStart: Boolean) {
        when (isStart) {
            true -> {
                categoryAdapter.processStartClick(0)
            }
            else -> {
                // do nothing
            }
        }

        categoryAdapter.initBinding(binding)
        isCleanStatusNow = !isStart
        deleteModeOn = isStart
        categoryAdapter.setMode(isStart)


        when (isStart) {
            true -> {
                requireActivity().findViewById<BottomNavigationView>(R.id.navigation_main).visibility =
                    View.INVISIBLE
                binding.toolbarCategory.visibility = View.INVISIBLE
                binding.sametoptitle.visibility = View.VISIBLE
            }
            else -> {
                requireActivity().findViewById<BottomNavigationView>(R.id.navigation_main).visibility =
                    View.VISIBLE
                binding.toolbarCategory.visibility = View.VISIBLE
                binding.sametoptitle.visibility = View.GONE
            }
        }

    }


    private fun doRestSettingForDeleteMode(sheetBehavior: BottomSheetBehavior<LinearLayout>, isStart: Boolean) {
        when (isStart) {
            true -> {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                categoryAdapter.initSheetBehavior(sheetBehavior)
            }
            else -> {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                categoryAdapter = CategoryAdapter(viewModel)
                binding.rvCategoryList.adapter = categoryAdapter


                viewModel.items.observe(viewLifecycleOwner) {
                    categoryAdapter.submitList(it)
                }


                viewModel.openCategoryEvent.observe(viewLifecycleOwner, EventObserver {
                    if (isCleanStatusNow) {
                        openCategoryDetail(it.categoryId, it.label, it.period)
                    }
                })
            }
        }
    }


    private suspend fun beginViewProcess(sheetBehavior: BottomSheetBehavior<LinearLayout>) {

        withContext(Dispatchers.Main) {

            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            delay(10L)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            val bottomSheetPersistent: LinearLayout = binding.root.findViewById(R.id.bottom_sheet_persistent)
            val tLeft = bottomSheetPersistent.findViewById<TextView>(R.id.take_image_label)
            if (!SelectionAllToggleSelector.isAllSelectChecked) {
                tLeft.leftDrawable(R.drawable.ic_checkmark1)
            } else {
                tLeft.leftDrawable(R.drawable.ic_unchecked)
            }

            BottomSheetBehavior.from(bottomSheetPersistent).state = BottomSheetBehavior.STATE_EXPANDED

            delay(10L)
            binding.executePendingBindings()
            delay(10L)
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        }

    }

    @Suppress("KotlinConstantConditions")
    private fun handleBottomSheetViewExceptionIfExist(
        newState: Int,
        bottomSheetPersistent: LinearLayout,
        sheetBehavior: BottomSheetBehavior<LinearLayout>
    ) {
        if (!deleteModeOn && newState == BottomSheetBehavior.STATE_EXPANDED) {
            deleteModeOn = !deleteModeOn
            bottomSheetPersistent.visibility = View.INVISIBLE
            sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            binding.executePendingBindings()
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            deleteModeOn = !deleteModeOn
        }
    }

    private fun toggleSelectionsAndIcon() {
        SelectionAllToggleSelector.isAllSelectChecked =
            !SelectionAllToggleSelector.isAllSelectChecked

        if (!SelectionAllToggleSelector.isAllSelectChecked) {
            categoryAdapter.selectAll()
        } else {
            categoryAdapter.deselectAll()
        }
    }

    private fun refreshViewStatuses(sheetBehavior: BottomSheetBehavior<LinearLayout>) {
        CoroutineScope(Dispatchers.Main).launch {
            beginViewProcess(sheetBehavior)
        }
    }

    private fun confirmModalShowAndGetBool(bottomSheetPersistent: LinearLayout) {

        if (categoryAdapter.getmItemListCheckEmpty()) {
            return
        }

        val builder = AlertDialog.Builder(requireContext(), R.style.MDialogTheme)

        builder.setTitle(
            "정말 삭제하시겠습니까?"
        )
            .setMessage("삭제 후 복구가 되지 않습니다.")
            .setPositiveButton("삭제",
                DialogInterface.OnClickListener { _, _ ->

                    bottomSheetPersistent.visibility = View.INVISIBLE
                    deleteConfirmedByUser()

                })
            .setNegativeButton(
                "취소",
                DialogInterface.OnClickListener { _, _ ->

                })

        setDialogLayoutStyleAndShow(builder)
    }

    private fun deleteConfirmedByUser() {

        categoryAdapter.fakeViewUpdateGone()
        goBackToOriginalStatuses()

        deleteModeOn = false

        CoroutineScope(Dispatchers.IO).launch {
            delay(2500L)
            var internalFlag = false
            for (inter in 0 until 6) {
                withContext(Dispatchers.IO) {
                    if (SelectionAllToggleSelector.singleToneFlag) {
                        SelectionAllToggleSelector.singleToneFlag = false
                        internalFlag = true
                    }
                }
                if (!internalFlag) {
                    delay(1000L)
                } else {
                    resetBasicAdapterInScopeMainUI()

                    for (twiceIndex in 0 until 2) {
                        if (viewModel.isNothingToShow) {
                            mustDisplayIt()
                        } else {
                            mustHideIt()
                        }
                        if (twiceIndex == 0) {
                            delay(1000L)
                        }
                    }

                    break
                }
            }
        }
    }

    private suspend fun resetBasicAdapterInScopeMainUI() {
        withContext(Dispatchers.Main) {
            categoryAdapter = CategoryAdapter(viewModel)
            categoryAdapter.setMode(false)
            binding.rvCategoryList.adapter = categoryAdapter
            resetToOriginalObservers()
        }
    }

    private fun setDialogLayoutStyleAndShow(builder: AlertDialog.Builder) {
        DialogStylingUtil().setDialogMarginAndDisplay(builder)
    }

    private fun goBackToOriginalStatuses() {
        isCleanStatusNow = true
        SelectionAllToggleSelector.isAllSelectChecked = true
        requireActivity().findViewById<BottomNavigationView>(R.id.navigation_main).visibility =
            View.VISIBLE
        binding.toolbarCategory.visibility = View.VISIBLE
        binding.sametoptitle.visibility = View.GONE
        categoryAdapter.deselectAll()
        categoryAdapter.setMode(false)
    }

    private fun resetToOriginalObservers() {
        viewModel.items.observe(viewLifecycleOwner) {
            categoryAdapter.submitList(it)
        }
        viewModel.openCategoryEvent.observe(viewLifecycleOwner, EventObserver {
            if (isCleanStatusNow) {
                openCategoryDetail(it.categoryId, it.label, it.period)
            }
        })
    }

    /*---------------------------------------------*/

    private fun movingSec() {
        Toast.makeText(requireContext(), "이동", Toast.LENGTH_SHORT).show()
        // do later write
    }

    /*---------------------------------------------*/


    /*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }
    */

    /*---------------------------------------------*/

    private fun openCategoryDetail(categoryId: String, categoryLabel: String, categoryPeriod: String) {
        if (isCleanStatusNow) {

            findNavController().navigate(
                R.id.action_category_to_category_detail, bundleOf(
                    KEY_CATEGORY_ID to categoryId,
                    KEY_CATEGORY_LABEL to categoryLabel,
                    KEY_CATEGORY_PERIOD to categoryPeriod
                )
            )

        }

    }


    /*-----------------------------------------------------*/


    override fun onResume() {
        super.onResume()

        if (BFLAG) {
            this.flag = true
            BFLAG = false
        }
        if (this.flag) {
            this.flag = !this.flag
            val mmIntent = Intent(requireContext(), MainComposeActivity::class.java)
            startActivity(mmIntent)
            activity?.finish()
        }

        setBackKeyPressLogicTwiceSafeCheck()   // extra optional fun for stability and safety
    }


    /*------------------------------------------------------*/
    // Added extra for back function

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerCallback(this@CategoryFragment::callback.isInitialized)
        callback.listen()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    // End of back use define
    /*------------------------------------------------------*/

    // Logic functions
    private fun registerCallback(alreadyInitialized: Boolean) {
        if (alreadyInitialized) {
            return
        } else {
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (deleteModeOn) {
                        deletingSec(false)
                    } else {
                        if (this@CategoryFragment::myEndSnackbar.isInitialized) {
                            if (myEndSnackbar.isShown) {
                                (requireActivity() as Activity).finishAndRemoveTask()
                            } else {
                                myEndSnackbar.show()
                            }
                        } else {
                            myEndSnackbar = Snackbar.make(binding.root, "앱을 종료하고 나가시겠습니까?", Snackbar.LENGTH_LONG)
                                .setAction("종료") {
                                    (requireActivity() as Activity).finishAndRemoveTask()
                                }

                            myEndSnackbar.show()
                        }
                    }
                }
            }
        }
    }

    private fun OnBackPressedCallback.listen() {
        if (this@CategoryFragment::callback.isInitialized) {
            requireActivity().onBackPressedDispatcher.addCallback(this@CategoryFragment, this)
        }
    }


    private fun setBackKeyPressLogicTwiceSafeCheck() {
        if (this@CategoryFragment::callback.isInitialized) {
            callback.listen()
        } else {
            registerCallback(this@CategoryFragment::callback.isInitialized)
            callback.listen()
        }
    }

    /*------------------------------------------------------*/


//    companion object {
//        private val PERMISSIONS =
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_NETWORK_STATE,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.INTERNET
//            )
//        // Manifest.permission.ACCESS_BACKGROUND_LOCATION,
//    }

}


/*-----------------------------------------------------*/
object SelectionAllToggleSelector {
    var isAllSelectChecked: Boolean = true
    var singleToneFlag: Boolean = false
}
/*-----------------------------------------------------*/


// Refer
//    private fun getPermissionsRequest() =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//            if (isAllPermissionsGranted(PERMISSIONS)) {
//                when {
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(
//                        getConnectivityMarshmallowManagerCallback()
//                    )
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> marshmallowNetworkAvailableRequest()
//                    else -> {
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                            context.registerReceiver(
//                                networkReceiver,
//                                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
//                            ) // android.net.ConnectivityManager.CONNECTIVITY_ACTION
//                        }
//                    }
//                }//extension function
//                myCallNetworkFunc()
//            } else {
//                myCallNoPermissionsOnNetworkAndLocations()
//            }
//        }

//    private fun myCallNoPermissionsOnNetworkAndLocations() {
//        Log.i("", "")
//    }
//
//    private fun myCallNetworkFunc() {
//        Log.i("", "")
//    }
