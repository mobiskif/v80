package ru.mobiskif.jetpack

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class Model : ViewModel() {
    private val repository = Repository()
    val state = MutableLiveData("Выбрать пациента")
    val wait = repository.wait
    val lpus = repository.lpus
    val distrs = repository.distrs
    val users = repository.users
    var cuser = repository.cuser
    val specs = repository.specs
    val docs = repository.docs
    val talons = repository.talons
    val history = repository.history
    val idtalon = repository.idtalon

    fun repaint() {
        //wait.apply { MutableLiveData(true) }
        //wait.apply { MutableLiveData(false) }
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

    fun setPalette(context: Context, theme: String, user: User? = cuser.value) {
        if (user!=null) {
            user.Palette = theme
            updateUser(user)
        }
        LightPalette = setLightPalette(context, theme)
    }

    fun setDBContext(context: Context) {
        viewModelScope.launch { repository.setDBContext(context) }
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

    fun readLpus(it: String) {
        viewModelScope.launch { repository.readLpus(it) }
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
