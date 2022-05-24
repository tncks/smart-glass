package com.smart.app.ui.categorydetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.app.R
import com.smart.app.common.BUTTON_TYPE
import com.smart.app.common.CONTENT_TYPE
import com.smart.app.common.MY_BTN_JPG_URL_ADD_PLUS
import com.smart.app.databinding.ItemCategoryTopSellingBinding
import com.smart.app.model.Category
import com.smart.app.ui.common.CategoryDiffCallback
import com.smart.app.util.*

class CategoryTopSellingItemAdapter(
    private val categoryPeriod: String?
) :
    ListAdapter<Category, CategoryTopSellingItemAdapter.TopSellingItemViewHolder>(CategoryDiffCallback()) {


    var selectionIndex: Int = 1         // should be public var, not private
    var prevSelectionPointer: View? = null  // should be public var, not private
    var prevBottomSelectionPointerUnder: View? = null  // should be public var, not private

    private var myItemCount: Int = 1
    private var myTagDispenser: Int = 1
    private lateinit var myDays: List<String>
    private lateinit var myExpandableDaysForAdd: MutableList<String>

    init {
        setMyCount()
    }

    private fun setMyCount() {
        this.myItemCount = 1
        categoryPeriod?.let {
            var loggingStr = it.filter { a ->
                !a.isWhitespace()
            }

            val myTarget = '.'
            loggingStr = loggingStr.replace(myTarget, '-')

            val begins = loggingStr.split("~")[0].dropLast(1)
            val ends = loggingStr.split("~")[1].dropLast(1)

            this.myDays = getDatesBetweenModernNew(convertingForm(begins), convertingForm(ends))
            this.myExpandableDaysForAdd = this.myDays.toMutableList()
            this.myItemCount += this.myDays.size
            this.myTagDispenser = this.myItemCount
        }
    }


    /*  Description
    // in onCreateViewHolder block skip condition check because of same behavior
    // These comments are Description for condition
    // if   (viewType == CONTENT_TYPE) do same
    // elif (viewType == BUTTON_TYPE)  do same
    // else 404 do same
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSellingItemViewHolder {

        val binding = ItemCategoryTopSellingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopSellingItemViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: TopSellingItemViewHolder, position: Int) {

        holder.setnDayth(position + 1)
        holder.bind(getItem(position))

    }


    override fun getItemViewType(position: Int): Int {

        if (itemCount == 1 && position == 0) {
            return 404
        }

        return if (position == itemCount - 1) {
            BUTTON_TYPE
        } else {
            CONTENT_TYPE
        }

    }


    override fun getItemCount(): Int {
        return this@CategoryTopSellingItemAdapter.myItemCount
    }


    /*-----------------------------------------------------------------------*/


    inner class TopSellingItemViewHolder(
        private val binding: ItemCategoryTopSellingBinding,
        private val viewType: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var nDayth: Int = 1

        fun bind(category: Category) {
            try {
                when (viewType) {
                    CONTENT_TYPE -> {
                        doContentTypeUiSetting(category)
                    }
                    BUTTON_TYPE -> {
                        doButtonTypeUiSetting()
                    }
                    else -> {
                        doButtonTypeUiSetting()
                    }
                }
            } finally {
                // IMPORTANT FINALLY DO
                binding.executePendingBindings()
            }

        }


        fun setnDayth(position: Int) {
            this@TopSellingItemViewHolder.nDayth = position
        }


        private fun doContentTypeUiSetting(category: Category) {

            // 1일차, 2일차, 3일차 ... on and on
            binding.category = category

            uiSettingLogicSubroutine()

            setInitialDefaultTextColorSelectedStyleOnFirst() // 처음에 한번만 실행되는 함수, 첫번째 1일차 색상 set 용도

            setStyleHideWithUnnecessaryImageView()

            setDayStringTextDaythAutoIncreDefaultOn(
                this@TopSellingItemViewHolder.nDayth.toString(),
                this@TopSellingItemViewHolder.nDayth
            )  // n일차 텍스트 설정 함수

        }

        private fun uiSettingLogicSubroutine() {

            binding.root.findViewById<TextView>(R.id.tv_category_top_selling_label).setOnClickListener {
                if (prevSelectionPointer != null) {
                    (prevSelectionPointer as TextView).setTextColor(getIntegerBlackColorCode())
                }
                (it as TextView).setTextColor(getIntegerPrimaryColorCode())

                prevSelectionPointer = it

                if (prevBottomSelectionPointerUnder != null) {
                    (prevBottomSelectionPointerUnder as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                binding.root.findViewById<TextView>(R.id.tv_category_date_period)
                    .bottomDrawable(R.drawable.ic_nbar, R.dimen.indicationbar)

                prevBottomSelectionPointerUnder = binding.root.findViewById<TextView>(R.id.tv_category_date_period)

            }

        }

        private fun setInitialDefaultTextColorSelectedStyleOnFirst() {
            if (this@TopSellingItemViewHolder.nDayth == selectionIndex) {

                binding.root.findViewById<TextView>(R.id.tv_category_top_selling_label)
                    .setTextColor(getIntegerPrimaryColorCode())
                binding.root.findViewById<TextView>(R.id.tv_category_date_period)
                    .bottomDrawable(R.drawable.ic_nbar, R.dimen.indicationbar)


                prevSelectionPointer =
                    (binding.root.findViewById<TextView>(R.id.tv_category_top_selling_label) as View)
                prevBottomSelectionPointerUnder =
                    (binding.root.findViewById<TextView>(R.id.tv_category_date_period) as View)
                selectionIndex = -10
            }
        }

        @Suppress("LiftReturnOrAssignment")
        private fun setDayStringTextDaythAutoIncreDefaultOn(
            nDaythStringValue: String,
            nDaythParam: Int
        ) {
            val resultJoinedStringValue = "$nDaythStringValue 일차"
            binding.root.findViewById<TextView>(R.id.tv_category_top_selling_label).text = resultJoinedStringValue
            var start = ""
            try {

                val b = this@CategoryTopSellingItemAdapter::myDays.isInitialized
                start = periodLogic(start, nDaythParam, b, myDays)

            } catch (e: IndexOutOfBoundsException) {

                start = showAddedDatesAgainTry(start, nDaythParam)


            } finally {

                binding.root.findViewById<TextView>(R.id.tv_category_date_period).text = start

            }

        }

        @Suppress("LiftReturnOrAssignment")
        private fun showAddedDatesAgainTry(param: String, nDaythParam: Int): String {
            var start = param

            try {

                val b = this@CategoryTopSellingItemAdapter::myExpandableDaysForAdd.isInitialized
                start = periodLogic(start, nDaythParam, b, myExpandableDaysForAdd)

            } catch (e: IndexOutOfBoundsException) {
                start = myExpandableDaysForAdd.last()
            }
            return start

        }

        private fun periodLogic(param: String, nDaythParam: Int, b: Boolean, paramDays: List<String>): String {
            var start = param
            categoryPeriod?.let {
                if (b) {
                    start = paramDays[nDaythParam - 1]
                    start = start.substring(start.indexOf("-") + 1)
                    val reverseTarget = '-'
                    start = start.replace(reverseTarget, '.')
                    start += "."
                    val indexAt = start.indexOf(".") + 1
                    start = addChar(start, ' ', indexAt)
                }
            }
            return start
        }


        private fun doButtonTypeUiSetting() {

            val btnThumbnailImageUrl = MY_BTN_JPG_URL_ADD_PLUS
            setUpAddButtonImageAndUrl(btnThumbnailImageUrl)

            setStyleHideWithUnnecessaryTextView()

            binding.root.findViewById<ImageView>(R.id.iv_category_top_selling_image).apply {

                tag = (++myTagDispenser)

                setOnClickListener {
                    if (this@CategoryTopSellingItemAdapter::myExpandableDaysForAdd.isInitialized) {

                        if (myExpandableDaysForAdd.size < 35) {
                            doAddWork()
                        } else {
                            doNotWorkAndHideMyPlusButton()
                        }

                    }

                }

            }

        }


        @Suppress("SameParameterValue")
        private fun setUpAddButtonImageAndUrl(btnThumbnailImageUrl: String) {
            binding.category = Category(
                "dummy",
                "  ",
                btnThumbnailImageUrl,
                false,
                "",
                "",
                ""
            )
        }

        private fun setStyleHideWithUnnecessaryImageView() {
            binding.root.findViewById<ImageView>(R.id.iv_category_top_selling_image).visibility = View.INVISIBLE
        }

        private fun setStyleHideWithUnnecessaryTextView() {
            binding.root.findViewById<TextView>(R.id.tv_category_date_period).visibility = View.INVISIBLE
        }

        @Suppress("LiftReturnOrAssignment")
        private fun doAddWork() {

            var lastDate = ""

            try {

                this@CategoryTopSellingItemAdapter.myItemCount++


                if (this@CategoryTopSellingItemAdapter.myDays.size == this@CategoryTopSellingItemAdapter.myExpandableDaysForAdd.size) {

                    lastDate = getTommorowOfLastDate(getLastDayOfDates(this@CategoryTopSellingItemAdapter.myDays))

                } else {

                    lastDate =
                        getTommorowOfLastDate(getLastDayOfDates(this@CategoryTopSellingItemAdapter.myExpandableDaysForAdd))

                }


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

                if (lastDate.isNotBlank()) {
                    this@CategoryTopSellingItemAdapter.myExpandableDaysForAdd.add(lastDate)
                } else {
                    this@CategoryTopSellingItemAdapter.myItemCount--
                }




                binding.tvCategoryTopSellingBadge.apply {
                    visibility = View.VISIBLE
                    visibility = View.GONE
                }

                binding.tvCategoryTopSellingBadge.apply {
                    visibility = View.VISIBLE
                    visibility = View.INVISIBLE
                }

            }

        }

        private fun doNotWorkAndHideMyPlusButton() {
            try {
                binding.root.findViewWithTag<ImageView>(myTagDispenser).visibility = View.INVISIBLE
            } catch (e: Exception) {
                Log.i("dummy", "dummy")
            }
        }

        private fun getLastDayOfDates(myDays: List<String>): String {
            return myDays.last()
        }

        private fun getTommorowOfLastDate(param: String): String {
            return DateIncrementer().addOneDay(param)
        }


    }

    /*-----------------------------------------------------------------------*/


}


// 혹시 모를 오류에 대비한 복사 붙여넣기 로직 백업 코드
/*
            binding.root.findViewById<TextView>(R.id.tv_category_top_selling_label).setOnClickListener {
                if (prevSelectionPointer != null) {
                    (prevSelectionPointer as TextView).setTextColor(getIntegerBlackColorCode())
                }
                (it as TextView).setTextColor(getIntegerPrimaryColorCode())

                prevSelectionPointer = it

                if (prevBottomSelectionPointerUnder != null) {
                    (prevBottomSelectionPointerUnder as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
                binding.root.findViewById<TextView>(R.id.tv_category_date_period)
                    .bottomDrawable(R.drawable.ic_nbar, R.dimen.indicationbar)

                prevBottomSelectionPointerUnder = binding.root.findViewById<TextView>(R.id.tv_category_date_period)

            }
             */

/*
            binding.root.findViewById<ImageView>(R.id.iv_category_top_selling_image).tag = (++myTagDispenser)


            binding.root.findViewById<ImageView>(R.id.iv_category_top_selling_image).setOnClickListener {
                if (this@CategoryTopSellingItemAdapter::myExpandableDaysForAdd.isInitialized) {

                    if (myExpandableDaysForAdd.size < 35) {
                        doAddWork()
                    } else {
                        doNotWorkAndHideMyPlusButton()
                    }

                }

            }

             */

//                binding.tvCategoryTopSellingBadge.visibility = View.VISIBLE
//                binding.tvCategoryTopSellingBadge.visibility = View.GONE
//                binding.tvCategoryTopSellingBadge.visibility = View.VISIBLE
//                binding.tvCategoryTopSellingBadge.visibility = View.INVISIBLE