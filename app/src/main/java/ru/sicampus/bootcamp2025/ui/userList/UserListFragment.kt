package ru.sicampus.bootcamp2025.ui.userList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.squareup.picasso.Picasso
import ru.sicampus.bootcamp2025.R
import ru.sicampus.bootcamp2025.databinding.FragmentFreeVolounteersListBinding
import ru.sicampus.bootcamp2025.utils.collectWithLifecycle

class FragmentFreeVolounteersListFragment : Fragment(R.layout.fragment_free_volounteers_list) {
    private var _viewBinding: FragmentFreeVolounteersListBinding? = null
    private val viewBinding: FragmentFreeVolounteersListBinding get() = _viewBinding!!

    private val viewModel by viewModels<FragmentFreeVolounteersViewModel> { FragmentFreeVolounteersViewModel.Factory }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _viewBinding = FragmentFreeVolounteersListBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.collectWithLifecycle(this) { state ->
        }


    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

}