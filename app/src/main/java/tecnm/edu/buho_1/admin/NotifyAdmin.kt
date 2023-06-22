package tecnm.edu.buho_1.admin

import java.util.Date

data class NotifyAdmin(
    val idnot: String = "",
    val idPost: String = "",
    val nickname: String?  = "",
    val nameReport: String?  = "",
    val category: String?  = "",
    val timestamp: Date? = null
)
