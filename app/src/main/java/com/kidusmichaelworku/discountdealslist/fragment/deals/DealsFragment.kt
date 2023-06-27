package com.kidusmichaelworku.discountdealslist.fragment.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kidusmichaelworku.discountdealslist.R
import com.kidusmichaelworku.discountdealslist.database.DealModel
import com.kidusmichaelworku.discountdealslist.databinding.FragmentDealsBinding
import com.kidusmichaelworku.discountdealslist.services.DealsNetwork
import com.kidusmichaelworku.discountdealslist.services.DealsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dealsService = DealsNetwork.getRetrofitClient().create(DealsService::class.java)
        val dealsViewModel = ViewModelProvider(this)[DealsViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        binding.buttonSortByStore.setOnClickListener {
            dealsViewModel.getDealsSortedByStore().observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.nothing_to_filter_by_store),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val rvDealsAdapter = DealsListRecyclerAdapter(it, dealsViewModel)

                    binding.rvDealsList.layoutManager = layoutManager
                    binding.rvDealsList.adapter = rvDealsAdapter

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.offers_are_sorted_by_store),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.buttonSortByDate.setOnClickListener {
            dealsViewModel.getDealsSortedByDate().observe(viewLifecycleOwner) {
                if (it.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Nothing to Filter by Date",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val rvDealsAdapter = DealsListRecyclerAdapter(it, dealsViewModel)

                    binding.rvDealsList.layoutManager = layoutManager
                    binding.rvDealsList.adapter = rvDealsAdapter

                    Toast.makeText(
                        requireContext(),
                        "Offers are sorted by Date",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        //Use coroutines to make the API calls in a separate thread
        dealsViewModel.getDeals().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
//                fetchData = true
                CoroutineScope(Dispatchers.IO).launch {
                    // Get list of deals
                    val offers = dealsService.getOffers().offers

                    for (offer in offers) {
                        val dealModel = DealModel(
                            offer.lmd_id,
                            offer.store,
                            offer.merchant_homepage,
                            offer.long_offer,
                            offer.title,
                            offer.description,
                            offer.code,
                            offer.terms_and_conditions,
                            offer.categories,
                            offer.featured,
                            offer.publisher_exclusive,
                            offer.url,
                            offer.smartlink,
                            offer.image_url,
                            offer.type,
                            offer.offer,
                            offer.offer_value,
                            offer.status,
                            offer.start_date,
                            offer.end_date
                        )
                        dealsViewModel.insertDeal(dealModel)
                    }
                }
                dealsViewModel.getDeals().observe(viewLifecycleOwner) { deal ->
                    val rvDealsAdapter = DealsListRecyclerAdapter(deal, dealsViewModel)

                    binding.rvDealsList.layoutManager = layoutManager
                    binding.rvDealsList.adapter = rvDealsAdapter
                }
            } else {
                val rvDealsAdapter = DealsListRecyclerAdapter(it, dealsViewModel)

                binding.rvDealsList.layoutManager = layoutManager
                binding.rvDealsList.adapter = rvDealsAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}