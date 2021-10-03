package ru.mobiskif.jetpack

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class Model : ViewModel() {
    lateinit var clpu: Lpu
    val state = MutableLiveData<String>()
    val repository = Repository()
    val wait = repository.wait
    var cuser = repository.cuser
    val palette = repository.palette

    val users = repository.users
    val lpus = repository.lpus
    val distrs = repository.distrs
    val history = repository.history
    val specs = repository.specs
    val docs = repository.docs
    val talons = repository.talons
    val idtalon = repository.idtalon
    val confs = repository.confs

    fun repaint() {
        val old = getState()
        setState("Инструкция")
        setState(old)
    }

    fun setState(state: String) {
        this.state.postValue(state)
    }

    fun getState(): String {
        return "${state.value}"
    }

    fun setPalette(context: Context, theme: String) {
        LightPalette = setLightPalette(context, theme)
        viewModelScope.launch { repository.setPalette(theme) }
    }

    fun setDBContext(context: Context) {
        viewModelScope.launch { repository.setDBContext(context) }
    }

    fun readConfs() {
        viewModelScope.launch { repository.readConfs() }
    }

    fun writeConfs() {
        viewModelScope.launch { repository.writeConfs(confs) }
    }

    fun createUser() {
        viewModelScope.launch { repository.createUser() }
    }

    fun readUsers() {
        viewModelScope.launch { repository.readUsers() }
    }

    fun updateUser(it: User) {
        viewModelScope.launch { repository.updateUser(it) }
    }

    fun deleteUser(it: User) {
        viewModelScope.launch { repository.deleteUser(it) }
    }

    fun deleteLpu(it: Lpu) {
        viewModelScope.launch { repository.deleteLpu(it) }
    }

    fun checkPatient(it: User) {
        viewModelScope.launch { repository.checkPatient(it) }
    }

    fun setCurrentUser(it: User) {
        viewModelScope.launch { repository.setCurrentUser(it) }
    }

    fun readDistrs() {
        viewModelScope.launch { repository.readDistrs() }
    }

    fun readLpusFull() {
        viewModelScope.launch { repository.readLpusFull() }
    }

    fun readLpus(did: String, uid: String) {
        viewModelScope.launch { repository.readLpus(did, uid) }
    }

    fun readSpecs(idLpu: String) {
        viewModelScope.launch { repository.readSpecs(idLpu) }
    }

    fun readDoctors(idLpu: String, idSpec: String, idPat: String) {
        viewModelScope.launch { repository.readDoctors(idLpu, idSpec, idPat) }
    }

    fun readTalons(idLpu: String, idDoc: String, idPat: String) {
        viewModelScope.launch { repository.readTalons(idLpu, idDoc, idPat) }
    }

    fun readHists(user: User) {
        viewModelScope.launch { repository.readHists(user) }
    }

    fun getTalon(idLpu: String, idAppointment: String, idPat: String) {
        viewModelScope.launch { repository.getTalon(idLpu, idAppointment, idPat) }
    }

    fun delTalon(idLpu: String, idAppointment: String, idPat: String) {
        viewModelScope.launch { repository.delTalon(idLpu, idAppointment, idPat) }
    }


}
