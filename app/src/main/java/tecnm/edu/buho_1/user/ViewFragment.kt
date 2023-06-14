package tecnm.edu.buho_1.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tecnm.edu.buho_1.R
import java.util.Date


class ViewFragment : Fragment() {

    private lateinit var retur : ImageButton
    private lateinit var idPosts: SharedPreferences



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_view, container, false)

        idPosts = requireActivity().getSharedPreferences("idData",0)

        val post = idPosts.getString("idPost","")

        val mNicknameTextView: TextView = view.findViewById(R.id.userName_notify) as TextView
        val mCategoryTextView: TextView = view.findViewById(R.id.chooseTextView_notify) as TextView
        val mLocationTextView: TextView = view.findViewById(R.id.localizationTextView_notify) as TextView
        val mTimestampTextView: TextView = view.findViewById(R.id.dateTextView_notify) as TextView
        val mDescriptionTextView: TextView = view.findViewById(R.id.editTextDescription_notify) as TextView
        val mPosterImageView: ImageView = view.findViewById(R.id.imageView_notify) as ImageView


        val db = Firebase.firestore

        val documentId = "$post"
        val documentRef = db.collection("posts").document(documentId)

        //Toast.makeText(requireContext(), "Documento $documentId", Toast.LENGTH_SHORT).show()

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

        retur = view.findViewById(R.id.back_to_notify)

        retur.setOnClickListener {

            onBackPressed()




        }



        return view
    }

     private fun onBackPressed() {
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("notificacionesFragment")
        if (myFragment != null && myFragment.isVisible) {
            // Estás en el Fragmento que deseas regresar
            reiniciarSharedPreferences(requireContext())
        } else {
            super.requireActivity().onBackPressed()
        }
    }


    private fun reiniciarSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("idData", 0)
        val editor = sharedPreferences.edit()
        editor.clear() // Borra todos los datos almacenados en SharedPreferences
        editor.apply()

    }



}