package com.example.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EtuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = EtudiantDBHelper(applicationContext)
        val db = dbHelper.readableDatabase
        val etudiantsList = getAllEtudiants(db)

        recyclerView = findViewById(R.id.recyclerViewEtudiants)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = EtuAdapter(etudiantsList)
        recyclerView.adapter = adapter

        val btnAjouter: Button = findViewById(R.id.btnAjouter)
        btnAjouter.setOnClickListener {
            val intent = Intent(this, Inscription::class.java)
            startActivity(intent)
        }
    }

    private fun getAllEtudiants(db: SQLiteDatabase): List<EtudiantBC> {
        val etudiantsList = mutableListOf<EtudiantBC>()

        val projection = arrayOf(
            EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL,
            EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP
        )

        val cursor = db.query(
            EtudiantBC.EtudiantEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val nom = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM))
                val prenom = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM))
                val phone = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE))
                val email = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL))
                val mdp = getString(getColumnIndexOrThrow(EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP))
                val etudiant = EtudiantBC(nom, prenom, phone, email, mdp)
                etudiantsList.add(etudiant)
            }
        }

        cursor.close()
        return etudiantsList
    }
}