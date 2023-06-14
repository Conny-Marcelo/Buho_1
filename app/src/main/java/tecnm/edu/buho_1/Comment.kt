package tecnm.edu.buho_1

import java.util.Date

data class Comment(
    val id: String = "",
    val nickname: String = "",
    val comment: String = "",
    val timestamp: Date? = null
)
