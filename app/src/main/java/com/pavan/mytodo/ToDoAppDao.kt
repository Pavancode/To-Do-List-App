package com.pavan.mytodo

import androidx.room.*


@Dao
interface ToDoAppDao {
    @Insert
    fun insetToDo(toDo: ToDo):Long

    @Query("SELECT *FROM " + ToDoDatabase.TABLE_NAME)
    fun fetchList():MutableList<ToDo>

    @Update
    fun updateToDo(toDo: ToDo)

    @Delete
    fun deleteToDo(toDo: ToDo)





}