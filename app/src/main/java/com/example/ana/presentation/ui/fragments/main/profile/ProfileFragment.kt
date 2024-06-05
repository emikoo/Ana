package com.example.ana.presentation.ui.fragments.main.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.local.PrefsSettings
import com.example.ana.data.model.User
import com.example.ana.databinding.FragmentProfileBinding
import com.example.ana.presentation.extensions.updateLanguage
import com.example.ana.presentation.ui.activity.MainActivity
import com.example.ana.view_model.HomeViewModel
import com.google.android.material.tabs.TabLayout
import com.teenteen.teencash.presentation.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var viewModel: HomeViewModel
    private var flag = true
    private var pictureUrl = ""
    override fun setupViews() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.getUserName(prefs.getCurrentUserId())
        viewModel.getProfilePhoto(prefs.getCurrentUserId())
        setupTabLayout()
        binding.edit.setOnClickListener {
            flag = if (flag) {
                setEditViews()
                false
            } else {
                if (binding.name.text.toString().isBlank()) {
                    Toast.makeText(
                        requireContext(), getString(
                            R.string.please_write_the_name_or_nickname
                        ), Toast.LENGTH_SHORT
                    ).show()
                    false
                } else {
                    saveChanges()
                    setProfileViews()
                    true
                }
            }
        }
        binding.logOut.setOnClickListener {
            progressDialog.show()
            restart()
        }
        binding.camera.setOnClickListener { openGalleryForImage() }
        binding.btnNotification.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToNotificationFragment())
        }
    }

    private fun setStrings() {
        binding.profile.setText(R.string.profile)
        binding.tLogOut.setText(R.string.log_out)
    }

    private fun setEditViews() {
        binding.name.isEnabled = true
        binding.name.background.mutate()
            .setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
        binding.card.visibility = View.VISIBLE
        binding.edit.setBackgroundResource(R.drawable.ic_save)
    }

    private fun setProfileViews() {
        binding.name.isEnabled = false
        binding.name.background.mutate()
            .setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        binding.card.visibility = View.GONE
        binding.edit.setBackgroundResource(R.drawable.ic_edit)
    }

    private fun saveChanges() {
        val name = binding.name.text.toString()
        viewModel.updateUser(prefs.getCurrentUserId(), User(name, photo = pictureUrl))
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 102)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 102) {
            pictureUrl = data?.data.toString()
            Glide.with(this)
                .load(pictureUrl)
                .placeholder(R.drawable.ic_user)
                .into(binding.profilePicture)
        } else binding.tab.getTabAt(0)?.select()
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tab
        tabLayout.setSelectedTabIndicatorHeight(0)
        tabLayout.setBackgroundColor(Color.parseColor("#F0EDFF"))
        tabLayout.removeAllTabs()
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(createTabView(getString(R.string.kazakh)))
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(createTabView(getString(R.string.russian)))
        )
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(createTabView(getString(R.string.english)))
        )
        setInitialTabSelection()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("RestrictedApi")
            override fun onTabSelected(tab: TabLayout.Tab) {
                setSelectedTab(tab)
                when (tab.position) {
                    0 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "kk",
                            requireContext(),
                            prefs
                        )
                    }

                    1 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "ru",
                            requireContext(),
                            prefs
                        )
                    }

                    2 -> {
                        PreferenceManager(requireContext()).updateLanguage(
                            "eng",
                            requireContext(),
                            prefs
                        )
                    }
                }
                setStrings()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setUnselectedTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        setInitialTabSelection()
    }

    private fun setInitialTabSelection() {
        val currentLanguage = prefs.getSettingsLanguage()
        val tabLayout = binding.tab
        when (currentLanguage) {
            "kk" -> {
                tabLayout.getTabAt(0)?.select()
                setSelectedTab(tabLayout.getTabAt(0))
            }

            "ru" -> {
                tabLayout.getTabAt(1)?.select()
                setSelectedTab(tabLayout.getTabAt(1))
            }

            "eng" -> {
                tabLayout.getTabAt(2)?.select()
                setSelectedTab(tabLayout.getTabAt(2))
            }
        }
    }

    private fun createTabView(tabText: String): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)
        val tabTextView = view.findViewById<TextView>(R.id.tab_text)
        tabTextView.text = tabText
        return view
    }

    private fun setSelectedTab(tab: TabLayout.Tab?) {
        val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
        tabTextView?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected)
        tabTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun setUnselectedTab(tab: TabLayout.Tab?) {
        val tabTextView = tab?.customView?.findViewById<TextView>(R.id.tab_text)
        tabTextView?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.tab_unselected)
        tabTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
    }

    private fun restart() {
        prefs.saveCurrentUserId("")
        prefs.setFirstTimeLaunch(PrefsSettings.NOT_AUTH)
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.pop_in, R.anim.pop_out)
        requireActivity().finishAffinity()
        progressDialog.dismiss()
    }

    override fun subscribeToLiveData() {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.name.setText(it)
        }
        viewModel.photo.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) binding.profilePicture.scaleType =
                ImageView.ScaleType.CENTER_CROP
            else binding.profilePicture.scaleType = ImageView.ScaleType.CENTER_INSIDE
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_user)
                .into(binding.profilePicture)
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentProfileBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentProfileBinding.inflate(layoutInflater, container, attachToRoot))
    }
}