package liuuu.laurence.reactivexexample

import android.content.Context
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_github_user.view.*
import liuuu.laurence.reactivexexample.model.GitHubResponse.GitHubUserSince

class GitHubUserAdapter(private val mContext: Context, private val mGitHubUserSinceList: List<GitHubUserSince>) : RecyclerView.Adapter<GitHubUserAdapter.GitHubViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GitHubViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_github_user, viewGroup, false)

        return GitHubViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GitHubViewHolder, position: Int) {
        val gitHubUser = getItem(position)

        holder.nameTextView.text = gitHubUser.login
        holder.typeTextView.text = gitHubUser.type
        Picasso.get().load(gitHubUser.avatarUrl).placeholder(R.mipmap.ic_launcher).into(holder.avatarImageView)
    }

    override fun getItemCount(): Int {
        return mGitHubUserSinceList.size
    }

    fun getItem(position: Int): GitHubUserSince {
        return mGitHubUserSinceList[position]
    }

    class GitHubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.nameTextView
        val typeTextView: TextView = itemView.typeTextView
        val avatarImageView: ImageView = itemView.avatarImageView
    }
}