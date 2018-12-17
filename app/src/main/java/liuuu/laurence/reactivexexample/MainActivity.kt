package liuuu.laurence.reactivexexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import liuuu.laurence.reactivexexample.dialogfragments.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxJavaBasicButton.setOnClickListener {
            RxJavaBasicDialogFragment.newInstance().show(supportFragmentManager, "Basic")
        }

        rxKotlinBasicButton.setOnClickListener {
            RxKotlinBasicDialogFragment.newInstance().show(supportFragmentManager, "RxKotlinBasic")
        }

        operatorButton.setOnClickListener {
            OperatorDialogFragment.newInstance().show(supportFragmentManager, "Operator")
        }

        compositeDisposableButton.setOnClickListener {
            CompositeDisposableDialogFragment.newInstance().show(supportFragmentManager, "CompositeDisposable")
        }

        customDataButton.setOnClickListener {
            CustomDataDialogFragment.newInstance().show(supportFragmentManager, "CustomData")
        }

        gitHubUserButton.setOnClickListener {
            startActivity<GitHubUserActivity>()
        }

        gitHubUserStartWithSButton.setOnClickListener {
            startActivity<GitHubUserStartWithSActivity>()
        }

        myGitHubRepoButton.setOnClickListener {
            startActivity<GitHubRepoActivity>()
        }
    }
}
