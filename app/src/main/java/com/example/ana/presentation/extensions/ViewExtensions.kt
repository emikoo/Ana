package com.example.ana.presentation.extensions

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.show(fragmentManager: FragmentManager?) {
    val bottomSheetDialogFragment: BottomSheetDialogFragment = this
    fragmentManager?.let {
        bottomSheetDialogFragment.show(
            it ,
            bottomSheetDialogFragment.tag
        )
    }
}