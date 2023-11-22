package com.example.todoapp.ui.main.bottomsheet

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.example.todoapp.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor():ViewModel() {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(Constant.DATASTORE_NAME)

}