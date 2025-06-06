package vcmsa.projects.assignment2_prog7313

data class Goal(
    val id: String = "",
    val goalName: String,
    val goalDescription: String,
    val goalCompleted: Boolean,
)

data class WeeklyGoals(
    val goals: List<Goal> = listOf(),
    val weekStartDate: String = "",
)