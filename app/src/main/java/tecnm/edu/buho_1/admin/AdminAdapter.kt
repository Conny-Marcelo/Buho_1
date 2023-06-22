package tecnm.edu.buho_1.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import tecnm.edu.buho_1.CommentActivity
import tecnm.edu.buho_1.R

    class AdminAdapter (private val values: List<NotifyAdmin>) : RecyclerView.Adapter<AdminAdapter.NotifyViewHolder>() {

        inner class NotifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val idTextView: TextView = itemView.findViewById(R.id.idPost_admin)
            private val nicknameTextView: TextView = itemView.findViewById(R.id.item_nickname_notify_admin)
            private val repTextView: TextView = itemView.findViewById(R.id.report_nickname_notify_admin)
            private val catTextView: TextView = itemView.findViewById(R.id.item_categories_admin)
            private val dateTextView: TextView = itemView.findViewById(R.id.item_date_notify_admin)
            private val cardView: CardView = itemView.findViewById(R.id.card_notify_admin)


            fun bind(notify: NotifyAdmin) {
                idTextView.text = notify.idPost
                nicknameTextView.text = notify.nickname
                repTextView.text = notify.nameReport
                catTextView.text = notify.category
                dateTextView.text = notify.timestamp.toString()

                    // Manejar el evento de clic en el CardView
                cardView.setOnClickListener {
                    val cardId = notify.idnot
                    val datoEspecifico = notify.idPost
                    onCardItemClickListener.onItemClick(cardId,datoEspecifico)

                }

            }

        }

        interface OnItemClickListener {
            fun onItemClick(cardId: String, data: String)
        }

        private lateinit var onCardItemClickListener: OnItemClickListener

        fun setOnItemClickListener(listener: OnItemClickListener) {
            this.onCardItemClickListener = listener
        }


        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  NotifyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_notify_admin, parent, false)
            return  NotifyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder:  NotifyViewHolder, position: Int) {
            val comm = values[position]
            holder.bind(comm)
            holder.itemView.setOnClickListener {
                onCardItemClickListener.onItemClick(comm.idnot, comm.idPost )
            }
        }

        override fun getItemCount(): Int {
            return values.size
        }
    }

