package com.kidusmichaelworku.discountdealslist.fragment.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kidusmichaelworku.discountdealslist.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val favoritesViewModel =
        ViewModelProvider(this)[FavoritesViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        //Mock Data
//        val deal1 = DealsModel(1275984, "timesprime.com","https://www.timesprime.com/", "Times Prime Membership @ just &#8377;1199", "Times Prime Membership", "Get it at just &#8377;1199","","", "Entertainment,Entertainment Subscription","No","N", "https://www.timesprime.com/", "https://smartlink.linkmydeals.com/?account_id=18950&network=&url=https%3A%2F%2Fwww.timesprime.com%2F", "https://static.timesprime.com/2x/youtube-logo-box.png","Deal","Offer","Hot Offer","new", "2023-06-19","2023-06-27")
//        val deal2 = DealsModel(12345678, "toliday.in","https://www.toliday.in/","Get Flat &#8377;300 discount on Flight and Hotel Booking","Get &#8377;300 OFF","This voucher is valid on Flight and Hotel Booking","FLAT300","","Travel,Domestic Flights,International Flights,Hotels","No","N","https://toliday.in/","https://smartlink.linkmydeals.com/?account_id=18950&network=&url=https%3A%2F%2Ftoliday.in%2F","https://toliday.in/coupon-code/10.png","Code","Price-Off","&#8377;300","new","2023-02-01","2023-06-25")
//        val dealList: List<DealsModel> = listOf(deal1, deal2)

        favoritesViewModel.getDeals().observe(viewLifecycleOwner) {
            val rvDealsAdapter = FavoritesListRecyclerAdapter(it)

            binding.rvFavoritesList.layoutManager = layoutManager
            binding.rvFavoritesList.adapter = rvDealsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}