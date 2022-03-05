package me.visutrb.resumegen.ui.resumelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.visutrb.resumegen.databinding.ItemResumeBinding
import me.visutrb.resumegen.entity.Resume

class ResumeListRecyclerViewAdapter :
    RecyclerView.Adapter<ResumeListRecyclerViewAdapter.ViewHolder>() {

    private val resumes = mutableListOf<Resume>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemResumeBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumes[position]
        with(holder) {
            binding.nameTv.text = resume.fullName
        }
    }

    fun addAll(vararg resumes: Resume) {
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