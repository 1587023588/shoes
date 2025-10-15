package com.example.shoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoes.databinding.FragmentCategoryBinding
import com.example.shoes.databinding.ItemCategoryBinding

data class CategoryItem(val iconRes: Int, val label: String)

// 顶层私有 ViewHolder，避免在函数/局部定义 data class 触发编译限制
private class CatViewHolder(val icon: ImageView, val label: TextView)

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        // 按截图严格映射图标与文案（图标来自 img 目录：cat_#.png）
        // 组1：非遗布鞋（2行x4列 共8项）
        val group1 = listOf(
            CategoryItem(R.drawable.cat_1, "线上销售"),
            CategoryItem(R.drawable.cat_6, "非遗学习体验"),
            CategoryItem(R.drawable.cat_7, "搭配时刻"),
            CategoryItem(R.drawable.cat_8, "文创周边"),
            CategoryItem(R.drawable.cat_9, "私人定制"),
            CategoryItem(R.drawable.cat_10, "新品布鞋"),
            CategoryItem(R.drawable.cat_11, "优惠活动"),
            CategoryItem(R.drawable.cat_12, "更多")
        )

        // 组2：团结北庄（2x2 共4项）
        val group2 = listOf(
            CategoryItem(R.drawable.cat_14, "云上场馆"),
            CategoryItem(R.drawable.cat_15, "村级活动"),
            CategoryItem(R.drawable.cat_16, "预约参观"),
            CategoryItem(R.drawable.cat_17, "红色研学")
        )

        // 组3：红色西柏坡（2x2 共4项）
        val group3 = listOf(
            CategoryItem(R.drawable.cat_18, "西柏坡精神"),
            CategoryItem(R.drawable.cat_19, "团结精神"),
            CategoryItem(R.drawable.cat_20, "拥军故事"),
            CategoryItem(R.drawable.cat_21, "红色资源")
        )

        binding.grid1.adapter = CategoryAdapter(group1)
        binding.grid2.adapter = CategoryAdapter(group2)
        binding.grid3.adapter = CategoryAdapter(group3)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class CategoryAdapter(private val items: List<CategoryItem>) : BaseAdapter() {
        override fun getCount() = items.size
        override fun getItem(position: Int) = items[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: CatViewHolder
            val view: View
            if (convertView == null) {
                val vb = ItemCategoryBinding.inflate(layoutInflater, parent, false)
                view = vb.root
                holder = CatViewHolder(vb.icon, vb.label)
                view.tag = holder
            } else {
                view = convertView
                holder = convertView.tag as CatViewHolder
            }
            val item = getItem(position)
            holder.icon.setImageResource(item.iconRes)
            holder.label.text = item.label
            view.setOnClickListener {
                Toast.makeText(requireContext(), item.label, Toast.LENGTH_SHORT).show()
            }
            return view
        }
    }
}
