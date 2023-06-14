package tecnm.edu.buho_1.user

import java.util.Date

data class Notify(
    val idpost: String = "",
    val nickname: String?  = "",
    val category: String?  = "",
    val timestamp: Date? = null
)
