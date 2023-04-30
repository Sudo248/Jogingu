package com.sudo248.data.repositories

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sudo248.data.firebase.FirebaseKeys
import com.sudo248.data.local.database.dao.JoginguDao
import com.sudo248.data.local.database.models.firebase.RunDocument
import com.sudo248.data.local.database.models.firebase.UserDocument
import com.sudo248.data.mapper.toRun
import com.sudo248.data.mapper.toRunDB
import com.sudo248.data.mapper.toRunDocument
import com.sudo248.data.util.toRunInStatistic
import com.sudo248.domain.common.DataState
import com.sudo248.domain.entities.Run
import com.sudo248.domain.entities.RunInStatistic
import com.sudo248.domain.entities.UserRun
import com.sudo248.domain.repositories.RunRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRepositoryImpl @Inject constructor(
    private val dao: JoginguDao,
    private val ioDispatcher: CoroutineDispatcher
) : RunRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore

    override suspend fun getAllUserRun(): Flow<DataState<List<UserRun>>> = flow {
        try {
            val querySnapshot = firestore.collection(FirebaseKeys.COLLECTION_RUN).get().await()
            val users = mutableListOf<UserDocument>()
            val runs = querySnapshot.documents.map {
                val runDocument = it.toObject(RunDocument::class.java)!!
                val user = users.firstOrNull { user -> user.userId == runDocument.userId }
                    ?: getUserDocumentById(runDocument.userId).also { user -> users.add(user) }
                UserRun(
                    userId = user.userId,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    userImageUrl = user.imageUrl,
                    run = runDocument.toRun(it.id)
                )
            }
            emit(DataState.Success(runs))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.Error(e.message ?: "Unknown"))
        }
    }

    private suspend fun getUserDocumentById(userId: String): UserDocument {
        val querySnapshot =
            firestore.collection(FirebaseKeys.COLLECTION_USER).whereEqualTo("userId", userId)
                .limit(1).get().await()
        return querySnapshot.first().toObject(UserDocument::class.java)
    }

    override suspend fun getAllRuns(): Flow<DataState<List<Run>>> = flow {
        try {
            val runDBFlow = dao.getAllRunDBs()
            emitAll(
                runDBFlow.map { list ->
                    DataState.Success(list.map { it.toRun() })
                }
            )
        } catch (e: IOException) {
            emit(DataState.Error("${e.message}"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun addNewRun(run: Run) = coroutineScope {
        val runDocument = run.toRunDocument(Firebase.auth.currentUser!!.uid)
        val runFirebase = firestore.collection(FirebaseKeys.COLLECTION_RUN).add(runDocument).await()
        dao.insertRunDB(run.copy(runId = runFirebase.id).toRunDB())
    }

    override suspend fun deleteRuns(vararg runs: Run) {
        runs.forEach { dao.deleteRunDBs(it.toRunDB()) }
    }

    override suspend fun getRunsThisDay(): Flow<DataState<List<RunInStatistic?>>> = flow {
        try {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            val runThisDay = dao.getRunsFromDay(cal.timeInMillis)
            val runsInDay = MutableList<RunInStatistic?>(24) { null }
            emitAll(
                runThisDay.map { list ->
                    for (run in list) {
                        cal.time = run.timeStart
                        val hour = cal.get(Calendar.HOUR_OF_DAY)
                        runsInDay[hour - 1] = run.toRunInStatistic()
                    }
                    DataState.Success(runsInDay)
                }
            )
        } catch (e: Exception) {
            emit(DataState.Error("${e.message}"))
        }

    }.flowOn(ioDispatcher)

    override suspend fun getRunsThisWeek(): Flow<DataState<List<RunInStatistic?>>> = flow {
        try {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            val runsThisWeek = dao.getRunsFromDay(cal.timeInMillis)
            val runsInWeek = MutableList<RunInStatistic?>(7) { null }
            emitAll(
                runsThisWeek.map { list ->
                    for (run in list) {
                        cal.time = run.timeStart
                        val day = cal.get(Calendar.DAY_OF_WEEK)
                        runsInWeek[day - 1] = run.toRunInStatistic()
                    }
                    DataState.Success(runsInWeek)
                }
            )
        } catch (e: Exception) {
            emit(DataState.Error("${e.message}"))
        }
    }.flowOn(ioDispatcher)

    override suspend fun getRunsThisMonth(): Flow<DataState<List<RunInStatistic?>>> = flow {
        try {
            Timber.d("start get run of month")
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            val runsThisMonth = dao.getRunsFromDay(cal.timeInMillis)
            val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val runsInMonth = MutableList<RunInStatistic?>(maxDay) { null }
            emitAll(
                runsThisMonth.map { list ->
                    for (run in list) {
                        cal.time = run.timeStart
                        val day = cal.get(Calendar.DAY_OF_MONTH)
                        runsInMonth[day - 1] = run.toRunInStatistic()
                    }
                    DataState.Success(runsInMonth)
                }
            )
        } catch (e: Exception) {
            emit(DataState.Error("${e.message}"))
        }
    }.flowOn(ioDispatcher)
}