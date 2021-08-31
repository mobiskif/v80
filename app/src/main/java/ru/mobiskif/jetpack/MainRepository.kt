package ru.mobiskif.jetpack

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {
    private lateinit var db: AppDatabase
    private val _wait = MutableLiveData(false)
    val wait: LiveData<Boolean> = _wait
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    private val _lpus = MutableLiveData<List<Lpu>>()
    val lpus: LiveData<List<Lpu>> = _lpus
    private val _distrs = MutableLiveData<List<Distr>>()
    val distrs: LiveData<List<Distr>> = _distrs
    private val _cuser = MutableLiveData<User>()
    val cuser: LiveData<User> = _cuser
    private val _specs = MutableLiveData<List<Spec>>()
    val specs: LiveData<List<Spec>> = _specs
    private val _docs = MutableLiveData<List<Doc>>()
    val docs: LiveData<List<Doc>> = _docs
    private val _talons = MutableLiveData<List<Talon>>()
    val talons: LiveData<List<Talon>> = _talons
    private val _history = MutableLiveData<List<Hist>>()
    val history: LiveData<List<Hist>> = _history

    @Database(entities = [User::class, Lpu::class, Distr::class, Hist::class], version = 9)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
        abstract fun lpuDao(): LpuDao
        abstract fun distrDao(): DistrDao
        abstract fun histDao(): HistDao
    }

    fun setDBContext(ac: Context) {
        val appname = ac.packageManager.getApplicationLabel(ac.applicationInfo).toString()
        val bld = Room.databaseBuilder(ac, AppDatabase::class.java, appname)
        db = bld.fallbackToDestructiveMigration().build()
    }

    suspend fun readDistrs() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            var dlist = db.distrDao().read()
            if (dlist.isEmpty()) {
                dlist = fromDistrMap(Hub2().getDistrList("GetDistrictList"))
                dlist.forEach { db.distrDao().create(it) }
            }
            _distrs.postValue(dlist)
        }
        _wait.postValue(false)
    }

    suspend fun readLpus(did: String) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            var llist = db.lpuDao().readByDid(did)
            if (llist.isEmpty()) {
                val args = arrayOf(did)
                llist = fromLpuMap(did, Hub2().getLpuList("GetLPUList", args))
                llist.forEach { db.lpuDao().create(it) }
            }
            _lpus.postValue(llist)
        }
        _wait.postValue(false)
    }

    suspend fun deleteLpu(it: Lpu) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            db.lpuDao().delete(it)
            _lpus.postValue(db.lpuDao().readByDid(it.did))
        }
        _wait.postValue(false)
    }

    suspend fun createUser() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val maxid = db.userDao().readMaxId()
            db.userDao().create(User(maxid + 1, "", "", "", "", "1", "", "", "", "","","",""))
            _users.postValue(db.userDao().read())
        }
        _wait.postValue(false)
    }

    suspend fun updateUser(it: User) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            db.userDao().update(it)
            _users.postValue(db.userDao().read())
        }
        _wait.postValue(false)
    }

    suspend fun deleteUser(it: User) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            db.userDao().delete(it)
            _users.postValue(db.userDao().read())
        }
        _wait.postValue(false)
    }

    suspend fun checkPatient(it: User) {
        val result = mutableMapOf<String, String>()
        val args = arrayOf(
            it.F.toString(),
            it.I.toString(),
            it.O.toString(),
            it.D.toString(),
            it.iL.toString(),
        )
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val res = Hub2().checkPat("CheckPatient", args)
            if (res.isNotEmpty()) {
                if (res[0]["Success"] == "true") {
                    result["IdPat"] = res[0]["IdPat"].toString()
                    result["Success"] = "true"
                } else {
                    result["IdPat"] = res[0]["ErrorDescription"].toString()
                    result["Success"] = "false"
                }
            } else {
                result["IdPat"] = "Учреждение не ответило"
                result["Success"] = "false"
            }
        }
        _wait.postValue(false)
        it.idPat = result["IdPat"]
        it.Err = result["Success"]
        _cuser.postValue(it)
    }

    suspend fun readUsers() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            _users.postValue(db.userDao().read())
        }
        _wait.postValue(false)
    }

    suspend fun readSpecs(idLpu: String) {
        val args = arrayOf(idLpu)
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val res = fromSpecMap(Hub2().getSpecList("GetSpesialityList", args))
            _specs.postValue(res)
        }
        _wait.postValue(false)
    }

    suspend fun readDoctors(idLpu: String, idSpec: String, idPat: String) {
        val args = arrayOf(idLpu, idSpec, idPat)
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val res = fromDocMap(Hub2().getDocList("GetDoctorList", args))
            _docs.postValue(res)
        }
        _wait.postValue(false)
    }

    suspend fun readTalons(idLpu: String, idDoc: String, idPat: String) {
        val args = arrayOf(idLpu, idDoc, idPat)
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val res = fromTalonMap(Hub2().getTalonList("GetAvaibleAppointments", args))
            _talons.postValue(res)
        }
        _wait.postValue(false)
    }

    suspend fun readHists(idLpu: String, idPat: String) {
        val args = arrayOf(idLpu, idPat)
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            /*
            var hlist = db.histDao().readByUid("1")
            if (hlist.isEmpty()) {
                hlist = fromHistMap(idLpu, idPat, Hub2().getHistList("GetPatientHistory", args))
                hlist.forEach { db.histDao().create(it) }
            }
            */
            val res = fromHistMap(idLpu, idPat, Hub2().getHistList("GetPatientHistory", args))
            _history.postValue(res)
        }
        _wait.postValue(false)
        /*
            var llist = db.lpuDao().readByDid(did)
            if (llist.isEmpty()) {
                val args = arrayOf(did)
                llist = fromLpuMap(did, Hub2().getLpuList("GetLPUList", args))
                llist.forEach { db.lpuDao().create(it) }
            }
            _lpus.postValue(llist)

         */
    }

    suspend fun setCurrentUser(it: User) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            _cuser.postValue(it)
        }
        _wait.postValue(false)
    }
}
