package com.kidusmichaelworku.greendeals.fragment.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kidusmichaelworku.greendeals.R
import com.kidusmichaelworku.greendeals.database.DealModel
import com.kidusmichaelworku.greendeals.databinding.FragmentDealsBinding
import com.kidusmichaelworku.greendeals.services.DealsNetwork
import com.kidusmichaelworku.greendeals.services.DealsService
import com.kidusmichaelworku.greendeals.services.Offers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null

    /** This property is only valid between onCreateView and onDestroyView. **/
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
        /** Handles what the Sort By Store button does **/
        binding.buttonSortByStore.setOnClickListener {
            dealsViewModel.getDealsSortedByStore().observe(viewLifecycleOwner) {
                /** Check if the Deals Table in the database is empty or not.
                 * If the table is not empty then this will sort the items by Store name.
                 */
                if (it.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.nothing_to_filter_by_store),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    displayDealsList(it, dealsViewModel, layoutManager)

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.offers_are_sorted_by_store),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        /** Handles what the Sort By Date button does **/
        binding.buttonSortByDate.setOnClickListener {
            dealsViewModel.getDealsSortedByDate().observe(viewLifecycleOwner) {
                /** Checks if the Deals table in the database is empty or not.
                 * If the table is not empty then this will store the items by Start date.
                 */
                if (it.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.nothing_to_filter_by_date),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    displayDealsList(it, dealsViewModel, layoutManager)

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.offers_are_sorted_by_date),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        /** Use coroutines to make the API calls in a separate thread **/
        dealsViewModel.getDeals().observe(viewLifecycleOwner) {
            /** Check if the Deals Table in the database is empty or not.
             * If the table is empty then this will fetch data from the API.
             */
            if (it.isEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    /** Fetch the list of offers from the API **/
                    val offers = dealsService.getOffers().offers

                    storeOffersToDatabase(offers, dealsViewModel)
                }
            }
            displayDealsList(it, dealsViewModel, layoutManager)
        }
    }

    /**
     * Converts the list of offers fetched from the API to [DealModel] and store it to the
     * database.
     */
    private fun storeOffersToDatabase(
        offers: List<Offers>,
        dealsViewModel: DealsViewModel
    ) {
        for (offer in offers) {
            val dealModel = DealModel(offer)
            /** Insert the offer into the Deal Table of the database **/
            dealsViewModel.insertDeal(dealModel)
        }
    }

    /**
     * Passes the list to the [RecyclerView] and displays the list
     */
    private fun displayDealsList(
        it: List<DealModel>,
        dealsViewModel: DealsViewModel,
        layoutManager: RecyclerView.LayoutManager
    ) {
        val rvDealsAdapter = DealsListRecyclerAdapter(it, dealsViewModel, requireContext())

        binding.rvDealsList.layoutManager = layoutManager
        binding.rvDealsList.adapter = rvDealsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}