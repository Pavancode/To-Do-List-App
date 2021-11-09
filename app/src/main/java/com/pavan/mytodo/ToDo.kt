package com.pavan.mytodo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName =ToDoDatabase.TABLE_NAME )
class ToDo:Serializable{
    @PrimaryKey(autoGenerate = true)
    var ToDoID:Long? = null
    var name:String?= null


}