package com.erkankaplan.getlanipadressen.room._03_roomdatabase


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erkankaplan.getlanipadressen.room._01_entity.HappyhoursFiyatModel
import com.erkankaplan.getlanipadressen.room._01_entity.HappyhoursModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.IngredientPriceModel
import com.erkankaplan.getlanipadressen.room._01_entity.MenuDetailModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductGroupModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientLimitModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductIngredientModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductModel
import com.erkankaplan.getlanipadressen.room._01_entity.ProductPriceModel

import com.erkankaplan.getlanipadressen.room._01_entity.UserModel
import com.erkankaplan.getlanipadressen.room._02_dao.IngredientDao
import com.erkankaplan.getlanipadressen.room._02_dao.ProductDao

import com.erkankaplan.getlanipadressen.room._02_dao.UserModelDao

@Database(entities = [
    ProductModel::class,
    ProductGroupModel::class,
    ProductPriceModel::class,
    ProductIngredientModel::class,
    ProductIngredientLimitModel::class,

    UserModel::class,

    IngredientGroupModel::class,
    IngredientModel::class,
    IngredientPriceModel::class,

    HappyhoursModel::class,
    HappyhoursFiyatModel::class,

    MenuDetailModel::class,
                     ],
          version = 4,
          exportSchema = false) abstract class KioskAppDatabase : RoomDatabase() {

    // Butun DAO lar burda toplaniyor
    abstract fun productDao() : ProductDao
    abstract fun userModelDao() : UserModelDao
    abstract fun ingredientDao() : IngredientDao

    companion object {
        @Volatile private var INSTANCE : KioskAppDatabase? = null

        fun getDatabase(context : Context) : KioskAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                                                    KioskAppDatabase::class.java,
                                                    "user_database").fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}