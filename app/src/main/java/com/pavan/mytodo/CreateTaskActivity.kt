package com.pavan.mytodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_create_task.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.editText
import org.jetbrains.anko.uiThread

class CreateTaskActivity : AppCompatActivity() {
    lateinit var toDoDatabase: ToDoDatabase
    var isBeingUpdated = false
    var previousTodo:ToDo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        if (intent.hasExtra("Previous")){
            isBeingUpdated = true
            previousTodo = intent.extras?.get("Previous") as ToDo

        }
        val editText:EditText = findViewById(R.id.my_edit_text)
        editText.setText(previousTodo?.name)
        toDoDatabase = Room.databaseBuilder(applicationContext,ToDoDatabase::class.java,ToDoDatabase.DB_NAME).build()
        save_btn.setOnClickListener {
            val enteredText = my_edit_text.text.toString()

            if (isBeingUpdated){
                previousTodo?.name = enteredText
                previousTodo?.let {
                    updateRow(previousTodo as ToDo)
                }
            }
            else{
                val todo = ToDo()
                todo.name= enteredText
                insertRow(todo)
            }


        }



    }
    fun insertRow(todo:ToDo){
        // We will inserting Data on background thread
        doAsync {

            val id =toDoDatabase.toDoAppDao().insetToDo(todo)
            uiThread {
                //ui related work
                todo.ToDoID= id
                val intent = Intent(this@CreateTaskActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

    }
    private  fun updateRow(todo: ToDo){
        doAsync {
            toDoDatabase.toDoAppDao().updateToDo(todo)

            uiThread {
                val intent = Intent(this@CreateTaskActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}