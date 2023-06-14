package tecnm.edu.buho_1.admin

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.R
import tecnm.edu.buho_1.user.User


class UsersReportsAdminFragment : Fragment(){

    private val usuariosList = mutableListOf<User>()
    private lateinit var usuariosAdapter: UserAdapter

    private lateinit var navController: NavController

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_usuarios_reportes_admin, container, false)

        //[Verifica la conexión a internet]
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragmentAdmin) as NavHostFragment
        navController = navHostFragment.navController

        if (networkInfo == null || !networkInfo.isConnected) {
            navController.navigate(R.id.userFragmentAdmin)
        }
        //[Verifica la conexión a internet]

        //[Mostrar la tabla usuarios]
        val db = Firebase.firestore
        val collectionRef = db.collection("users")

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleViewTable)

        usuariosAdapter = UserAdapter(usuariosList, object: OnItemClickListener{
            override fun onDeleteClick(position: Int) {

                onDelete(position)
            }
        })
        recyclerView.adapter = usuariosAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val nombre = document.getString("nickname")
                    val email = document.getString("email")

                    val usuario = User(nombre = nombre ?: "", email = email ?: "")

                    ordenar(usuariosList)

                    usuariosList.add(usuario)

                }

                usuariosAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al obtener los usuarios: ", exception)
            }
        //[Mostrar la tabla usuarios]

        return view

    }

    private fun ordenar(lista: MutableList<User>) {
        lista.sortBy{ it.nombre }
    }

        private fun onDelete(position: Int) {
            // Eliminar el elemento del RecyclerView y de la colección de Firestore
            val item = usuariosList[position]
            // Eliminar el elemento del RecyclerView
            usuariosList.removeAt(position)
            usuariosAdapter.notifyItemRemoved(position)
            // Eliminar el elemento de la colección de Firestore
            val firestoreCollection = FirebaseFirestore.getInstance().collection("users")
            firestoreCollection.document(item.email).delete()
                .addOnSuccessListener {
                    // Éxito al eliminar el documento
                }
                .addOnFailureListener { e ->
                    // Error al eliminar el documento
                }

        }
}