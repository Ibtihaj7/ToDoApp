package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.model.MyRoomDatabase
import com.example.todoapp.model.TaskDAO
import com.example.todoapp.repo.database.DatabaseRepository
import com.example.todoapp.repo.database.DatabaseRepositoryImpl
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.repo.tasks.TasksRepositoryImpl
import com.example.todoapp.utils.Constant.TASKS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MyRoomDatabase::class.java,
            TASKS_DATABASE
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    @Provides
    fun provideDao(db: MyRoomDatabase) = db.taskDao()

    @Provides
    fun provideDatabaseRepository(taskDao:TaskDAO): DatabaseRepository {
        return DatabaseRepositoryImpl(taskDao)
    }

    @Provides
    fun provideTasksRepository(dbRepo:DatabaseRepository): TasksRepository = TasksRepositoryImpl(dbRepo)
}

