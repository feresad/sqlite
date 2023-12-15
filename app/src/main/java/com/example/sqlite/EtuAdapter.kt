package com.example.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class EtuAdapter(private val etudiants: List<EtudiantBC>) : RecyclerView.Adapter<EtuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_etudiant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val etudiant = etudiants[position]
        holder.bind(etudiant)
    }

    override fun getItemCount(): Int {
        return etudiants.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNom: TextView = itemView.findViewById(R.id.textViewNom)
        private val textViewPrenom: TextView = itemView.findViewById(R.id.textViewPrenom)
        private val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
        private val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(etudiant: EtudiantBC) {
            textViewNom.text = "Nom : ${etudiant.nom} "
            textViewPrenom.text = "Prenom : ${etudiant.prenom} "
            textViewPhone.text = "Phone : ${etudiant.phone}"
            textViewEmail.text = "Email : ${etudiant.email} "


            btnDelete.setOnClickListener {
                showDeleteConfirmationDialog(etudiant)
            }
        }

        private fun showDeleteConfirmationDialog(etudiant: EtudiantBC) {
            val builder = AlertDialog.Builder(itemView.context)

            builder.setTitle("Confirmer la suppression")
            builder.setMessage("Êtes-vous sûr de supprimer cette Etudiant?")
            builder.setPositiveButton("Oui") { _, _ ->
                deleteEtudiantFromDatabase(etudiant)
            }
            builder.setNegativeButton("Non") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }

        private fun deleteEtudiantFromDatabase(etudiant: EtudiantBC) {
            val dbHelper = EtudiantDBHelper(itemView.context)
            val db = dbHelper.writableDatabase

            val selection = "${EtudiantBC.EtudiantEntry.COLUMN_NAME_NOM} = ? AND " +
                    "${EtudiantBC.EtudiantEntry.COLUMN_NAME_PRENOM} = ? AND " +
                    "${EtudiantBC.EtudiantEntry.COLUMN_NAME_PHONE} = ? AND " +
                    "${EtudiantBC.EtudiantEntry.COLUMN_NAME_EMAIL} = ? AND " +
                    "${EtudiantBC.EtudiantEntry.COLUMN_NAME_MDP} = ?"

            val selectionArgs = arrayOf(
                etudiant.nom,
                etudiant.prenom,
                etudiant.phone,
                etudiant.email,
                etudiant.mdp
            )

            val deletedRows = db.delete(
                EtudiantBC.EtudiantEntry.TABLE_NAME,
                selection,
                selectionArgs
            )

            db.close()
            dbHelper.close()

            if (deletedRows > 0) {
                etudiants.toMutableList().remove(etudiant)
                notifyItemRemoved(adapterPosition)
            } else {
                Toast.makeText(itemView.context, "Échec de la suppression de l'entrée", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}