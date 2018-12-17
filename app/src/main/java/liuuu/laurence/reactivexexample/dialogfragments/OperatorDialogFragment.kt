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
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reactive.*
import liuuu.laurence.reactivexexample.R

class OperatorDialogFragment: DialogFragment() {

    private lateinit var mDisposable: Disposable
    private var mResult: String = ""

    companion object {
        fun newInstance(): OperatorDialogFragment {
            return OperatorDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodsObservable = getFoodsObservable()
        val foodsObserver = getFoodsObserver()

//        foodsObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(object: Predicate<String> {
//                    override fun test(p0: String): Boolean {
//                        return p0.toLowerCase().startsWith("b")
//                    }
//                })
//                .subscribe(foodsObserver)

        foodsObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter { s -> s.toLowerCase().startsWith("c") }
                    .subscribe(foodsObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_reactive, container, false)
    }

    private fun getFoodsObservable(): Observable<String> {
        val foodArray = arrayOf("Apple", "Arugula",
                "Bacon", "Banana", "Beef", "Bread", "Butter",
                "Cacao", "Cherry", "Chocolate", "Curry",
                "Danish", "Donut", "Dumpling",
                "Fennel", "Fish", "Fudge")
        return Observable.fromArray(*foodArray)
    }

    private fun getFoodsObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                Log.i("onSubscribe", d.toString())
                mDisposable = d
            }

            override fun onNext(t: String) {
                Log.i("onNext", t)
                mResult += "$t, "
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