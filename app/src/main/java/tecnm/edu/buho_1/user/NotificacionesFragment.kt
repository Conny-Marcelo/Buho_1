package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tecnm.edu.buho_1.CommentAdapter
import tecnm.edu.buho_1.R

class NotificacionesFragment : Fragment(), NotifyAdapter.OnCardItemClickListener {

    private lateinit var navController: NavController
    private val notifyList = mutableListOf<Notify>()
    private lateinit var notifyAdapter: NotifyAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var recycler: RecyclerView

    var nickname: String = ""

    var idPost: String? = ""
    private lateinit var idPosts: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_notificaciones, container, false)

        auth = FirebaseAuth.getInstance()

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        if (networkInfo == null || !networkInfo.isConnected) {
            navController.navigate(R.id.notifyFragment)

        }

        idPosts = requireActivity().getSharedPreferences("idData",0)

        recycler = view.findViewById(R.id.recycleView_notify)



        recycler()

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refresh_notify)

        swipeRefreshLayout.setOnRefreshListener {
            recycler()
            swipeRefreshLayout.isRefreshing = false
        }



    return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalScope.launch(Dispatchers.Default) {
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


    }


    private fun recycler() {

        notifyList.clear()
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        notifyAdapter = NotifyAdapter(notifyList)

        recycler.adapter = notifyAdapter

        notifyAdapter.setOnCardItemClickListener(this)
        recycler.layoutManager = LinearLayoutManager(context)

        val db = Firebase.firestore

        val collectionRef = db.collection("notify").whereNotEqualTo("nickname", nickname)


        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val idPost = document.getString("idpost")
                    val nombre = document.getString("nickname")
                    val category = document.getString("category")
                    val date = document.getDate("timestamp")

                    val notify= Notify(idpost = idPost ?:"",nickname = nombre ?: "", category = category ?: "", timestamp = date)

                    notifyList.add(notify)
                    ordenar(notifyList)

                }


               notifyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error al obtener los usuarios: ", exception)
            }
    }

    private fun ordenar(lista: MutableList<Notify>) {
        lista.sortByDescending { it.timestamp }

    }



    override fun onCardItemClick(data: String) {

        if(data == ""){

            navController.navigate(R.id.adviceFragment)

        }else{
            navController.navigate(R.id.viewFragment)

            val editor = idPosts.edit()
            editor.putString("idPost", data)
            editor.apply()
        }

       // Toast.makeText(context, "idddddd :  + $data", Toast.LENGTH_SHORT).show()
    }

}