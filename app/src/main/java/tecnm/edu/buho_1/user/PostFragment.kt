package tecnm.edu.buho_1.user

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import tecnm.edu.buho_1.R
import java.io.ByteArrayOutputStream
import java.util.UUID


class PostFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nicknameUserTextView: TextView
    private lateinit var getlocalizationTextView: TextView
    private lateinit var descriptionEditText: EditText

    private lateinit var galleryBtn: Button
    private lateinit var localizationBtn: Button
    private lateinit var cameraBtn: Button
    private lateinit var postBtn: Button

    private lateinit var image: ImageView

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var datos: SharedPreferences

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    var nickname: String = ""
    var item: String = ""
    var description: String = ""
    lateinit var imageBitmap: Bitmap
    var selectedImageUri: Uri? = null
    var imageUrl: String = ""

    val docuRef = String



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val paychartview = inflater.inflate(R.layout.fragment_post, container, false)

        datos = requireActivity().getSharedPreferences("datos",0)

        //conexión a internet
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Alerta")
            builder.setMessage("No tienes conexión a internet, intenta otra vez. ")
            builder.setPositiveButton("OK") { dialog, which ->
                // Acción cuando se presiona el botón "OK"
            }
            val dialog = builder.create()
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.show()
        }

        val address = datos.getString("address","")

        //Toast.makeText(context, "address es : " + address, Toast.LENGTH_SHORT).show()

        getlocalizationTextView = paychartview.findViewById(R.id.localizationTextView)
        getlocalizationTextView.text = "$address"

        // imagenes
        image = paychartview.findViewById(R.id.imageView)

        galleryBtn = paychartview.findViewById<Button>(R.id.galleryButton) as Button
        galleryBtn.setOnClickListener {
            openGallery()
        }

        localizationBtn = paychartview.findViewById(R.id.localizationButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        localizationBtn.setOnClickListener {
            // Verificar si el permiso ya fue concedido
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permiso ya concedido, realizar la acción deseada
                doSomething()
            } else {
                // Permiso no concedido, solicitarlo
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }

        //cámara
        cameraBtn = paychartview.findViewById(R.id.cameraButton)
        cameraBtn.setOnClickListener {
            openCamera()
        }

        nicknameUserTextView = paychartview.findViewById(R.id.userName)
        auth = FirebaseAuth.getInstance()

        // spinner y nombre de usuario
        val spinner = paychartview.findViewById<Spinner>(R.id.spinner_categories)

        spinner?.adapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.categories,
                R.layout.spinner_item
            )
        } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //..
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                item = parent?.getItemAtPosition(position).toString()
                //..
            }
        }
        //Descripción del post
        descriptionEditText = paychartview.findViewById(R.id.editTextDescription)

        //Botón para publicar
        postBtn = paychartview.findViewById(R.id.postButton)
        postBtn.setOnClickListener {
            //Pasar el texto a una variable
            description = descriptionEditText.text.toString()

            //notifications()

                //Guardar imagen
                // Obtener la referencia de Firebase Storage
                val storageRef = Firebase.storage.reference

                // Crear una referencia única para la imagen
                val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")
                val baos = ByteArrayOutputStream()
                image.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val datas = baos.toByteArray()

                val uploadTask = imageRef.putBytes(datas)
                uploadTask.addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                        // Guarda la URL de descarga en Firestore



                        //Instanciar Firebase y crear la colección
                        val db = FirebaseFirestore.getInstance()
                        val collectionRef = db.collection("posts")


                        // Crear un nuevo documento
                        val data = hashMapOf(
                            "nickname" to nickname,
                            "category" to item,
                            "location" to address,
                            "description" to description,
                            "timestamp" to FieldValue.serverTimestamp(),
                            "url" to imageUrl

                        )

                        collectionRef.add(data)
                            .addOnSuccessListener { documentReference ->
                                // El documento fue creado con éxito
                                Log.d(ContentValues.TAG, "Documento creado con ID: ${documentReference.id}")

                                notifications(documentReference.id.toString())

                               // Toast.makeText( context, "Documento creado con ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()

                            }
                            .addOnFailureListener { e ->
                                // Error al crear el documento
                                Log.w(ContentValues.TAG, "Error al crear el documento", e)
                                // Toast.makeText(context, "Error al crear el documento", Toast.LENGTH_SHORT).show()
                            }


                    }



                }



            clean()

        }

        updateUI()
        return paychartview
    }


    private fun notifications(documentReference : String){

        //Instanciar Firebase y crear la colección
        val db = FirebaseFirestore.getInstance()
        val collectionRef1 = db.collection("notify")

        // Crear un nuevo documento
        val data1 = hashMapOf(
            "idpost" to documentReference,
            "nickname" to nickname,
            "category" to item,
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


    private fun clean(){
        //item, address, descripción e imagen

        getlocalizationTextView.setText("")
        descriptionEditText.setText("")
        image.setImageResource(android.R.color.transparent)

    }

    //Propiedades que puedan ser accesibles sin necesidad de crear una instancia de la clase
    companion object {

        const val REQUEST_CODE_GALLERY = 100

        const val REQUEST_IMAGE_CAPTURE = 1

        private val REQUEST_CAMERA_PERMISSION = 123

        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

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
                nicknameUserTextView.text = "$nickname"


            }
        }
    }

    //Método para abrir la galeria
    private fun openGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)

    }

    //Método para acceder a la cámara
    private fun openCamera() {

        // Verifica si se tiene el permiso de la cámara
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            // Si no se tiene el permiso, se solicita
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }

    }

    //Obtnener la foto de la camara o la elegida en la galeria
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri.let { uri ->
                // Carga la imagen seleccionada en un ImageView utilizando Glide
                Glide.with(requireContext())
                    .load(uri)
                    .into(image)

                // Guarda la URI de la imagen para subirla a Firestore más tarde
                selectedImageUri.let { selectedImageUri = it }

            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            // Realiza las operaciones necesarias con la imagen capturada
            imageBitmap.let { uri ->
                // Carga la imagen seleccionada en un ImageView utilizando Glide
                Glide.with(requireContext())
                    .load(uri)
                    .into(image)

                // Guarda la URI de la imagen para subirla a Firestore más tarde
                imageBitmap.let { imageBitmap = it }

            }
        }

    }

    // Agrega el método onRequestPermissionsResult para manejar la respuesta del usuario
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                // Si se ha concedido el permiso, procede a utilizar la cámara
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Se tiene el permiso
                } else {
                    // No se ha concedido el permiso, muestra un mensaje de error
                    Toast.makeText(
                        requireContext(),
                        "Permiso denegado para la cámara",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, realizar la acción deseada
                doSomething()
            } else {
                // Permiso no concedido, mostrar mensaje al usuario
                Toast.makeText(
                    requireContext(),
                    "Permiso de ubicación denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Ir al mapa a obtener la ubicación
    private fun doSomething() {
        val intent = Intent(activity, MapsActivity::class.java)
        startActivity(intent)

    }





}