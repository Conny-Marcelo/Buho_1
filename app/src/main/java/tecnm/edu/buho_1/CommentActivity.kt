package tecnm.edu.buho_1


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.admin.UserAdapter
import tecnm.edu.buho_1.user.PrincipalPublicationsActivity
import tecnm.edu.buho_1.user.User


class CommentActivity : AppCompatActivity() {

    var MODE_PRIVATE = 0

    private lateinit var replyToUser: TextView

    private lateinit var comment: EditText

    private lateinit var commentBtn: ImageButton

    private lateinit var backImage: ImageButton

    var ide: String =""
    var nickname: String = ""
    var description: String = ""

    private val commentList = mutableListOf<Comment>()
    private lateinit var commentAdapter: CommentAdapter

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val userName = intent.getStringExtra("userName")
        val id= intent.getStringExtra("id")

        auth = FirebaseAuth.getInstance()
        //Toast.makeText(this, "Apreto el Botón de Comm $userName", Toast.LENGTH_SHORT).show()

        replyToUser = findViewById(R.id.userReply)
        replyToUser.text = "$userName"

        ide = id.toString()

        //Toast.makeText(this, "id $ide", Toast.LENGTH_SHORT).show()

        comment = findViewById(R.id.send_comment_textEdit)

        commentBtn = findViewById(R.id.send_comment)

        commentBtn.setOnClickListener {

            description = comment.text.toString()


            //Instanciar Firebase y crear la colección
            val db = FirebaseFirestore.getInstance()
            val collectionRef = db.collection("comments")

            // Crear un nuevo documento
            val data = hashMapOf(
                "id" to ide,
                "nickname" to nickname,
                "comment" to description,
                "timestamp" to FieldValue.serverTimestamp()

            )

            collectionRef.add(data)
                .addOnSuccessListener { documentReference ->
                    // El documento fue creado con éxito
                    Log.d(ContentValues.TAG, "Documento creado con ID: ${documentReference.id}")
                    //Toast.makeText(this, "Documento creado con ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    // Error al crear el documento
                    Log.w(ContentValues.TAG, "Error al crear el documento", e)
                    Log.w(ContentValues.TAG, "Error al guardar la imagen en Firestore", e)
                    //Toast.makeText(this, "Error al crear el documento", Toast.LENGTH_SHORT).show()
                }

            commentClean()

        }

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.refresh_comment)

        swipeRefreshLayout.setOnRefreshListener {
            recycler()
            swipeRefreshLayout.isRefreshing = false
        }

        recycler()
        updateUI()
        back()
        setDayNight()

    }
    private fun back(){
        backImage = findViewById(R.id.back_to_home)
        backImage.setOnClickListener {
            val intent = Intent(this, PrincipalPublicationsActivity::class.java)
            startActivity(intent)
        }
    }

    //Método para obtener el nombre de usuario
    private fun updateUI() {

        val db = Firebase.firestore
        val usersCollectionRef = db.collection("users")
        val user = auth.currentUser
        val userId = user?.email.toString()
        usersCollectionRef.document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                nickname = document.getString("nickname").toString()

            }
        }
    }

    private fun commentClean(){
        // Para limpiar el texto del EditText
        comment.setText("")
    }

    fun setDayNight() {
        val sp = getSharedPreferences("SP_", MODE_PRIVATE)
        val theme = sp.getInt("Theme", 1)
        if (theme == 0) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    private fun recycler(){
        commentList.clear()
        //[Mostrar la tabla usuarios]
        val db = Firebase.firestore

        val recyclerView = findViewById<RecyclerView>(R.id.recycleView_comment)
        val id= intent.getStringExtra("id")
        val idata = id.toString()
        val collectionRef = db.collection("comments").whereEqualTo("id", idata)

        commentAdapter = CommentAdapter(commentList)
        recyclerView.adapter = commentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter.filtrarComentariosPorIdCard(idata)

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val nombre = document.getString("nickname")
                    val comment = document.getString("comment")

                    val comments= Comment(nickname = nombre ?: "", comment = comment ?: "")


                    commentList.add(comments)
                    ordenar(commentList)

                }


                commentAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error al obtener los usuarios: ", exception)
            }
        //[Mostrar la tabla usuarios]
    }

    private fun ordenar(lista: MutableList<Comment>) {
        lista.sortByDescending{ it.timestamp }
    }


}