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

class Repository {
    private lateinit var db: AppDatabase
    private val _wait = MutableLiveData(false); val wait: LiveData<Boolean> = _wait
    private val _cuser = MutableLiveData<User>(); val cuser: LiveData<User> = _cuser
    private val _palette = MutableLiveData("Фиолетовая"); val palette: LiveData<String> = _palette

    private val _users = MutableLiveData<List<User>>(); val users: LiveData<List<User>> = _users
    private val _lpus = MutableLiveData<List<Lpu>>(); val lpus: LiveData<List<Lpu>> = _lpus
    private val _distrs = MutableLiveData<List<Distr>>(); val distrs: LiveData<List<Distr>> = _distrs
    private val _history = MutableLiveData<List<Hist>>(); val history: LiveData<List<Hist>> = _history
    private val _specs = MutableLiveData<List<Spec>>(); val specs: LiveData<List<Spec>> = _specs
    private val _docs = MutableLiveData<List<Doc>>(); val docs: LiveData<List<Doc>> = _docs
    private val _talons = MutableLiveData<List<Talon>>(); val talons: LiveData<List<Talon>> = _talons
    private val _idtalon = MutableLiveData<String>(); val idtalon: LiveData<String> = _idtalon
    private val _confs = MutableLiveData<List<Conf>>(); val confs: LiveData<List<Conf>> = _confs

    @Database(entities = [
        User::class,
        Lpuf::class,
        Lpu::class,
        Distr::class,
        Hist::class,
        Conf::class,
                         ], version = 1, exportSchema = false)

    abstract class AppDatabase : RoomDatabase() {
        abstract fun userDao(): UserDao
        abstract fun lpufDao(): LpufDao
        abstract fun lpuDao(): LpuDao
        abstract fun distrDao(): DistrDao
        abstract fun histDao(): HistDao
        abstract fun confDao(): ConfDao
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
            if (dlist.isNullOrEmpty()) {
                dlist = fromDistrMap(Hub2().getDistrList("GetDistrictList"))
                dlist.forEach { db.distrDao().create(it) }
            }
            //Log.d("jop","$dlist")
            _distrs.postValue(dlist)
        }
        _wait.postValue(false)
    }

    suspend fun readLpus(did: String, uid: String) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val args = arrayOf(did)
            var llist = db.lpuDao().readByDid(did, uid)
            if (llist.isEmpty()) {
                llist = fromLpuMap(did, uid, Hub2().getLpuList("GetLPUList", args))
                llist.forEach {
                    //Log.d("jop", "$it")
                    db.lpuDao().create(it)
                }
            }
            llist.forEach {
                val lpu = db.lpufDao().readByIdf(it.lid)
                if (lpu != null) {
                    it.address = lpu.address
                    it.phone = lpu.phone
                    it.email = lpu.email
                }
                db.lpuDao().update(it)
            }
            _lpus.postValue(llist)
        }
        _wait.postValue(false)
    }

    suspend fun readLpusFull() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            var llist = db.lpufDao().readf()
            if (llist.isNullOrEmpty()) {
                val args = arrayOf("")
                llist = fromLpuMapF(Hub2().getLpuList("GetLpuToRFSZIList", args))
                llist.forEach { db.lpufDao().createf(it) }
            }
        }
        _wait.postValue(false)
    }

    suspend fun deleteLpu(it: Lpu) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            db.lpuDao().delete(it)
            val lp = db.lpuDao().readByDid(it.did, it.uid)
            _lpus.postValue(lp)
        }
        _wait.postValue(false)
    }

    suspend fun createUser() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val maxid = db.userDao().readMaxId()
            db.userDao().create(User(maxid + 1))
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
            it.iLpu.toString(),
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
        it.Dat = result["Success"]
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

    suspend fun setCurrentUser(it: User) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            _cuser.postValue(it)
        }
        _wait.postValue(false)
    }

    suspend fun readHists(user: User) {
        val args = arrayOf(user.iLpu.toString(), user.idPat.toString())
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            var hlist = listOf<Hist>()//db.histDao().readByUidLid(uid, idLpu)
            if (hlist.isEmpty()) {
                hlist = fromHistMap(user, Hub2().getHistList("GetPatientHistory", args))
                //hlist.forEach { db.histDao().delete(it) }
                //hlist.forEach { db.histDao().create(it) }
            }
            _history.postValue(hlist)
        }
        _wait.postValue(false)
    }

    suspend fun getTalon(idLpu: String, idAppointment: String, idPat: String) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val args = arrayOf(idLpu, idAppointment, idPat)
            val res = Hub2().getTalon("SetAppointment", args)
            Log.d("jop","$res")
            if (res[0]["Success"] == "true") {
                _idtalon.postValue("Талон $idAppointment отложен успешно!")
            } else {
                _idtalon.postValue("ВНИМАНИЕ: в записи отказано!")
            }
        }
        _wait.postValue(false)
    }

    suspend fun delTalon(idLpu: String, idAppointment: String, idPat: String) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            val args = arrayOf(idLpu, idPat, idAppointment)
            val res = Hub2().deleteTalon("CreateClaimForRefusal", args)
            Log.d("jop","$res")
            if (res[0]["Success"] == "true") {
                _idtalon.postValue("Талон $idAppointment отменен успешно!")
            } else {
                _idtalon.postValue("ВНИМАНИЕ: отмена НЕ удалась!")
            }
        }
        _wait.postValue(false)
    }

    suspend fun readConfs() {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            var confs = db.confDao().read()
            if (confs.isNullOrEmpty()) db.confDao().create(Conf())
            confs = db.confDao().read()
            _confs.postValue(confs)
        }
        _wait.postValue(false)
    }

    suspend fun writeConfs(confs: LiveData<List<Conf>>) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {
            confs.value?.forEach { db.confDao().update(it) }
        }
        _wait.postValue(false)
    }

    suspend fun setPalette(theme: String) {
        _wait.postValue(true)
        withContext(Dispatchers.IO) {

            confs.value?.forEach {
                when (it.name) {
                    "palette" -> it.value = theme
                }
            }

            _palette.postValue(theme)
        }
        _wait.postValue(false)
    }
}
