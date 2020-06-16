package com.jaynewstrom.recyclerview

interface RecyclerViewData {
    fun update(viewFactories: List<RecyclerItemViewFactory<*>>)
}
