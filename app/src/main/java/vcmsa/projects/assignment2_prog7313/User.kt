package vcmsa.projects.assignment2_prog7313

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, //User's name
    val email: String, //User's Email
)