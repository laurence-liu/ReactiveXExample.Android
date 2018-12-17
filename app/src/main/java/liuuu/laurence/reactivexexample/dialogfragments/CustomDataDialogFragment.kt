package liuuu.laurence.reactivexexample.dialogfragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import liuuu.laurence.reactivexexample.R
import liuuu.laurence.reactivexexample.model.State
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reactive.*

class CustomDataDialogFragment : DialogFragment() {

    private lateinit var mDisposable: Disposable
    private var mResult: String = ""

    companion object {
        fun newInstance(): CustomDataDialogFragment {
            return CustomDataDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val citiesObservable = getCitiesObservable()
        val citiesObserver = getCitiesObserver()

        citiesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { state ->
                    state.name = state.name.toUpperCase()
                    state
                }
                .subscribe(citiesObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_reactive, container, false)
    }

    private fun getCitiesObservable(): Observable<State> {
        val stateList = ArrayList<State>()
        stateList.add(State(1, "California"))
        stateList.add(State(2, "New York"))
        stateList.add(State(3, "Colorado"))
        stateList.add(State(4, "Texas"))
        stateList.add(State(5, "Alaska"))

        return Observable.fromIterable(stateList)
    }

    private fun getCitiesObserver(): Observer<State> {
        return object : Observer<State> {
            override fun onSubscribe(d: Disposable) {
                Log.i("onSubscribe", d.toString())
                mDisposable = d
            }

            override fun onNext(t: State) {
                Log.i("onNext", t.name)
                mResult += "${t.id}. ${t.name} \n"
            }

            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onComplete() {
                Log.i("onComplete", "DONE!!")
                resultTextView.text = mResult
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.dispose()
    }
}