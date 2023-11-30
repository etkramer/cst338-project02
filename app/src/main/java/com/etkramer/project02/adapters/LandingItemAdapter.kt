package com.etkramer.project02.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.etkramer.project02.R
import com.etkramer.project02.db.AppDatabase
import com.etkramer.project02.db.Product
import com.etkramer.project02.db.UserProductEdge

@SuppressLint("NotifyDataSetChanged")
class LandingItemAdapter(private val context: Context, private val data: LiveData<List<Product>>) : RecyclerView.Adapter<LandingItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById(R.id.name_text) as TextView
        val descriptionText = view.findViewById(R.id.description_text) as TextView
        val addCartButton = view.findViewById(R.id.add_cart_button) as Button
        val deleteButton = view.findViewById(R.id.delete_button) as Button
    }

    init {
        data.observeForever { l ->
            l.let {
                this.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.landing_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data.value?.get(position)
        holder.nameText.text = "${item?.name} ($${item?.price})"
        holder.descriptionText.text = item?.description ?: "null"

        val db = AppDatabase.getInstance(context)
        holder.deleteButton.visibility =
            if (db.getCurrentUserOrNull(context)?.isAdmin == true) View.VISIBLE else View.INVISIBLE

        holder.deleteButton.setOnClickListener {
            if (item != null) {
                db.productDao().delete(item)
                Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
            }
        }

        val currentUser = db.getCurrentUserOrNull(context)
        val isInCart = db.userProductEdgeDao().findWithIds(currentUser?.id as Int, item?.id as Int)?.isInCart ?: false

        if (isInCart) {
            holder.addCartButton.isEnabled = false
            holder.addCartButton.text = "Already in cart"
        } else {
            holder.addCartButton.isEnabled = true
            holder.addCartButton.text = "Add to Cart"
        }

        holder.addCartButton.setOnClickListener {
            val existingEdge = db.userProductEdgeDao().findWithIds(currentUser.id, item.id)
                ?: UserProductEdge(0, currentUser.id, item.id, true)

            db.userProductEdgeDao().insert(existingEdge.copy(isInCart = true))
            this.notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return data.value?.size ?: 0
    }
}
