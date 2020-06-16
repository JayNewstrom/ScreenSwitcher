package com.jaynewstrom.recyclerview

import androidx.recyclerview.widget.DiffUtil

internal class AdapterUpdatingRecyclerViewData(private val adapter: RecyclerViewAdapter) : RecyclerViewData {
    override fun update(viewFactories: List<RecyclerItemViewFactory<*>>) {
        val newAdapterData = AdapterData.create(viewFactories, adapter.context)
        val diffResult = DiffUtil.calculateDiff(DiffCallback(adapter.data, newAdapterData))
        adapter.setData(newAdapterData, diffResult)
    }
}

private class DiffCallback(
    private val oldData: AdapterData,
    private val newData: AdapterData
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldData.itemCount()

    override fun getNewListSize(): Int = newData.itemCount()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData.itemAtPosition(oldItemPosition).isSameItemAs(newData.itemAtPosition(newItemPosition))
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData.itemAtPosition(oldItemPosition).hasSameContentsAs(newData.itemAtPosition(newItemPosition))
    }
}
