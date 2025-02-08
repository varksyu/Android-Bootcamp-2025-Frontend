package ru.sicampus.bootcamp2025.ui.centerCard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.databinding.FragmentCenterCardBinding
import ru.sicampus.bootcamp2025.databinding.FragmentProfileBinding
import ru.sicampus.bootcamp2025.ui.profile.ProfileViewModel
import ru.sicampus.bootcamp2025.ui.userList.VolunteerAdapter
import ru.sicampus.bootcamp2025.utils.collectWithLifecycle

class CenterCardFragment: BottomSheetDialogFragment(R.layout.fragment_center_card) {
    private var _viewBinding: FragmentCenterCardBinding? = null
    private val viewBinding: FragmentCenterCardBinding get() = _viewBinding!!

    private val viewModel by viewModels<CenterCardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentCenterCardBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val centerName = arguments?.getString("centerName") // Получаем id центра
        if (centerName != null) {
            viewModel.saveName(centerName) // Загружаем данные центра
        }


        viewBinding.refresh.setOnClickListener { viewModel.clickRefresh() }
        viewBinding.close.setOnClickListener { viewModel.clickClose() }
        viewBinding.joinTheCenter.setOnClickListener { viewModel.joinTheCenter() }


        val adapter = ActiveVolunteerAdapter()
        viewBinding.activeVolunteersList.adapter = adapter


        viewModel.state.collectWithLifecycle(this) { state ->

            viewBinding.error.visibility =
                if (state is CenterCardViewModel.State.Error) View.VISIBLE else View.GONE
            viewBinding.loading.visibility =
                if (state is CenterCardViewModel.State.Loading) View.VISIBLE else View.GONE
            viewBinding.content.visibility =
                if (state is CenterCardViewModel.State.Show) View.VISIBLE else View.GONE

            when (state) {
                is CenterCardViewModel.State.Loading -> Unit
                is CenterCardViewModel.State.Show -> {
                    viewBinding.nameCenter.text = state.item.name
                    viewBinding.description.text = state.item.description
                    //adapter.submitList(state.item.)

                    //if (state.items.avatarUrl != null) Picasso.get().load(state.items.avatarUrl).resize(100, 100).centerCrop().into(viewBinding.avatar)
                }

                is CenterCardViewModel.State.Error -> {
                    viewBinding.errorText.text = state.text
                }
            }

        }
    }
}