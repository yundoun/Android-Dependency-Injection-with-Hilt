package com.example.memo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// 프로젝트 레벨의 Hilt 설정
@HiltAndroidApp
class MemoApplication: Application()