package com.example.android.elefantitoverde


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.android.elefantitoverde.R.id.lvListar
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_agregar.*
import okhttp3.*
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AgregarFragment : Fragment() {

    var miContexto: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_agregar, container, false)

        var categorias:ArrayList<String> = ArrayList()
        categorias.add("Bebestibles")
        categorias.add("Abarrotes")
        categorias.add("Lacteos")
        categorias.add("Carnes")
        categorias.add("Electrónica")
        categorias.add("Casa y Jardin")


        //esto es nuevo

       var adaptador = ArrayAdapter<String>(miContexto, android.R.layout.simple_list_item_1,categorias)
       spnCategoria.adapter =adaptador

        spnCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var categoria_Prod = spnCategoria.selectedItem.toString()
                Toast.makeText(miContexto, categoria_Prod,Toast.LENGTH_SHORT).show()
            }




        }
       /* var spnCategoria = view.findViewById<Spinner>(R.id.spnCategoria)
        spnCategoria.adapter = ArrayAdapter<String>(miContexto,android.R.layout.simple_list_item_1,categorias)*/


        var txtNombre = view.findViewById<TextView>(R.id.txtNombre)
        var txtCantidad = view.findViewById<TextView>(R.id.txtCantidad)
        var txtPrecio = view.findViewById<TextView>(R.id.txtPrecio)
        var categoria_Prod = view.findViewById<Spinner>(R.id.spnCategoria)
        val myDB  = CustomSQL(miContexto!!,"miDB",null,1)
        var btn= view.findViewById<Button>(R.id.btnAgregar)
        btn.setOnClickListener {
            if(txtNombre.text.toString()=="" || txtCantidad.text.toString()=="" ||
                txtPrecio.text.toString()=="" )
            {
                Toast.makeText(miContexto, "Debe completar todos los campos", Toast.LENGTH_SHORT).show()
            }
            else{
                var alertDialog = AlertDialog.Builder(miContexto)
                    alertDialog.setTitle("Agregar Producto")
                    alertDialog.setMessage("Esta seguro que desea agregar el producto?")
                    alertDialog.setPositiveButton("Si",
                        DialogInterface.OnClickListener { dialog, which ->

                        val url ="https://www.mindicador.cl/api"
                        val request = Request.Builder().url(url).build()
                        val cliente = OkHttpClient()
                        cliente.newCall(request).enqueue(object : Callback {

                            override fun onResponse(call: Call?, response: Response?) {
                                val respuesta = response?.body()?.string()
                                val gson = GsonBuilder().create()
                                val indicador = gson.fromJson(respuesta,Indicador::class.java)


                                var a = indicador.serie[0].valor

                                this@AgregarFragment.activity?.runOnUiThread {
                                    //calcular valores iva y dolar

                                    var precioiva =txtPrecio.text.toString().toInt()*1.19
                                    var precioDolar= precioiva / a

                                  /*  spnCategoria.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                                      override fun onNothingSelected(parent: AdapterView<*>?) {

                                        }

                                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                            var categoriaProd = spnCategoria.selectedItem.toString()
                                        }
                                    }*/

                                   myDB.insertar(txtNombre.text.toString(),
                                           txtCantidad.text.toString().toInt(),
                                           txtPrecio.text.toString().toInt(),
                                           precioiva.toInt(),
                                           precioDolar,
                                           cate)

                                    Toast.makeText(miContexto,"Cliente Agregado",Toast.LENGTH_SHORT).show()
                                }

                            }

                            override fun onFailure (call: Call?, e: IOException){

                            }

                        })





                    })
                alertDialog.setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    })
                alertDialog.show()

            }
        }

        return view
    }

}
