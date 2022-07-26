package com.ahmety.pokedex.ui

import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ahmety.pokedex.databinding.FragmentMainBinding
import com.ahmety.pokedex.model.Pokemon
import com.ahmety.pokedex.ui.adapter.MainAdapter
import com.ahmety.pokedex.ui.adapter.MainLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var mainAdapter: MainAdapter? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CODE = 10000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBarVisibility()
       // setUI()
       // observeUI()
        setupAdapter()
        setupList()
        setupClickListener()

    }

    override fun onResume() {
        super.onResume()
        setUI()
    }

    private fun initActionBarVisibility() {
        if (checkDrawOverlayPermission()) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        }
    }

    private fun setUI() {
        binding.givePermissionBtn.isVisible = !checkDrawOverlayPermission()
        binding.recyclerViewMain.isVisible = checkDrawOverlayPermission()
        if (checkDrawOverlayPermission()) {
            setupList()
        }
    }

    private fun setupAdapter() {
        binding.givePermissionBtn.isVisible = !checkDrawOverlayPermission()
        binding.recyclerViewMain.isVisible = checkDrawOverlayPermission()
        mainAdapter = MainAdapter(::onClickPokemon)
        binding.recyclerViewMain.adapter = mainAdapter?.withLoadStateHeaderAndFooter(
            header = MainLoadStateAdapter { mainAdapter?.retry() },
            footer = MainLoadStateAdapter { mainAdapter?.retry() }
        )

        binding.recyclerViewMain.apply {
            setHasFixedSize(true)
        }
    }

    private fun setupList() {
        lifecycleScope.launch {
            homeViewModel.listData.collectLatest { pagedData ->
                mainAdapter?.submitData(pagedData)
            }
        }
    }

  /*  private fun observeUI() {
        adapter = MainAdapter(::onClickPokemon)
        binding.recyclerViewMain.adapter = adapter
        homeViewModel.pokemonList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.txtError.isVisible = false
                    //binding.animationViewLoading.isVisible = false
                    val data = it.data
                  //  binding.textView.text = "success"
                    adapter?.submitList(data?.results)
                }
                is Resource.Error -> {
                    binding.txtError.isVisible = true
                    //binding.animationViewLoading.isVisible = false
                    it.message?.let { message ->
                         binding.txtError.text = message
                        //Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    //binding.animationViewLoading.isVisible = true
                }
            }
        }
    } */

    private fun onClickPokemon(pokemon: Pokemon) {
        /*
                findNavController().apply {
                currentDestination?.getAction(R.id.action_mainFragment_to_customerFragment)?.run {
                    navigate(R.id.action_mainFragment_to_customerFragment)
                }
            }
         */
    }

    private fun setupClickListener() {
        binding.apply {
            givePermissionBtn.setOnClickListener{
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${requireActivity().packageName}")
                )
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    private fun checkDrawOverlayPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        return Settings.canDrawOverlays(requireContext())
    }

    @Deprecated("Deprecated in Java")
    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(requireContext())) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            }
        }
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            (AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW == op &&
                    requireActivity().packageName.equals(requireContext().packageName))) {
            // proceed to back to your app
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainAdapter = null
    }

}