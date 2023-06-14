package tecnm.edu.buho_1.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.user.User

class UserAdapter( private val usuarios: List<User>, listener : OnItemClickListener) : RecyclerView.Adapter<UserAdapter.UsuarioViewHolder>() {

    private val listener: OnItemClickListener
    init {
        this.listener = listener
    }

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nicknameTextView: TextView = itemView.findViewById(R.id.nicknameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailText)
        val btnDelete: ImageButton = itemView.findViewById(R.id.imageButtonDeleteUser)

        fun bind(usuario: User) {
            nicknameTextView.text = usuario.nombre
            emailTextView.text = usuario.email

            btnDelete.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)

        return UsuarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)

        holder.btnDelete.setOnClickListener {
            listener?.onDeleteClick(position)
        }


    }

    override fun getItemCount(): Int {
        return usuarios.size
    }


}