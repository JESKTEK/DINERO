package vcmsa.projects.assignment2_prog7313

data class Category(
    val id: String = "",
    val catName: String, //Name of the Category
    val dateCreated: String, //Date of Budget Created
    val emailAssociated: String, //Email of Associated User
    val amountSpent: Double, //Amount Spent by User in Budget
    var amountBudgeted: Double //Amount Budgeted by User
)