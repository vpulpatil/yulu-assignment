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

class QueryResultAdapter(private val list: List<Venue>, private val context: Context) : RecyclerView.Adapter<QueryResultAdapter.QueryResultViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return QueryResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: QueryResultViewHolder, position: Int) {
        holder.placeNameTV.text = list[position].name
        holder.beenHereCountTV.text = context.getString(R.string.count_been_here_text, list[position].beenHere.count.toString())
    }


    inner class QueryResultViewHolder(view: View): RecyclerView.ViewHolder(view) {

        @BindView(R.id.placeNameTV)
        lateinit var placeNameTV: TextView

        @BindView(R.id.beenHereCountTV)
        lateinit var beenHereCountTV: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}