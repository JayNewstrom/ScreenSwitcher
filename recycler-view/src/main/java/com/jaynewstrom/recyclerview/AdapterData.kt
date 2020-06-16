package com.jaynewstrom.recyclerview

import android.content.Context
import android.util.SparseArray

internal class AdapterData(
    private val list: List<RecyclerItemViewFactory<*>>,
    private val map: SparseArray<RecyclerItemViewFactory<*>>
) {
    companion object {
        fun create(itemViewFactories: List<RecyclerItemViewFactory<*>>, context: Context): AdapterData {
            val localItemViewFactories = itemViewFactories.toList()
            val map = SparseArray<RecyclerItemViewFactory<*>>()
            for (itemViewFactory in localItemViewFactories) {
                map.append(itemViewFactory.layoutResource(context), itemViewFactory)
            }
            return AdapterData(localItemViewFactories, map)
        }
    }

    fun itemCount(): Int = list.size

    fun itemAtPosition(position: Int): RecyclerItemViewFactory<*> = list[position]

    fun itemForViewType(viewType: Int): RecyclerItemViewFactory<*> = map.get(viewType)
}
