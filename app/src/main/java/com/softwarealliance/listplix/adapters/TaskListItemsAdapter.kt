package com.softwarealliance.listplix.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.softwarealliance.listplix.R
import com.softwarealliance.listplix.model.responseapi.Task
import com.softwarealliance.listplix.model.responseapi.User
import kotlinx.android.synthetic.main.item_tasks.view.*

open class TaskListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Task>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_tasks,
                parent,
                false
            )
        )
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_card_name.text = model.title
            holder.itemView.task_desc_tv.text = model.description
            holder.itemView.task_status_tv.text = model.status.toUpperCase()

            if(model.status == "done")
            {
                holder.itemView.task_status_tv.setBackgroundColor(context.resources.getColor(R.color.status_done))
            } else if(model.status == "in progress"){
                holder.itemView.task_status_tv.setBackgroundColor(context.resources.getColor(R.color.status_in_progress))
            } else {
                holder.itemView.task_status_tv.setBackgroundColor(context.resources.getColor(R.color.status_open))
            }

        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * An interface for onclick items.
     */
    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}