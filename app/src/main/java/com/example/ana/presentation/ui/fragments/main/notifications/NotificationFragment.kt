package com.example.ana.presentation.ui.fragments.main.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ana.databinding.FragmentNotificationBinding
import com.example.ana.presentation.ui.adapters.ArticleAdapter
import com.example.ana.presentation.ui.adapters.NotificationAdapter
import com.example.ana.view_model.NotificationViewModel
import com.teenteen.teencash.presentation.base.BaseFragment

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    lateinit var adapter: NotificationAdapter
    lateinit var viewModel: NotificationViewModel

    override fun setupViews() {
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        viewModel.getNotifications()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun subscribeToLiveData() {
        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter = NotificationAdapter(it.toMutableList())
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun attachBinding(
        list: MutableList<FragmentNotificationBinding>,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) {
        list.add(FragmentNotificationBinding.inflate(layoutInflater, container, attachToRoot))
    }
}