package tecnm.edu.buho_1.user

import java.util.Date

data class Item(val id: String? = null,  val nickname: String? = null, val location: String? = null, val share: String? = null,
                val category: String? = null, val timestamp: Date? = null, val description: String? = null, val url: String? = null)