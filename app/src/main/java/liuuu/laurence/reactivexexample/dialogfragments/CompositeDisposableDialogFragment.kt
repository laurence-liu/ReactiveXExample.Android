package liuuu.laurence.reactivexexample.dialogfragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reactive.*
import liuuu.laurence.reactivexexample.R

class CompositeDisposableDialogFragment : DialogFragment() {

    private var mCompositeDisposable = CompositeDisposable()
    private var mResult: String = ""

    companion object {
        fun newInstance(): CompositeDisposableDialogFragment {
            return CompositeDisposableDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val foodsObservable = getFoodsObservable()
        val foodsObserver = getFoodsObserver()
        val foodsCapsObserver = getFoodsCapsObserver()

        mCompositeDisposable.add(
                foodsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { s -> s.toLowerCase().startsWith("c") }
                        .subscribeWith(foodsObserver))

        mCompositeDisposable.add(
                foodsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter { s -> s.toLowerCase().startsWith("b") }
                        .map { s -> s.toUpperCase() }
                        .subscribeWith(foodsCapsObserver)
        )
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

    private fun getFoodsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                Log.i("Foods onNext", t)
                mResult += "$t, "
            }

            override fun onError(e: Throwable) {
                Log.i("Foods onError", e.toString())
            }

            override fun onComplete() {
                Log.i("Foods onComplete", "Foods DONE!!")
//                resultTextView.text = mResult
            }
        }
    }

    private fun getFoodsCapsObserver(): DisposableObserver<String> {
        return object : DisposableObserver<String>() {
            override fun onNext(t: String) {
                Log.i("FoodsCaps onNext", t)
                mResult += "$t, "
            }

            override fun onError(e: Throwable) {
                Log.i("FoodsCaps onError", e.toString())
            }

            override fun onComplete() {
                Log.i("FoodsCaps onComplete", "FoodsCaps DONE!!")
                resultTextView.text = mResult
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable.clear()
    }
}