package tecnm.edu.buho_1.admin

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.R
import java.util.Date


class DeleteFragment : Fragment() {

    private lateinit var navController: NavController

    private lateinit var retur : ImageButton

    private lateinit var delete : Button

    private lateinit var idPosts: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_delete, container, false)

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragmentAdmin) as NavHostFragment
        navController = navHostFragment.navController

        if (networkInfo == null || !networkInfo.isConnected) {
            navController.navigate(R.id.postsFragmentAdmin)
        }

        idPosts = requireActivity().getSharedPreferences("idDate",Context.MODE_PRIVATE)

        val post = idPosts.getString("idP","")

        val mNicknameTextView: TextView = view.findViewById(R.id.userName_notify_admin) as TextView
        val mCategoryTextView: TextView = view.findViewById(R.id.chooseTextView_notify_admin) as TextView
        val mLocationTextView: TextView = view.findViewById(R.id.localizationTextView_notify_admin) as TextView
        val mTimestampTextView: TextView = view.findViewById(R.id.dateTextView_notify_admin) as TextView
        val mDescriptionTextView: TextView = view.findViewById(R.id.editTextDescription_notify_admin) as TextView
        val mPosterImageView: ImageView = view.findViewById(R.id.imageView_notify_admin) as ImageView

        val db = Firebase.firestore
        val documentId = "$post"
        val documentRef = db.collection("posts").document(documentId)

        //Toast.makeText(requireContext(), "Documento no encontrado $post", Toast.LENGTH_SHORT).show()

        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // El documento existe, puedes acceder a sus datos
                    val data = documentSnapshot.data

                    val nickname = data?.get("nickname")
                    val category = data?.get("category")
                    val location = data?.get("location")
                    val fecha: Date? = documentSnapshot.getDate("timestamp")
                    val desceiption = data?.get("description")
                    val url = data?.get("url")

                    // Hacer algo con los datos

                    mNicknameTextView.text = nickname as CharSequence?
                    mCategoryTextView.text = category as CharSequence?
                    mLocationTextView.text = location as CharSequence?
                    mTimestampTextView.text = fecha.toString()
                    //Toast.makeText(requireContext(), "$fecha", Toast.LENGTH_SHORT).show()
                    mDescriptionTextView.text = desceiption as CharSequence?
                    mPosterImageView.let {
                        Glide.with(requireActivity())
                            .load(url)
                            .into(it)
                    }

                } else {
                    // El documento no existe
                }
            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al obtener el documento
                Toast.makeText(requireContext(), "Documento no encontrado", Toast.LENGTH_SHORT).show()
            }


        retur = view.findViewById(R.id.back_to_notify_admin)

        retur.setOnClickListener {
            onBackPressed()
        }

        delete = view.findViewById(R.id.delete_post)

        delete.setOnClickListener {
            delete(documentId)
            dle(documentId)
        }

        return view
    }

    private fun onBackPressed() {
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("notificacionesAdmin")
        if (myFragment != null && myFragment.isVisible) {
            // Estás en el Fragmento que deseas regresar
        } else {
            super.requireActivity().onBackPressed()
        }
    }

    private fun delete(document : String){
        val db = Firebase.firestore
        val collectionRef = db.collection("posts")
        val documentRef = collectionRef.document(document)

        documentRef.delete()
            .addOnSuccessListener {
                // El documento se eliminó correctamente
                Toast.makeText(requireContext(), "La publicación ha sido eliminada", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
            .addOnFailureListener { e ->
                // Ocurrió un error al intentar eliminar el documento
                Toast.makeText(requireContext(), "Error al eliminar la publicación", Toast.LENGTH_SHORT).show()
            }

    }

    private fun dle(document: String){
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("notify")
        val query = collectionRef.whereEqualTo(document, "idpost")
        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Borra cada documento encontrado
                    document.reference.delete()
                }
            }
            .addOnFailureListener { exception ->
                // Maneja cualquier error que ocurra
                Log.e("TAG", "Error al obtener los documentos: $exception")
            }



    }


}