package co.yulu.assignment.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import co.yulu.assignment.R
import co.yulu.assignment.application.base.BaseViewHolder
import co.yulu.assignment.database.entity.Venue
import com.squareup.picasso.Picasso

class PlacesAdapter(
    private val list: List<Venue>,
    private val context: Context
) :
    RecyclerView.Adapter<PlacesAdapter.QueryResultViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return QueryResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QueryResultViewHolder, position: Int) {
        holder.configure(position)
    }


    inner class QueryResultViewHolder(view: View): BaseViewHolder(view) {

        @BindView(R.id.placeNameTV)
        lateinit var placeNameTV: TextView

        @BindView(R.id.distanceAwayTV)
        lateinit var distanceAwayTV: TextView

        @BindView(R.id.addressTV)
        lateinit var addressTV: TextView

        @BindView(R.id.isVerifiedPlaceTV)
        lateinit var isVerifiedPlaceTV: TextView

        @BindView(R.id.categoryLabelTV)
        lateinit var categoryLabelTV: TextView

        @BindView(R.id.placeIconIV)
        lateinit var placeIconIV: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun configure(position: Int) {
            val venue = list[position]

            placeNameTV.text = venue.name

            val distance = venue.location?.distance ?: 0
            if (distance < 1000) {
                distanceAwayTV.text = context.getString(R.string.distance_in_meter_away, distance.toString())
            } else {
                distanceAwayTV.text = context.getString(R.string.distance_in_kms_away, distance.div(1000).toString())
            }

            if (venue.location?.address?.isNotEmpty() == true) {
                addressTV.visibility = View.VISIBLE
                addressTV.text = venue.location?.address
            } else {
                addressTV.visibility = View.GONE
                addressTV.text = venue.location?.address
            }

            if (venue.verified == true) {
                isVerifiedPlaceTV.setBackgroundResource(R.drawable.verified_bg)
                isVerifiedPlaceTV.text = context.getString(R.string.verified_text)
            } else {
                isVerifiedPlaceTV.setBackgroundResource(R.drawable.unverified_bg)
                isVerifiedPlaceTV.text = context.getString(R.string.unverified_text)
            }

            if (venue.categories?.isNotEmpty() == true) {
                val category = venue.categories!![0]
                categoryLabelTV.visibility = View.VISIBLE
                categoryLabelTV.text = category.shortName
                val iconUrl = "${category.icon?.prefix}bg_64${category.icon?.suffix}"
                Picasso.get().load(iconUrl).error(R.drawable.default_place_icon).into(placeIconIV)
            } else {
                categoryLabelTV.visibility = View.GONE
            }

        }
    }
}