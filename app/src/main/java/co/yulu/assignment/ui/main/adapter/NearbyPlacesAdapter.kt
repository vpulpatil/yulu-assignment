package co.yulu.assignment.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import co.yulu.assignment.R
import co.yulu.assignment.network.responsehandlers.Venue
import co.yulu.assignment.network.responsehandlers.suggestedplaces.Item

class NearbyPlacesAdapter(private val list: List<Item>, private val context: Context) : RecyclerView.Adapter<NearbyPlacesAdapter.NearbyPlacesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyPlacesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return NearbyPlacesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NearbyPlacesViewHolder, position: Int) {
        holder.placeNameTV.text = list[position].venue.name
        holder.beenHereCountTV.text = context.getString(R.string.count_been_here_text, list[position].venue.beenHere.count.toString())
    }


    inner class NearbyPlacesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        @BindView(R.id.placeNameTV)
        lateinit var placeNameTV: TextView

        @BindView(R.id.beenHereCountTV)
        lateinit var beenHereCountTV: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}