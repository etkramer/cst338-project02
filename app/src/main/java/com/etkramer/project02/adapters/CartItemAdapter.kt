package com.etkramer.project02.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.etkramer.project02.R
import com.etkramer.project02.db.AppDatabase
import com.etkramer.project02.db.Product
import com.etkramer.project02.db.UserProductEdge

@SuppressLint("NotifyDataSetChanged")
class CartItemAdapter(private val context: Context, private val data: LiveData<List<Product>>) : RecyclerView.Adapter<CartItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val nameText = view.findViewById(R.id.name_text) as TextView
        val removeCartButton = view.findViewById(R.id.remove_cart_button) as Button
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
            .inflate(R.layout.cart_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data.value?.get(position) ?: throw Exception()
        holder.nameText.text = "${item.name} ($${item.price})"

        val db = AppDatabase.getInstance(context)
        val currentUser = db.getCurrentUserOrNull(context) ?: throw Exception()

        holder.removeCartButton.setOnClickListener {
            val existingEdge = db.userProductEdgeDao().findWithIds(currentUser.id, item.id)
                ?: UserProductEdge(0, currentUser.id, item.id, false)

            db.userProductEdgeDao().insert(existingEdge.copy(isInCart = false))
            this.notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return data.value?.size ?: 0
    }
}
