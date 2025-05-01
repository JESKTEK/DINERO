package vcmsa.projects.assignment2_prog7313

data class Expense(
    val id: String = "",
    val parentId: String = "",
    val categoryName: String, //Name of the Category
    val itemName: String, //Name of the Category
    val amountSpent: Double, //Amount Spent by User in Budget
    val dateCreated: String, //Date of Budget Created
    val uploadImage: String, //Drawable Resource ID
    val details: String, //Drawable Resource IDd
    val emailAssociated: String, //Email of Associated User
)