package com.jaynewstrom.screenswitchersample.viewpdf

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.github.barteksc.pdfviewer.PDFView
import com.jaynewstrom.screenswitchersample.core.inflate

internal class ViewPdfPresenter private constructor(private val view: View, component: ViewPdfComponent) {
    companion object {
        fun createView(context: Context, container: ViewGroup, component: ViewPdfComponent): View {
            val view = container.inflate(layoutResId = R.layout.view_pdf_view, context = context)
            ViewPdfPresenter(view, component)
            return view
        }
    }

    init {
        view.findViewById<PDFView>(R.id.pdf_view).fromAsset("Jarvis.pdf").load()
    }
}
