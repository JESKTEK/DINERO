package vcmsa.projects.assignment2_prog7313

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import vcmsa.projects.assignment2_prog7313.R
import vcmsa.projects.assignment2_prog7313.databinding.RecyclerCategoryLayoutBinding
import vcmsa.projects.assignment2_prog7313.databinding.RecyclerExpenseLayoutBinding
import java.io.ByteArrayInputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class ExpenseAdapter(private var expenseList: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.expenseViewHolder>() {

    val orderedExpenses = mutableListOf<Expense>()

    private val firestore = FirebaseFirestore.getInstance()

    class expenseViewHolder(val binding: RecyclerExpenseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): expenseViewHolder {
        val binding =
            RecyclerExpenseLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return expenseViewHolder(binding)
        //val view = LayoutInflater.from(parent.context)
        //    .inflate(R.layout.recycler_item_layout, parent, false)
        //return UserViewHolder(view)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun onBindViewHolder(holder: expenseViewHolder, position: Int) {

        val expense = expenseList[position]

        holder.binding.tvViewDetailsButton.setOnClickListener {
            onViewDetailsClick(expense, position, holder)
        }

        //holder.binding.tvProfileImage.setImageResource(post.uploadImage)
        holder.binding.tvCategoryName.text = expense.categoryName
        holder.binding.tvCategoryItem.text = expense.itemName
        holder.binding.tvDate.text = expense.dateCreated
        val amtSpentText = "R" + "%.2f".format(expense.amountSpent)
        holder.binding.tvAmtSpent.text = amtSpentText

        try {
            val imageBytes = Base64.decode(expense.uploadImage)
            val decodedBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            if (decodedBitmap != null) {
                holder.binding.tvReceiptImage.setImageBitmap(decodedBitmap)
            } else {
                holder.binding.tvReceiptImage.setImageResource(R.drawable.ic_launcher_foreground)
            }
        } catch (e: Exception) {
            holder.binding.tvReceiptImage.setImageResource(R.drawable.ic_launcher_foreground)
        }

        //val imageBytes = Base64.decode(expense.uploadImage)
        //val decodedImage = BitmapFactory.decodeStream(ByteArrayInputStream(imageBytes))
        //holder.binding.tvReceiptImage.setImageBitmap(decodedImage)
    }

    override fun getItemCount() = expenseList.size

    public fun onViewDetailsClick(expense: Expense, position: Int, holder: expenseViewHolder) {
        val expenseChoice = expenseList[position]
        val stringDetails = expenseChoice.details
        AlertDialog.Builder(holder.itemView.context).setMessage(stringDetails)
            .setPositiveButton("Okay") { dialog, _ ->
                dialog.dismiss()
            }.show()
        return
    }

    fun updateData(newList: List<Expense>) {
        expenseList = newList
        notifyDataSetChanged()
    }
}