package tecnm.edu.buho_1.admin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.user.Notify
import tecnm.edu.buho_1.user.NotifyAdapter


class NotificationsAdmin : Fragment(), AdminAdapter.OnItemClickListener {

    private lateinit var navController: NavController

    private lateinit var recycler: RecyclerView

    private val notifyList = mutableListOf<NotifyAdmin>()
    private lateinit var notifyAdapter: AdminAdapter

    var idP: String? = ""
    private lateinit var idPosts: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notificaciones_admin, container, false)

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragmentAdmin) as NavHostFragment
        navController = navHostFragment.navController

        if (networkInfo == null || !networkInfo.isConnected) {
            navController.navigate(R.id.notifyFragmentAdmin)
        }

        idPosts = requireActivity().getSharedPreferences("idDate",Context.MODE_PRIVATE)

        recycler = view.findViewById(R.id.recycleView_notify_admin)

        recycler()

        val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.refresh_notify_admin)

        swipeRefreshLayout.setOnRefreshListener {
            recycler()
            swipeRefreshLayout.isRefreshing = false
        }


        return view

    }

    private fun recycler() {
        notifyList.clear()
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        notifyAdapter = AdminAdapter(notifyList)

        recycler.adapter = notifyAdapter

        notifyAdapter.setOnItemClickListener(this)
        recycler.layoutManager = LinearLayoutManager(context)

        val db = Firebase.firestore

        val collectionRef = db.collection("notify_report")


        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val idP = document.getString("idpost")
                    val nombre = document.getString("nickname")
                    val nReport= document.getString("userReport")
                    val category = document.getString("category")
                    val date = document.getDate("timestamp")

                    val notify= NotifyAdmin(idPost = idP ?: "" ,nickname = nombre ?: "", nameReport =  nReport ?: "", category = category ?: "", timestamp = date)


                    notifyList.add(notify)
                    ordenar(notifyList)

                }


                notifyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(ContentValues.TAG, "Error al obtener los usuarios: ", exception)
            }
    }

    private fun ordenar(lista: MutableList<NotifyAdmin>) {
        lista.sortByDescending { it.timestamp }

    }

    override fun onItemClick(data: String) {
        try {

            navController.navigate(R.id.deleteAdmin)


            val editor = idPosts.edit()
            editor?.putString("idP", data)
            editor?.apply()
            //Toast.makeText(context, "idddddd :  + $data", Toast.LENGTH_SHORT).show()

        }catch (e: Exception) {
            // Manejo de cualquier otra excepción
            Log.e(TAG,"Error desconocido: ${e.message}")
        }

    }




}