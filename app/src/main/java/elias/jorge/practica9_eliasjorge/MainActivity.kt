package elias.jorge.practica9_eliasjorge

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private val userRef= FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btnSave: Button = findViewById(R.id.buttonSave)
        btnSave.setOnClickListener{saveMarkFromForm()}

        userRef.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousName: String?) {}
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                val name = dataSnapshot.child("firstName").getValue(String::class.java)
                val lastname = dataSnapshot.child("lastName").getValue(String::class.java)
                val age = dataSnapshot.child("age").getValue(String::class.java)

                if (name != null && lastname != null && age != null) {
                    val usuario = User(name, lastname, age)
                    Log.d("MainActivity", "Usuario agregado: ${usuario.toString()}")
                    writeMark(usuario)
                }else{
                    Log.d("MainActivity", "No se pudieron obtener los datos del usuario")
                }
            }
        })
    }

    private fun saveMarkFromForm(){
        var name: EditText =findViewById(R.id.editTextName) as EditText
        var lastname: EditText =findViewById(R.id.editTextLastName) as EditText
        var age: EditText =findViewById(R.id.editTextAge) as EditText

        val usuario = User(name.text.toString(),lastname.text.toString(),age.text.toString())
        userRef.push().setValue(usuario)
    }

    private fun writeMark(mark: User) {
        val listV: TextView = findViewById(R.id.textViewList) as TextView
        val text = listV.text.toString() + mark.toString() + "\n"
        listV.text = text
    }

}