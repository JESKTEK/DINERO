package vcmsa.projects.assignment2_prog7313


import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import vcmsa.projects.assignment2_prog7313.R
import vcmsa.projects.assignment2_prog7313.databinding.RecyclerCategoryLayoutBinding
import java.io.ByteArrayInputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CategoryAdapter (private var catList: List<Category>): RecyclerView.Adapter<CategoryAdapter.categoryViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()
    class categoryViewHolder(val binding: RecyclerCategoryLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryViewHolder {
        val binding = RecyclerCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return categoryViewHolder(binding)
        //val view = LayoutInflater.from(parent.context)
        //    .inflate(R.layout.recycler_item_layout, parent, false)
        //return UserViewHolder(view)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun onBindViewHolder(holder: categoryViewHolder, position: Int) {
        val category = catList[position]

        holder.binding.tvAddExpenseButton.setOnClickListener {
            onAddExpenseClick(category, position, holder)
        }

        //holder.binding.tvProfileImage.setImageResource(post.uploadImage)
        holder.binding.tvName.text = category.catName
        holder.binding.tvDate.text = category.dateCreated
        val amtSpentText = "R" + "%.2f".format(category.amountSpent)
        holder.binding.tvAmtSpent.text = amtSpentText
        val amtBudgetText = "R" + "%.2f".format(category.amountBudgeted)
        holder.binding.tvAmtBudget.text =  amtBudgetText
    }

    override fun getItemCount() = catList.size

    public fun onAddExpenseClick(category: Category, position: Int, holder: categoryViewHolder) {
        //val postChoice = postList[position]
        //postChoice.likes++
        return
    }

    fun updateData(newList: List<Category>) {
        catList = newList
        notifyDataSetChanged()
    }
}