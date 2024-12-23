package com.erkankaplan.getlanipadressen.room._02_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.erkankaplan.getlanipadressen.room._01_entity.UserModel

@Dao
interface UserModelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: UserModel) : Long?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUsers(users: List<UserModel>) : List<Long>?



    @Query("SELECT * FROM user_model")
    fun getAllUsers(): LiveData<List<UserModel>>

    @Delete
    suspend fun delete(user: UserModel): Int?

    @Update
    suspend fun update(user: UserModel): Int?


    @Query("SELECT * FROM user_model WHERE id = :userId")
    fun getUserById(userId : Int) : UserModel?





    @Query("DELETE FROM user_model WHERE id = :userId")
    suspend fun deleteUserById(userId : Int) : Int?

    @Query("DELETE FROM user_model")
    suspend fun deleteAllUsers(): Int?

}