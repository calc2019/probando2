package com.example.android.elefantitoverde

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.sql.SQLException


class CustomSQL (var contexto:Context, var name:String, var factory: SQLiteDatabase.CursorFactory?, var version:Int):
    SQLiteOpenHelper (contexto,name,factory,version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "CREATE TABLE lista (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, cantidad INTERGER, precio INTERGER, precioIva INTEGER, precioDolar DOUBLE, categoria TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun insertar(

        nombre: String,
        cantidad: Int,
        precio: Int,
        precioIva: Int,
        precioDolar: Double,
        categoria: String
    ) {
        try {
            var db = this.writableDatabase
            var cv = ContentValues()

            cv.put("nombre", nombre)
            cv.put("cantidad", cantidad)
            cv.put("precio", precio)
            cv.put("precioIva", precioIva)
            cv.put("precioDolar", precioDolar)
            cv.put("categoria", categoria)// hay qye agregarlo a la BD
            val resultado = db.insert("lista", null, cv)
            db.close()
            if (resultado == -1L) {
                Toast.makeText(contexto, "Mensaje no agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(contexto, "Mensaje agregado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SQLException) {
            Toast.makeText(contexto, "Error al Insertar ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("sqlListar", e.message)
        }
    }


fun listar(): ArrayList<Producto>
{
    var lista = ArrayList<Producto>()
    try
    {
        val db = this.writableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery("SELECT * FROM lista",null)
        if(cursor.moveToFirst()== true)
        {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val cantidad = cursor.getInt(2)
                val precio = cursor.getInt(3)
                val precioIva = cursor.getInt(4)
                val precioDolar = cursor.getDouble(5)
                val categoria = cursor.getString(6)
                val reg = Producto(id,nombre,cantidad, precio, precioIva, precioDolar, categoria)
                lista.add(reg)

            }while (cursor.moveToNext())
        }
        db.close()
    }
    catch (e: android.database.SQLException)
    {
        Toast.makeText(contexto,"${e.message}",Toast.LENGTH_SHORT).show()
    }
    return  lista
}


fun eliminar(id:Int)
{
    try{
        val db = this.writableDatabase
        val args = arrayOf(id.toString())
        val resultado = db.delete("lista","id = ?",args)
        db.close()
        if (resultado==0)
        {
            Toast.makeText(contexto,"no se pudo eliminar",Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(contexto,"Eliminado",Toast.LENGTH_SHORT).show()
        }
    }
    catch (e: android.database.SQLException)
    {
        Toast.makeText(contexto,"${e.message}",Toast.LENGTH_SHORT).show()
    }
}
}
