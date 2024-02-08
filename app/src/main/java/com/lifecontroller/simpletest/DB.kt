package com.lifecontroller.simpletest

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Question::class],
    version = 1,
//    exportSchema = true
)
abstract class DB: RoomDatabase() {
    abstract val dao: QuestionDao

    companion object{
        const val DATABASE_NAME = "testApp.db"

        @Volatile
        private var INSTANCE: DB? = null
        fun getInstance(context: android.content.Context): DB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DB::class.java,
                        DATABASE_NAME
                    )
                        .createFromAsset("test.db")
                        .allowMainThreadQueries()
                        .build()
                }
                return instance
            }
        }
    }
}

@Entity
data class Question(
    @PrimaryKey val id: Long? = null,
    val query: String,
    val answer: String
)

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question")
    fun getQuestions(): List<Question>
    @Query("SELECT * FROM question WHERE id = :id")
    fun getQuestionById(id: Long): Question?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestion(question: Question)
    @Delete
    fun deleteQuestion(question: Question)
}