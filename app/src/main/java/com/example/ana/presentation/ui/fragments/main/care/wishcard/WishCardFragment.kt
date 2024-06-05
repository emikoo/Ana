package com.example.ana.presentation.ui.fragments.main.care.wishcard

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ana.R
import com.example.ana.data.model.CATEGORIES_SECTION
import com.example.ana.data.model.GALLERY_SECTION
import com.example.ana.data.model.ITEM_SECTION
import com.example.ana.data.model.WishCard
import com.example.ana.data.model.cardList
import com.example.ana.databinding.FragmentWishCardBinding
import com.example.ana.presentation.ui.adapters.WishCardAdapter
import com.example.ana.presentation.ui.adapters.WishCardSelector
import com.example.ana.view_model.WishCardViewModel
import com.google.android.material.tabs.TabLayout
import com.teenteen.teencash.presentation.base.BaseFragment

class WishCardFragment : BaseFragment<FragmentWishCardBinding>(),
    WishCardSelector {

    lateinit var adapter: WishCardAdapter
    lateinit var viewModel: WishCardViewModel
    var itemSection = CATEGORIES_SECTION
    var sectionName = ""
    var imageId = ""

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[WishCardViewModel::class.java]
        setupRecyclerView()
        binding.btnBack.setOnClickListener {
            when (itemSection) {
                CATEGORIES_SECTION -> goBack()
                ITEM_SECTION -> {
                    adapter.update(cardList)
                    binding.titleSection.visibility = View.GONE
                    itemSection = CATEGORIES_SECTION
                }

                GALLERY_SECTION -> {
                    viewModel.getSectionItems(prefs.getCurrentUserId(), sectionName)
                    binding.tab.visibility = View.GONE
                    binding.titleSection.visibility = View.VISIBLE
                    itemSection = ITEM_SECTION
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = WishCardAdapter(cardList, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
    }

    private fun goBack() {
        findNavController().navigate(WishCardFragmentDirections.actionWishCardFragmentToSelfCareFragment())
    }

    override fun attachBinding(
        list: MutableList<FragmentWishCardBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentWishCardBinding.inflate(layoutInflater, container, attachToRoot))
    }

    override fun onSectorSelected(sector: WishCard) {
        progressDialog.show()

        when (sector.itemSection) {
            CATEGORIES_SECTION -> {
                binding.tab.visibility = View.GONE
                binding.titleSection.visibility = View.VISIBLE
                sectionName = sector.name.toString()
                binding.titleSection.text = getString(R.string.section) + " $sectionName"
                viewModel.getSectionItems(prefs.getCurrentUserId(), sectionName)
                itemSection = ITEM_SECTION
            }

            ITEM_SECTION -> {
                binding.tab.visibility = View.VISIBLE
                setupTabLayout()
                binding.titleSection.visibility = View.GONE
                imageId = sector.name.toString()
                viewModel.getAlbumPictures()
                itemSection = GALLERY_SECTION
            }

            GALLERY_SECTION -> {
                binding.tab.visibility = View.GONE
                binding.titleSection.visibility = View.VISIBLE
                viewModel.uploadPicture(
                    prefs.getCurrentUserId(),
                    sectionName,
                    imageId,
                    sector.image.toString()
                )
                viewModel.getSectionItems(prefs.getCurrentUserId(), sectionName)
                itemSection = ITEM_SECTION
            }
        }
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tab
        tabLayout.setSelectedTabIndicatorHeight(0)
        tabLayout.setBackgroundColor(Color.parseColor("#F0EDFF"))
        tabLayout.removeAllTabs()
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTabView(getString(R.string.album))))
        tabLayout.addTab(
            tabLayout.newTab().setCustomView(createTabView(getString(R.string.download)))
        )
        tabLayout.getTabAt(0)?.select()
        setSelectedTab(tabLayout.getTabAt(0))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setSelectedTab(tab)
                if (tab.position == 1) openGalleryForImage()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setUnselectedTab(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
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

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, RESULT_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RESULT_GALLERY) {
            val imageUri = data?.data
            viewModel.uploadPicture(
                prefs.getCurrentUserId(),
                sectionName,
                imageId,
                imageUri.toString()
            )
            viewModel.getSectionItems(prefs.getCurrentUserId(), sectionName)
            setupItemSection()
        } else binding.tab.getTabAt(0)?.select()
    }

    private fun setupItemSection() {
        itemSection = ITEM_SECTION
        binding.tab.visibility = View.GONE
        binding.titleSection.visibility = View.VISIBLE
    }

    override fun subscribeToLiveData() {
        viewModel.sectionItems.observe(viewLifecycleOwner) {
            adapter.update(it.toMutableList())
            progressDialog.dismiss()
        }
        viewModel.albumPictures.observe(viewLifecycleOwner) {
            adapter.update(it.toMutableList())
            progressDialog.dismiss()
        }
    }

    companion object {
        private const val RESULT_GALLERY = 102
    }
}