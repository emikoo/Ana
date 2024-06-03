package com.example.ana.presentation.ui.fragments.main.home.child

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.ana.R
import com.example.ana.data.model.Child
import com.example.ana.databinding.LayoutAuthChildBinding
import com.example.ana.presentation.base.BaseBottomSheetDialogFragment
import com.example.ana.view_model.HomeViewModel
import com.google.android.datatransport.BuildConfig
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

interface UpdateData {
    fun updateChildrenList()
}

class ChildAuthBottomSheet(private val updater: UpdateData) :
    BaseBottomSheetDialogFragment<LayoutAuthChildBinding>() {

    private var state = Screen.AGE
    private var currentPhotoPath = ""
    private lateinit var viewModel: HomeViewModel

    enum class Screen {
        AGE, NAME, IMAGE, ENDING, DIARY
    }

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupAgeScreen()
        binding.back.setOnClickListener { this.dismiss() }
        binding.button1.setOnClickListener {
            when (state) {
                Screen.AGE -> {
                    setupNameScreen()
                }

                Screen.NAME -> {
                    if (binding.input.text.toString().isNotBlank()) setupImageScreen()
                    else Toast.makeText(requireContext(), getString(R.string.please_write_the_name_or_nickname), Toast.LENGTH_LONG).show()
                }

                Screen.IMAGE -> {
                    openGalleryForImage()
                }

                Screen.ENDING -> {
                    this.dismiss()
                }

                Screen.DIARY -> {
                    this.dismiss()
                }
            }
        }

        binding.skip.setOnClickListener {
            addChild(Child(binding.input.text.toString(), getDate()))
            setupEndingScreen()
        }

        binding.button2.setOnClickListener {
            if (state == Screen.IMAGE) requestCameraPermission()
            if (state == Screen.DIARY) this.dismiss()
        }
    }

    private fun setupAgeScreen() {
        state = Screen.AGE
        binding.title.text = getString(R.string.when_was_the_baby_born)
        binding.button1.text = getString(R.string.next)
        binding.datePicker.maxDate = System.currentTimeMillis()
    }

    private fun setupNameScreen() {
        state = Screen.NAME
        binding.textField.visibility = View.VISIBLE
        binding.datePicker.visibility = View.GONE
        binding.skip.visibility = View.VISIBLE
        binding.title.text = getString(R.string.what_is_your_child_s_name)
    }

    private fun setupImageScreen() {
        state = Screen.IMAGE
        binding.textField.visibility = View.GONE
        binding.skip.visibility = View.VISIBLE
//        binding.button2.visibility = View.VISIBLE
        binding.image.visibility = View.VISIBLE
        binding.title.text = getString(R.string.add_profile_photo)
        binding.button1.text = getString(R.string.upload_from_device)
//        binding.button2.text = "Take a photo"
        binding.image.setBackgroundResource(R.drawable.ic_baby_pic)
    }

    private fun setupEndingScreen() {
        state = Screen.ENDING
        binding.title.text = getString(R.string.thank_you_for_provided_information)
        binding.textField.visibility = View.GONE
        binding.button1.visibility = View.GONE
//        binding.button2.visibility = View.GONE
        binding.skip.visibility = View.INVISIBLE
        binding.image.visibility = View.VISIBLE
        binding.image.setBackgroundResource(R.drawable.ic_tick_ending)
        progressDialog.dismiss()
    }

    private fun setupDiaryScreen() {
        state = Screen.DIARY
        binding.button2.visibility = View.VISIBLE
        binding.image.setBackgroundResource(R.drawable.ic_notes)
        binding.title.text = "Do you want to get started on your baby’s diary?"
        binding.button1.text = "Get started!"
        binding.button2.text = "Not Now"
    }

    private fun getDate(): String {
        var month = (binding.datePicker.month + 1).toString()
        var day = binding.datePicker.dayOfMonth.toString()
        val year = binding.datePicker.year.toString()
        if (day.toInt() < 10) {
            day = "0$day"
        }
        if (month.toInt() < 10) {
            month = "0$month"
        }
        return "$day/$month/$year"
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RESULT_GALLERY)
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            RESULT_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_GALLERY && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGalleryForImage()
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }

        if (requestCode == RESULT_CAMERA && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.camera_and_storage_permissions_are_needed_to_take_pictures),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        photoFile?.also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "${BuildConfig.APPLICATION_ID}.provider",
                it
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, RESULT_CAMERA)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    private fun uploadImageToFirebase(contentUri: Uri?) {
        contentUri ?: return

        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        val inputStream = requireActivity().contentResolver.openInputStream(contentUri)
        val uploadTask = inputStream?.let { stream ->
            refStorage.putStream(stream)
        }

        uploadTask?.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                progressDialog.show()
                val imageUrl = uri.toString()
                addChild(Child(binding.input.text.toString(), getDate(), imageUrl))
                setupEndingScreen()
            }
        }?.addOnFailureListener { exception ->
            Log.e("Storage", "Upload failed", exception)
            Toast.makeText(
                requireContext(),
                "Upload failed: ${exception.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun addChild(child: Child) {
        viewModel.addChild(prefs.getCurrentUserId(), child)
        updater.updateChildrenList()
    }

    override fun attachBinding(
        list: MutableList<LayoutAuthChildBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(LayoutAuthChildBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_GALLERY) {
            val imageUri = data?.data
            uploadImageToFirebase(imageUri)
        }
        if (requestCode == RESULT_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageUri = Uri.fromFile(File(currentPhotoPath))
            uploadImageToFirebase(imageUri)
        }
    }

    companion object {
        private const val RESULT_CAMERA = 101
        private const val RESULT_GALLERY = 102
    }
}