package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.CommentActivity
import tecnm.edu.buho_1.user.Item
import tecnm.edu.buho_1.R


class HomeFragment : Fragment() {

    private val listPosts:MutableList<Item> = ArrayList()

    private lateinit var recycler: RecyclerView

    private lateinit var navController: NavController

    private lateinit var filterBtn : ImageButton
    private lateinit var moreBtn : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio, container, false)

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        if (networkInfo == null || !networkInfo.isConnected) {
            navController.navigate(R.id.homeFragment)
        }

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.fragment_home)

        swipeRefreshLayout.setOnRefreshListener {
           postRecycler()
            swipeRefreshLayout.isRefreshing = false
        }

        recycler = view.findViewById(R.id.recycleView)
        postRecycler()

        filterBtn = view.findViewById(R.id.fillImage)

        filterBtn.setOnClickListener {
            navController.navigate(R.id.filterFragment)
        }

        moreBtn = view.findViewById(R.id.postFast)

        moreBtn.setOnClickListener {
            navController.navigate(R.id.crearPublicacionFragment)
        }


        return view
    }

    private fun postRecycler(){

        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = LinearLayoutManager(context)
        listPosts.clear()
        val db = Firebase.firestore
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val mPost =
                        Item(document.id,
                            document.getString("nickname"),
                            document.getString("category"),
                            document.getString("location"),
                            document.getDate("timestamp"),
                            document.getString("description"),
                            document.getString("url"))
                    mPost.let {

                        listPosts.add(it)
                    }
                    //Toast.makeText(requireContext(), mPost.toString(), Toast.LENGTH_LONG).show()
                }
                //Toast.makeText(requireContext(), listPosts.size.toString(), Toast.LENGTH_LONG).show()
                ordenarPorFechaDescendente(listPosts)

                val adaptador = PostViewAdapter(listPosts)
                recycler.adapter = adaptador

                //recycler.adapter = PostViewAdapter(listPosts)

            }
            .addOnFailureListener { exception ->

            }
    }


    class PostViewAdapter(private val values: List<Item>) :
        RecyclerView.Adapter<PostViewAdapter.ViewHolder>() {

        private lateinit var buttonReport: Button
        private lateinit var buttonComment: Button

        private lateinit var auth: FirebaseAuth
        var name: String = ""

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_post, parent, false)

            buttonReport = view.findViewById(R.id.buttonReport)
            buttonComment = view.findViewById(R.id.buttonComment)

            return ViewHolder(view)
        }



        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val mPost = values[position]
            holder.mNicknameTextView.text = mPost.nickname
            holder.mCategoryTextView.text = mPost.category
            holder.mLocationTextView.text = mPost.location
            holder.mTimestampTextView.text = mPost.timestamp.toString()
            holder.mDescriptionTextView.text = mPost.description
            holder.mPosterImageView.let {
                Glide.with(holder.itemView.context)
                    .load(mPost.url)
                    .into(it)
            }

            buttonReport.setOnClickListener {

                // Acceder al contexto de la aplicación
                val contexto = holder.itemView.context

                // Ejemplo de Toast
                Toast.makeText(contexto, "Publicación reportada", Toast.LENGTH_SHORT).show()

                //Instanciar Firebase y crear la colección
                val db = FirebaseFirestore.getInstance()
                auth = FirebaseAuth.getInstance()

                val usersCollectionRef = db.collection("users")
                val user = auth.currentUser
                val userId = user?.email.toString()
                usersCollectionRef.document(userId).get().addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("nickname").toString()
                        Toast.makeText(contexto, "$name", Toast.LENGTH_SHORT ).show()
                    }
                }

                val collectionRef1 = db.collection("notify_report")

                    // Crear un nuevo documento
                    val data1 = hashMapOf(
                        "idpost" to mPost.id,
                        "userReport" to name,
                        "nickname" to mPost.nickname,
                        "category" to mPost.location,
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


            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra("userName", mPost.nickname)
                intent.putExtra("id", mPost.id)
                context.startActivity(intent)

            }

              buttonComment.setOnClickListener {
                  val context = holder.itemView.context
                  val intent = Intent(context, CommentActivity::class.java)
                  intent.putExtra("userName", mPost.nickname)
                  intent.putExtra("id", mPost.id)
                  context.startActivity(intent)
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNicknameTextView: TextView = view.findViewById(R.id.item_nickname) as TextView
            val mCategoryTextView: TextView = view.findViewById(R.id.item_category) as TextView
            val mLocationTextView: TextView = view.findViewById(R.id.item_location) as TextView
            val mTimestampTextView: TextView = view.findViewById(R.id.item_date) as TextView
            val mDescriptionTextView: TextView = view.findViewById(R.id.item_description) as TextView
            val mPosterImageView: ImageView = view.findViewById(R.id.item_image) as ImageView
        }

        // Método para ordenar los elementos por fecha más reciente

    }
    private fun ordenarPorFechaDescendente(lista: MutableList<Item>) {
        lista.sortByDescending { it.timestamp }
    }


}


