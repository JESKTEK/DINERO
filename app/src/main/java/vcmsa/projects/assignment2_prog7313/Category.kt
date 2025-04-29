package vcmsa.projects.assignment2_prog7313

data class Category(
    val id: String = "",
    val catName: String, //Drawable Resource ID, image uploaded with post
    val dateCreated: String, //Caption of Post
    val emailAssociated: String, //Email of Associated User
    val amountSpent: Long, //Date of Post
    var amountBudgeted: Long //Number of Likes
)