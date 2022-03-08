package me.visutrb.resumegen.mvp.resumelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.visutrb.resumegen.databinding.ItemResumeBinding
import me.visutrb.resumegen.entity.Resume
import java.io.File

class ResumeListRecyclerViewAdapter :
    RecyclerView.Adapter<ResumeListRecyclerViewAdapter.ViewHolder>() {

    var onResumeSelected: ((resume: Resume) -> Unit)? = null

    private val resumes = mutableListOf<Resume>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemResumeBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumes[position]
        with(holder) {
            binding.root.setOnClickListener { onResumeSelected?.invoke(resume) }
            binding.nameTv.text = resume.fullName
            binding.jobTitleTv.text = resume.role
            resume.profilePicturePath.let {
                if (it.isBlank() || it.isEmpty()) {
                    return@let
                }
                Glide.with(binding.root)
                    .load(File(it))
                    .into(binding.profilePictureImv)
            }
        }
    }

    fun replaceAll(resumes: List<Resume>) {
        this.resumes.clear()
        this.resumes.addAll(resumes)
        notifyDataSetChanged()
    }

    fun addAll(resumes: List<Resume>) {
        val newItemsCount = resumes.size
        val currentItemsCount = this.resumes.size
        this.resumes.addAll(resumes)
        notifyItemRangeInserted(currentItemsCount, newItemsCount)
    }

    fun add(resume: Resume) {
        val currentItemsCount = resumes.size
        resumes.add(resume)
        notifyItemInserted(currentItemsCount)
    }

    fun remove(resume: Resume) {
        val index = resumes.indexOfFirst { it.id == resume.id }
        if (index == -1) {
            return
        }
        resumes.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return resumes.size
    }

    class ViewHolder(val binding: ItemResumeBinding) : RecyclerView.ViewHolder(binding.root)
}