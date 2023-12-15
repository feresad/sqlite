package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Inscription : AppCompatActivity() {

    private lateinit var Nom: EditText
    private lateinit var Prenom: EditText
    private lateinit var Tel: EditText
    private lateinit var Email: EditText
    private lateinit var Mdp: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inscription)

        Nom = findViewById(R.id.Nom)
        Prenom = findViewById(R.id.Prenom)
        Tel = findViewById(R.id.Tel)
        Email = findViewById(R.id.Email)
        Mdp = findViewById(R.id.Mdp)

        val btnValider: Button = findViewById(R.id.btnValider)
        val btnAnnuler: Button = findViewById(R.id.btnAnnuler)

        fun champsNonVides(): Boolean {
            return Nom.text.isNotBlank() &&
                    Prenom.text.isNotBlank() &&
                    Tel.text.isNotBlank() &&
                    Email.text.isNotBlank() &&
                    Mdp.text.isNotBlank()
        }
        btnValider.setOnClickListener {
            if (champsNonVides()) {
                val dbHelper = EtudiantDBHelper(applicationContext)
                val db = dbHelper.writableDatabase

                val values = ContentValues().apply {
                    put(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM, Nom.text.toString())
                    put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM, Prenom.text.toString())
                    put(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE, Tel.text.toString())
                    put(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL, Email.text.toString())
                    put(EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP, Mdp.text.toString())
                }

                val newRowId = db.insert(EtudiantBC.EtudiantEntry.TABLE_NAME, null, values)

                db.close()
                dbHelper.close()

                if (newRowId != -1L) {
                    afficherMessage("Validation", "Inscription réussie!")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    afficherMessage("Erreur", "Une erreur s'est produite lors de l'inscription.")
                }
            } else {
                afficherMessage("Erreur", "Veuillez remplir tous les champs.")
            }
        }


        btnAnnuler.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun afficherMessage(titre: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titre)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}