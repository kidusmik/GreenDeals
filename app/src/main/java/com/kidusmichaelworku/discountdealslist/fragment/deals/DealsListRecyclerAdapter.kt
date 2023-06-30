package com.kidusmichaelworku.discountdealslist.fragment.deals

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kidusmichaelworku.discountdealslist.database.DealModel
import com.kidusmichaelworku.discountdealslist.database.FavoritesModel
import com.kidusmichaelworku.discountdealslist.databinding.LiDealsListBinding

class DealsListRecyclerAdapter(
    private val offersList: List<DealModel>,
    private val viewModel: DealsViewModel
) : RecyclerView.Adapter<DealsListRecyclerAdapter.DealsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealsListViewHolder {
        val binding = LiDealsListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DealsListViewHolder(binding)
    }

    override fun getItemCount() = offersList.size

    override fun onBindViewHolder(holder: DealsListViewHolder, position: Int) {
        with(holder) {
            with(offersList[position]) {
                val title = offersList[position].title ?: "N/A"
                val description = offersList[position].description ?: "N/A"
                val coupon = offersList[position].code ?: "N/A"
                val discountedPrice = offersList[position].offer_value ?: "N/A"
                /** Used [Html] because the data fetched contains HTML contents **/
                binding.tvDescriptionDeals.text =
                    Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
                binding.tvTitleDeals.text = Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT)
                binding.tvCouponCodeDeals.text = Html.fromHtml(coupon, Html.FROM_HTML_MODE_COMPACT)
                binding.tvDiscountedPriceDeals.text =
                    Html.fromHtml(discountedPrice, Html.FROM_HTML_MODE_COMPACT)
                /** Use [Glide] to display the image URL **/
                Glide.with(holder.itemView.context)
                    .load(offersList[position].image_url)
                    .into(binding.ivDeals)
                /** This lets the user add any of the deal in the list to favorites **/
                binding.imageButton.setOnClickListener {
                    /** Converts the data to a [FavoritesModel] entity so
                     * that it can be stored in the database **/
                    val addedDeal = FavoritesModel(
                        offersList[position].lmd_id,
                        offersList[position].store,
                        offersList[position].merchant_homepage,
                        offersList[position].long_offer,
                        offersList[position].title,
                        offersList[position].description,
                        offersList[position].code,
                        offersList[position].terms_and_conditions,
                        offersList[position].categories,
                        offersList[position].featured,
                        offersList[position].publisher_exclusive,
                        offersList[position].url,
                        offersList[position].smartlink,
                        offersList[position].image_url,
                        offersList[position].type,
                        offersList[position].offer,
                        offersList[position].offer_value,
                        offersList[position].status,
                        offersList[position].start_date,
                        offersList[position].end_date
                    )
                    /** Adds the data to the database **/
                    viewModel.addFavorites(addedDeal)

                    Toast.makeText(
                        holder.itemView.context, "Added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                /** This navigates to [DetailFragment] when the user taps on any of the list items **/
                holder.itemView.setOnClickListener {
                    val action: NavDirections =
                        DealsFragmentDirections.actionNavigationDealsToDealsDetailFragment(
                            offersModel = offersList[position]
                        )
                    Navigation.findNavController(it).navigate(action)
                }
            }
        }
    }

    inner class DealsListViewHolder(val binding: LiDealsListBinding) :
        RecyclerView.ViewHolder(binding.root)
}