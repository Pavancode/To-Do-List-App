package com.pavan.mytodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    lateinit var toDoDatabse:ToDoDatabase
    lateinit var recyclerView:RecyclerView
    lateinit var toDoAdapter:ToDoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var list: MutableList<ToDo> = mutableListOf()
        toDoDatabse = Room.databaseBuilder(applicationContext,ToDoDatabase::class.java,ToDoDatabase.DB_NAME).build()
        addtodo.setOnClickListener {
            val intent = Intent(this,CreateTaskActivity::class.java)
            startActivity(intent)
        }
        recyclerView= findViewById(R.id.recycle)
        toDoAdapter = ToDoAdapter(this,list,toDoDatabse)
        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchToDoList()

    }
    private fun fetchToDoList(){
        doAsync {
            val list = toDoDatabse.toDoAppDao().fetchList()
            uiThread {
                (toDoAdapter).setList(list)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val searchItem : MenuItem = menu?.findItem(R.id.action_search)!!
        val  searchView:SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
              return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchTodos(it)
                }
                return true
            }

        })
        return true

    }
    private fun searchTodos(newText:String){
        doAsync {
            val list = toDoDatabse.toDoAppDao().fetchList()

            uiThread {
                val  filteredList = filter(list,newText)
                filteredList?.let {listafterfiltering ->
                    toDoAdapter.setList(listafterfiltering)
                    recyclerView.scrollToPosition(0)
                }

            }
        }
    }
    private fun filter(list: List<ToDo>,newText: String):MutableList<ToDo>?{
        val lowerCaseText = newText.lowercase()
        val filteredList:MutableList<ToDo> = mutableListOf()
        for (item in list){
            val  text :String = item.name?.toLowerCase()!!
            if (text.contains(lowerCaseText)){
                filteredList.add(item)
            }

        }
        return filteredList

    }
}