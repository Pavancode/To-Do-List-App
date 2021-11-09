package com.pavan.mytodo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ToDoAdapter(var context:Context, var  toDoList: MutableList<ToDo>,var toDoDatabase: ToDoDatabase):RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {


    class ToDoViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var nameTextView:TextView =itemView.findViewById(R.id.titleoftask)
        var editButton: ImageView =itemView.findViewById(R.id.editButton)
        var deleteButton:ImageView =itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ToDoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.to_do_item,parent,false)
        return ToDoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = toDoList[position]
        holder.nameTextView.text = todo.name

        holder.editButton.setOnClickListener{
            val intent = Intent(context, CreateTaskActivity::class.java)
            intent.putExtra("Previous",todo)
            context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete item")
                .setMessage("Are you want to delete this? :) :)")
                .setPositiveButton("Yes") { dialogInterface,i ->
                    deleteItem(todo,position)
                }
                .setNegativeButton("No"){dialogInterface,i ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
        }


    }

    override fun getItemCount(): Int {
       return toDoList.size
    }
    fun setList(List:MutableList<ToDo>){
        toDoList=List
        notifyDataSetChanged()
    }
    private fun deleteItem(toDo: ToDo,position: Int){
        doAsync {
            toDoDatabase.toDoAppDao().deleteToDo(toDo)


            uiThread {
                toDoList.remove(toDo)
                notifyItemRemoved(position)

            }



        }
    }
}