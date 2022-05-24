package com.smart.app.ui.categorydetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smart.app.databinding.ItemCategoryTopSellingSectionBinding
import com.smart.app.model.Category
import com.smart.app.model.TopSelling

class CategoryTopSellingSectionAdapter(
    private val categoryPeriod: String?
) :
    ListAdapter<TopSelling, CategoryTopSellingSectionAdapter.TopSellingSectionViewHolder>(TopSellingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSellingSectionViewHolder {
        val binding = ItemCategoryTopSellingSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopSellingSectionViewHolder(binding, categoryPeriod)
    }

    override fun onBindViewHolder(holder: TopSellingSectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    /*-------------------------------------------------------------------------*/

    class TopSellingSectionViewHolder(
        private val binding: ItemCategoryTopSellingSectionBinding,
        private val categoryPeriod: String?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val nestedAdapter = CategoryTopSellingItemAdapter(categoryPeriod)

        init {
            binding.rvCategorySection.adapter = nestedAdapter
        }

        fun bind(topSelling: TopSelling) {
            binding.title = topSelling.title
            binding.executePendingBindings()

            val joinedListTopSellingCategories = appendPlusButtonAtLastAndGetJoinedList(topSelling.categories)
            nestedAdapter.submitList(joinedListTopSellingCategories)
//          nestedAdapter.submitList(topSelling.categories)
        }

        private fun appendPlusButtonAtLastAndGetJoinedList(categoriesParam: List<Category>): List<Category> {

            val mydummyc = Category("", "", "", false, "", "", "")
            val appended = listOf(mydummyc)

            val flexibleJoinedList: MutableList<Category> = ArrayList()
            flexibleJoinedList.addAll(categoriesParam)
            flexibleJoinedList.addAll(appended)

            return flexibleJoinedList.toList()

        }

    }
    /*-------------------------------------------------------------------------*/


}


/*-------------------------------------------------------------------------*/

class TopSellingDiffCallback : DiffUtil.ItemCallback<TopSelling>() {
    override fun areItemsTheSame(oldItem: TopSelling, newItem: TopSelling): Boolean {
        return oldItem.title.text == newItem.title.text
    }

    override fun areContentsTheSame(oldItem: TopSelling, newItem: TopSelling): Boolean {
        return oldItem == newItem
    }

}