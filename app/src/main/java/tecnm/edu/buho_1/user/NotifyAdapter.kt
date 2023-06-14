package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import tecnm.edu.buho_1.R

    class NotifyAdapter (private val values: List<Notify>) : RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder>() {

        inner class NotifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val nicknameTextView: TextView = itemView.findViewById(R.id.item_nickname_notify)
            private val catTextView: TextView = itemView.findViewById(R.id.item_categories)
            private val dateTextView: TextView = itemView.findViewById(R.id.item_date_notify)
            private val cardView: CardView = itemView.findViewById(R.id.card_notify)
            private val idTextView: TextView = itemView.findViewById(R.id.idPost)

            fun bind(notify: Notify) {
                nicknameTextView.text = notify.nickname
                catTextView.text = notify.category
                dateTextView.text = notify.timestamp.toString()
                idTextView.text = notify.idpost

                // Manejar el evento de clic en el CardView
                cardView.setOnClickListener {
                    val datoEspecifico = notify.idpost
                    onCardItemClickListener.onCardItemClick(datoEspecifico)
                }
            }

        }


        interface OnCardItemClickListener {
            fun onCardItemClick(data: String)
        }

        private lateinit var onCardItemClickListener: OnCardItemClickListener


        fun setOnCardItemClickListener(listener: OnCardItemClickListener) {
            this.onCardItemClickListener = listener
        }


        @SuppressLint("MissingInflatedId")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  NotifyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_notify, parent, false)
            return  NotifyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder:  NotifyViewHolder, position: Int) {
            val comm = values[position]
            holder.bind(comm)

            holder.itemView.setOnClickListener {
                onCardItemClickListener.onCardItemClick(comm.idpost)
            }

        }

        override fun getItemCount(): Int {
            return values.size
        }


    }

