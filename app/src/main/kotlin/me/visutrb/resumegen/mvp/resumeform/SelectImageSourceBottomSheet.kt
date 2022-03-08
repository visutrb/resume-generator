package me.visutrb.resumegen.mvp.resumeform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.visutrb.resumegen.databinding.BottomSheetSelectImageSourceBinding

class SelectImageSourceBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSelectImageSourceBinding

    var onOptionSelected: ((dialog: SelectImageSourceBottomSheet, option: Int) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSelectImageSourceBinding.inflate(inflater, container, false)
        binding.cameraOption.setOnClickListener { onOptionSelected?.invoke(this, OPTION_CAMERA) }
        binding.galleryOption.setOnClickListener { onOptionSelected?.invoke(this, OPTION_GALLERY) }
        return binding.root
    }

    companion object {
        const val TAG = "SelectImageSourceBottomSheet"

        const val OPTION_CAMERA = 1
        const val OPTION_GALLERY = 2
    }
}