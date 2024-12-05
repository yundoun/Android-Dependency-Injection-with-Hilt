package com.example.memo.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.memo.data.local.MemoDatabase
import com.example.memo.data.repository.MemoRepositoryImpl
import com.example.memo.domain.repository.MemoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMemoDatabase(app: Application): MemoDatabase {  // 리턴 타입을 RoomDatabase에서 MemoDatabase로 수정
        return Room.databaseBuilder(
            app,
            MemoDatabase::class.java,
            "memo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMemoRepository(db: MemoDatabase): MemoRepository {
        return MemoRepositoryImpl(db.memoDao())
    }
}