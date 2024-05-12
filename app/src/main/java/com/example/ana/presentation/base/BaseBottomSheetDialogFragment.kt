package com.example.ana.presentation.base

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.presentation.utills.ProgressDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

@Suppress("DEPRECATION")
abstract class BaseBottomSheetDialogFragment<VB_CHILD : ViewBinding> :
    BottomSheetDialogFragment() {

    private var _binding: VB_CHILD? = null
    lateinit var binding: VB_CHILD
    lateinit var progressDialog: Dialog
    lateinit var prefs: PrefsSettings
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var usersCollection: CollectionReference
    var currentUser: FirebaseUser? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        (requireView().parent.parent.parent as View).fitsSystemWindows = false
//        val touchOutsideView = dialog!!.window!!.decorView
//            .findViewById<View>(com.google.android.material.R.id.touch_outside)
//        touchOutsideView.setOnClickListener(null)
//    }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ) = getInflatedView(inflater , container , false)

    private fun getInflatedView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    ): View {
        val tempList = mutableListOf<VB_CHILD>()
        attachBinding(tempList , inflater , container , attachToRoot)
        this._binding = tempList[0]
        binding = _binding as VB_CHILD
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext() , theme)
        dialog.setOnShowListener { it ->
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.isDraggable = false
                val layoutParams = it.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        init()
        setupViews()
    }

    abstract fun setupViews()

    private fun init() {
        db = FirebaseFirestore.getInstance()
        prefs = PrefsSettings(requireActivity())
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        progressDialog = ProgressDialog.progressDialog(requireActivity())
        usersCollection = db.collection("users")
    }

    override fun onDestroy() {
        super.onDestroy()
        this._binding = null
    }

    abstract fun attachBinding(
        list: MutableList<VB_CHILD> ,
        layoutInflater: LayoutInflater ,
        container: ViewGroup? ,
        attachToRoot: Boolean
    )
}