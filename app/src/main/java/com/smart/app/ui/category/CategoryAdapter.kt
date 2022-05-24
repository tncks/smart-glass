@file:Suppress(
    "SameParameterValue", "ReplaceManualRangeWithIndicesCalls", "LiftReturnOrAssignment",
    "KotlinConstantConditions"
)

package com.smart.app.ui.category

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smart.app.R
import com.smart.app.databinding.FragmentCategoryBinding
import com.smart.app.databinding.ItemCategoryBinding
import com.smart.app.model.Category
import com.smart.app.ui.ProfileAddEditActivity
import com.smart.app.ui.common.CategoryDiffCallback
import com.smart.app.util.leftDrawable

class CategoryAdapter(private val viewModel: CategoryViewModel) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {

    private var mIsInMultiChoiceMode = false
    private var mIsInSingleClickMode = false
    private var mItemList: MutableMap<Int, State>? = LinkedHashMap()
    private var mListener: Listener? = null
    private var mRecyclerView: RecyclerView? = null
    private var tmpItems: MutableList<View> = mutableListOf()
    private var tmpAbsolutes: MutableList<Int> = mutableListOf()
    private lateinit var binding2: FragmentCategoryBinding
    private lateinit var sheetBehavior2: BottomSheetBehavior<LinearLayout>


    /*-----------------------------------------------------------------*/

    fun getmItemListCheckEmpty(): Boolean {
        return getSelectedItemListExternal().isEmpty()
    }


    fun setMode(isModeEnabled: Boolean) {
        this@CategoryAdapter.mIsInMultiChoiceMode = isModeEnabled
        if (isModeEnabled) {
            var idx = 0
            for (tmpItem in tmpItems) {
                if ((mIsInMultiChoiceMode || mIsInSingleClickMode)) {
                    tmpItem.setOnClickListener {
                        processSingleClick(tmpAbsolutes[idx++])
                    }
                    tmpItem.setOnLongClickListener {
                        true
                    }
                }
            }
            processSingleClick(0)
        }
    }

    fun initBinding(bindingParam: FragmentCategoryBinding) {
        this.binding2 = bindingParam
    }

    fun initSheetBehavior(sheetBehaviorParam: BottomSheetBehavior<LinearLayout>) {
        this.sheetBehavior2 = sheetBehaviorParam
    }


    /*-----------------------------------------------------------------*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
        val mCurrentView: View = holder.itemView
        tmpItems.add(mCurrentView)
        tmpAbsolutes.add(holder.bindingAdapterPosition)
        if ((mIsInMultiChoiceMode || mIsInSingleClickMode)) {
            mCurrentView.setOnClickListener {
                processSingleClick(holder.bindingAdapterPosition)
            }
        } else if (defaultItemViewClickListener(holder, position) != null) {
            Log.i("dummy", "dummy")
        }

        processUpdate(mCurrentView, holder.bindingAdapterPosition)

        setUpShowPopUpMenuPerEveryItemOnLongClickOneSecond(holder, position)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView

        super.onAttachedToRecyclerView(recyclerView)
    }


    /*-----------------------------------------------------------------*/

    private fun setUpShowPopUpMenuPerEveryItemOnLongClickOneSecond(
        holder: CategoryAdapter.CategoryViewHolder,
        position: Int
    ) {

        holder.itemView.setOnLongClickListener {

            val popup =
                PopupMenu(
                    holder.itemView.context,
                    holder.itemView,
                    Gravity.CENTER
                )
            popup.menuInflater.inflate(R.menu.list_item_popup, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                val menuIndex = when (item.itemId) {
                    R.id.mItem01 -> 0
                    R.id.mItem02 -> 1
                    else -> 2
                }
                when (menuIndex) {
                    0 -> {
                        if (!mIsInMultiChoiceMode) {
                            val mIntent = Intent(holder.binding.root.context, ProfileAddEditActivity::class.java)
                            mIntent.putExtra("mIndex", position)
                            holder.binding.root.context.startActivity(mIntent)
                        }
                    }
                    1 -> {
                        Log.i("dummy", "dummy")
                    }
                    2 -> {
                        Log.i("dummy", "dummy")
                    }
                }
                true
            }
            popup.show()
            true
        }

    }

    /*-----------------------------------------------------------------*/


    fun selectAll() {
        performAll(Action.SELECT)
    }

    fun deselectAll() {
        performAll(Action.DESELECT)
    }

    private fun performAll(action: Action) {
        val selectedItems: Int
        val state: State
        if (action == Action.SELECT) {
            selectedItems = mItemList!!.size
            state = State.ACTIVE
        } else {
            selectedItems = 0
            state = State.INACTIVE
        }
        for (i in mItemList!!.keys) {
            mItemList!![i] = state
        }

        updateMultiChoiceMode(selectedItems)
        processNotifyDataSetChanged(0)
        if (mListener != null) {
            if (action == Action.SELECT) {
                mListener!!.onSelectAll(getSelectedItemListInternal().size, mItemList!!.size)
            } else {
                mListener!!.onDeselectAll(getSelectedItemListInternal().size, mItemList!!.size)
            }
        }
    }

    /*-----------------------------------------------------------------*/

    private fun getSelectedItemListInternal(): List<Int> {

        val selectedList: MutableList<Int> = ArrayList()
        for (item: Map.Entry<Int, State> in mItemList!!) {
            if (item.value == State.ACTIVE) {
                selectedList.add(item.key)
            }
        }
        return selectedList
    }

    private fun getSelectedItemListExternal(): List<Int> {
        if (mItemList.isNullOrEmpty()) {
            return ArrayList()
        } else {
            try {
                return getSelectedItemListInternal()
            } catch (e: Exception) {
                return ArrayList()
            }

        }

    }

    fun fakeViewUpdateGone() {
        this.mIsInMultiChoiceMode = false
        this.mIsInSingleClickMode = false
        try {
            val selectedItemList = getSelectedItemListExternal()
            if (selectedItemList.isEmpty()) {
                return
            } else {
                this.viewModel.updateFakeCategoryTmp(selectedItemList, this.tmpAbsolutes)
                for (i in 0 until selectedItemList.size) {
                    notifyItemRemoved(this.tmpAbsolutes[selectedItemList[i]])
                }
                val lastIndexAtSizeArr = selectedItemList.size - 1
                notifyItemRangeChanged(0, this.tmpAbsolutes[selectedItemList[lastIndexAtSizeArr]])
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return
        } catch (e: NullPointerException) {
            return
        } catch (e: Exception) {
            return
        }
    }


    private fun processSingleClick(position: Int) {
        if (mIsInMultiChoiceMode || mIsInSingleClickMode) {
            processClick(position)
        }
    }


    @Suppress("SameParameterValue")
    fun processStartClick(position: Int) {
        if (!mIsInMultiChoiceMode && !mIsInSingleClickMode) {
            processClick(position)
        }
    }

    private fun processUpdate(view: View, position: Int) {
        if (mItemList!!.containsKey(position)) {
            if (mItemList!![position] == State.ACTIVE) {
                setActive(view, true)
            } else {
                setActive(view, false)
            }
        } else {
            mItemList!![position] = State.INACTIVE
            processUpdate(view, position)
        }
    }


    private fun processClick(position: Int) {
        if (mItemList!!.containsKey(position)) {
            if (mItemList!![position] == State.ACTIVE) {
                perform(Action.DESELECT, position, true)
            } else {
                perform(Action.SELECT, position, true)
            }
        }
    }


    private fun perform(
        action: Action,
        position: Int,
        withCallback: Boolean
    ) {
        if (action == Action.SELECT) {
            mItemList!![position] = State.ACTIVE
        } else {
            mItemList!![position] = State.INACTIVE
        }
        val selectedListSize = getSelectedItemListInternal().size

        if (selectedListSize == itemCount && this@CategoryAdapter::binding2.isInitialized) {
            SelectionAllToggleSelector.isAllSelectChecked = false
            val bottomBarContainer: LinearLayout = binding2.root.findViewById(R.id.bottom_sheet_persistent)
            (bottomBarContainer.findViewById<TextView>(R.id.take_image_label)).leftDrawable(R.drawable.ic_checkmark1)
        } else {
            if (this@CategoryAdapter::binding2.isInitialized) {
                SelectionAllToggleSelector.isAllSelectChecked = true
                val bottomBarContainer: LinearLayout = binding2.root.findViewById(R.id.bottom_sheet_persistent)
                (bottomBarContainer.findViewById<TextView>(R.id.take_image_label)).leftDrawable(R.drawable.ic_unchecked)
            }
        }

        updateMultiChoiceMode(selectedListSize)
        processNotifyDataSetChanged(0)
        if (mListener != null && withCallback) {
            if (action == Action.SELECT) {
                mListener!!.onItemSelected(position, selectedListSize, mItemList!!.size)
            } else {
                mListener!!.onItemDeselected(position, selectedListSize, mItemList!!.size)
            }
        }
    }

    @Suppress("unused")
    private fun processNotifyDataSetChanged() {
    }

    private fun processNotifyDataSetChanged(justClickedAt: Int) {

        if (mRecyclerView != null) {
            notifyItemRangeChanged(0, itemCount, null)
            if (this@CategoryAdapter::binding2.isInitialized && this@CategoryAdapter::sheetBehavior2.isInitialized) {
                sheetBehavior2.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                binding2.executePendingBindings()
                sheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }


    private fun updateMultiChoiceMode(justSelectedListSize: Int) {
        mIsInMultiChoiceMode = true
        processNotifyDataSetChanged(0)
    }


    private fun setActive(view: View, state: Boolean) {
        val imageViewThumbnail: ImageView? = view.findViewById(R.id.iv_category_thumbnail)
        val tickImage: ImageView? = view.findViewById(R.id.tick_image)
        if (state) {
            imageViewThumbnail?.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
            imageViewThumbnail?.scaleX = 0.96f
            imageViewThumbnail?.scaleY = 0.96f
            tickImage?.visibility = View.VISIBLE
        } else {
            imageViewThumbnail?.colorFilter = null
            imageViewThumbnail?.scaleX = 1f
            imageViewThumbnail?.scaleY = 1f
            tickImage?.visibility = View.INVISIBLE
        }
    }


    private fun defaultItemViewClickListener(
        justHolder: CategoryAdapter.CategoryViewHolder,
        position: Int
    ): View.OnClickListener? {
        if (position == 1000) {
            return null
        }
        return View.OnClickListener {
            Log.i("dummy", "dummy")
        }
    }


    /*-----------------------------------------------------------------*/

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.viewModel = viewModel
            binding.category = category
            binding.executePendingBindings()
        }
    }

    /*-----------------------------------------------------------------*/

    interface Listener {
        fun onItemSelected(selectedPosition: Int, itemSelectedCount: Int, allItemCount: Int)
        fun onItemDeselected(deselectedPosition: Int, itemSelectedCount: Int, allItemCount: Int)
        fun onSelectAll(itemSelectedCount: Int, allItemCount: Int)
        fun onDeselectAll(itemSelectedCount: Int, allItemCount: Int)
    }

    /*-----------------------------------------------------------------*/

    enum class Action {
        SELECT, DESELECT
    }

    enum class State {
        ACTIVE, INACTIVE
    }


}


/*-----------------------------------------------------------------*/
/*-----------------------------------------------------------------*/
// Add Later on necessary

//    private fun select(position: Int): Boolean {
//        if (mItemList!!.containsKey(position) && mItemList!![position] == State.INACTIVE) {
//            perform(Action.SELECT, position, true)
//            return true
//        }
//        return false
//    }
//
//
//    private fun deselect(position: Int): Boolean {
//        if (mItemList!!.containsKey(position) && mItemList!![position] == State.ACTIVE) {
//            perform(Action.DESELECT, position, true)
//            return true
//        }
//        return false
//    }
//    private fun setItemList(itemList: LinkedHashMap<Int, State>?) {
//        mItemList = itemList
//    }
/*
private fun isSelectableInMultiChoiceMode(position: Int): Boolean {
        return true
    }
private fun setSingleClickMode(set: Boolean) {
        mIsInSingleClickMode = set
        processNotifyDataSetChanged()
    }


    private fun getSelectedItemCount(): Int {

        return getSelectedItemListInternal().size
    }


    private fun getSelectedItemList(): List<Int> {
        return getSelectedItemListInternal()
    }

    private fun setMultiChoiceSelectionListener(listener: Listener?) {
        mListener = listener
    }


    private fun isInSingleClickMode(): Boolean {
        return mIsInSingleClickMode
    }
 */
// Deprecated, unuse
//    private fun processUpdateFakeInternal(view: View, position: Int) {
//        if (mItemList!!.containsKey(position)) {
//            if (mItemList!![position] == State.ACTIVE) {
//                val crl: ConstraintLayout = view.findViewById(R.id.crtmp)
//                crl.visibility = View.GONE
//            }
//        }
//    }
// original, code before changed, origin form
/*
private fun defaultItemViewClickListener(
        holder: CategoryAdapter.CategoryViewHolder,
        position: Int
    ): View.OnClickListener? {
        if (position == 100000) {
            return null
        }
        return View.OnClickListener {
            Log.i("dummy", "dummy")
//            Toast.makeText(
//                holder.binding.root.context,
//                "navigate openCategoryDetail at item position ( $position )",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }
 */
// Deprecated
/*
@Suppress("unused")
    private fun processNotifyDataSetChanged() {
//        if (mRecyclerView != null) {
//            Log.i("dummy", "dummy")
//        }
    }
 */