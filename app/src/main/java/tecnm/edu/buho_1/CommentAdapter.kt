package tecnm.edu.buho_1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CommentAdapter ( private val comment: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var buttonReport: Button

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nicknameTextView: TextView = itemView.findViewById(R.id.item_nickname_comment)
        private val commTextView: TextView = itemView.findViewById(R.id.item_comment)

        fun bind(comment: Comment) {
            nicknameTextView.text = comment.nickname
            commTextView.text = comment.comment

        }

    }

    fun filtrarComentariosPorIdCard(idCard: String) {
        comment.filter { comentario ->
            comentario.id == idCard
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_comment, parent, false)

        buttonReport = itemView.findViewById(R.id.button_report_comment)

        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comm = comment[position]
        holder.bind(comm)

        buttonReport.setOnClickListener {

            // Acceder al contexto de la aplicación
            val contexto = holder.itemView.context

            // Ejemplo de Toast
            Toast.makeText(contexto, "Comentario reportado", Toast.LENGTH_SHORT).show()

            val id = (contexto as Activity).intent.getStringExtra("id")
            val idata = id.toString()

            //Instanciar Firebase y crear la colección
            val db = FirebaseFirestore.getInstance()
            val collectionRef1 = db.collection("comment_report")

            // Crear un nuevo documento
            val data1 = hashMapOf(
                "idpost" to idata,
                "nickname" to comm.nickname,
                "timestamp" to FieldValue.serverTimestamp()

            )

            collectionRef1.add(data1)
                .addOnSuccessListener { documentReference ->
                    // El documento fue creado con éxito
                    Log.d(ContentValues.TAG, "Documento creado con ID: ${documentReference.id}")
                    //Toast.makeText( context, "Documento creado con ID: ${documentReference.id}", Toast.LENGTH_SHORT ).show()

                }
                .addOnFailureListener { e ->
                    // Error al crear el documento
                    Log.w(ContentValues.TAG, "Error al crear el documento", e)
                    //Toast.makeText(context, "Error al crear el documento", Toast.LENGTH_SHORT).show()
                }

        }


    }

    override fun getItemCount(): Int {
        return comment.size
    }
}

