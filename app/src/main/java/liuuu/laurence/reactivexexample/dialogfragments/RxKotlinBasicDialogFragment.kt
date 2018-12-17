package liuuu.laurence.reactivexexample.dialogfragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reactive.*
import liuuu.laurence.reactivexexample.R

class RxKotlinBasicDialogFragment : DialogFragment() {

    private lateinit var mDisposable: Disposable
    private var mResult: String = ""

    companion object {
        fun newInstance(): RxKotlinBasicDialogFragment {
            return RxKotlinBasicDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val anyObservable = getAnyObservable()
        mDisposable = anyObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            Log.i("onNext", it.toString())
                            mResult += "$it, "
                        },
                        onError = {
                            Log.i("onError", it.toString())
                        },
                        onComplete = {
                            Log.i("onNext", "DONE!!")
                            resultTextView.text = mResult
                        }
                )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_reactive, container, false)
    }

    private fun getAnyObservable(): Observable<Any> {
        return listOf(true, 1, 2, "Three", 4.0f, 4.5, "Five", false).toObservable()
    }

    override fun onDestroy() {
        super.onDestroy()

        mDisposable.dispose()
    }
}