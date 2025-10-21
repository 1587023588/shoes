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
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

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
            CategoryItem(R.drawable.cat_5, "线上销售"),
            CategoryItem(R.drawable.cat_6, "非遗学习体验"),
            CategoryItem(R.drawable.cat_7, "搭配时刻"),
            CategoryItem(R.drawable.cat_8, "文创周边"),
            CategoryItem(R.drawable.cat_9, "私人定制"),
            CategoryItem(R.drawable.cat_10, "精品布鞋"),
            CategoryItem(R.drawable.cat_11, "优惠活动"),
            CategoryItem(R.drawable.cat_12, "更多")
        )

        // 组2：团结北庄（2x2 共4项）
        val group2 = listOf(
            CategoryItem(R.drawable.cat_12, "云上场馆"),
            CategoryItem(R.drawable.cat_13, "村级活动"),
            CategoryItem(R.drawable.cat_14, "预约参观"),
            CategoryItem(R.drawable.cat_15, "红色研学")
        )

        // 组3：红色西柏坡（2x2 共4项）
        val group3 = listOf(
            CategoryItem(R.drawable.cat_16, "西柏坡精神"),
            CategoryItem(R.drawable.cat_17, "团结精神"),
            CategoryItem(R.drawable.cat_18, "拥军故事"),
            CategoryItem(R.drawable.cat_19, "红色资源")
        )

        binding.grid1.adapter = CategoryAdapter(group1)
        binding.grid2.adapter = CategoryAdapter(group2)
        binding.grid3.adapter = CategoryAdapter(group3)

    // 将顶部与三组标题左侧图标的白底抠除（把接近白色的像素置为透明）
    tryRemoveWhiteBackground(binding.categoryHeaderIcon)
        tryRemoveWhiteBackground(binding.section1Icon)
        tryRemoveWhiteBackground(binding.section2Icon)
        tryRemoveWhiteBackground(binding.section3Icon)

    // 使用“星星”装饰行（青绿色，14dp 大小，8dp 间距）
    fillStarsRow(binding.section1StarsRow, starSizeDp = 14, spacingDp = 8)
    fillStarsRow(binding.section2StarsRow, starSizeDp = 14, spacingDp = 8)
    fillStarsRow(binding.section3StarsRow, starSizeDp = 14, spacingDp = 8)

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
                when {
                    item.label.contains("精品布鞋") -> {
                        startActivity(android.content.Intent(requireContext(), BoutiqueShoesActivity::class.java))
                    }
                    item.label.contains("优惠活动") -> {
                        startActivity(android.content.Intent(requireContext(), CouponActivity::class.java))
                    }
                    item.label.contains("线上销售") -> {
                        startActivity(android.content.Intent(requireContext(), OnlineSalesActivity::class.java))
                    }
                    item.label.contains("云上场馆") -> {
                        startActivity(android.content.Intent(requireContext(), MuseumActivity::class.java))
                    }
                        item.label.contains("村级活动") -> {
                            startActivity(android.content.Intent(requireContext(), VillageActivitiesActivity::class.java))
                        }
                    item.label.contains("预约参观") -> {
                        startActivity(android.content.Intent(requireContext(), BookingActivity::class.java))
                    }
                    item.label.contains("西柏坡精神") -> {
                        startActivity(android.content.Intent(requireContext(), XibaopoActivity::class.java))
                    }
                    item.label.contains("团结精神") -> {
                        val posterIntent = android.content.Intent(requireContext(), PosterActivity::class.java)
                        posterIntent.putExtra(PosterActivity.EXTRA_RES_ID, R.drawable.team)
                        startActivity(posterIntent)
                    }
                    item.label.contains("拥军故事") -> {
                        val posterIntent = android.content.Intent(requireContext(), PosterActivity::class.java)
                        posterIntent.putExtra(PosterActivity.EXTRA_RES_ID, R.drawable.arm)
                        startActivity(posterIntent)
                    }
                    item.label.contains("红色资源") -> {
                        val posterIntent = android.content.Intent(requireContext(), PosterActivity::class.java)
                        posterIntent.putExtra(PosterActivity.EXTRA_RES_ID, R.drawable.red)
                        startActivity(posterIntent)
                    }
                    else -> Toast.makeText(requireContext(), item.label, Toast.LENGTH_SHORT).show()
                }
            }
            return view
        }
    }

    /**
     * 将 drawable 中接近白色(#FFFFFF)的像素转换为透明，避免白底。
     * 仅用于小尺寸图标，性能开销可忽略。
     */
    private fun tryRemoveWhiteBackground(target: ImageView?, threshold: Int = 245) {
        if (target == null) return
        val d: Drawable = target.drawable ?: return
        // 计算位图尺寸：按视图大小渲染，若不可用则退回 intrinsic 尺寸
        val width = if (target.width > 0) target.width else (d.intrinsicWidth.takeIf { it > 0 } ?: 72)
        val height = if (target.height > 0) target.height else (d.intrinsicHeight.takeIf { it > 0 } ?: 72)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)

        val pixels = IntArray(width * height)
        bmp.getPixels(pixels, 0, width, 0, 0, width, height)
        var nonTransparentBefore = 0
        for (p in pixels) if (((p ushr 24) and 0xFF) > 0) nonTransparentBefore++

        for (i in pixels.indices) {
            val color = pixels[i]
            val a = (color ushr 24) and 0xFF
            val r = (color ushr 16) and 0xFF
            val g = (color ushr 8) and 0xFF
            val b = color and 0xFF
            if (a > 0 && r >= threshold && g >= threshold && b >= threshold) {
                // 将近白像素设为完全透明
                pixels[i] = (0x00 shl 24) or (r shl 16) or (g shl 8) or b
            }
        }

        var nonTransparentAfter = 0
        for (p in pixels) if (((p ushr 24) and 0xFF) > 0) nonTransparentAfter++

        // 如果去白后几乎全透明，说明主体是白色，回退为不去白版本，避免“啥也看不见”
        if (nonTransparentAfter < nonTransparentBefore / 20) { // <5%
            target.setImageDrawable(d)
            return
        }

        bmp.setPixels(pixels, 0, width, 0, 0, width, height)
        target.setImageDrawable(BitmapDrawable(resources, bmp))
    }

    // 在一行里按可用宽度添加星星，固定大小与间距，确保在同一基线
    private fun fillStarsRow(container: LinearLayout, starSizeDp: Int = 14, spacingDp: Int = 8) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val ctx = container.context
                val density = ctx.resources.displayMetrics.density
                val starSizePx = (starSizeDp * density).toInt()
                val spacingPx = (spacingDp * density).toInt()
                val width = container.width
                // 生成“真实五角星”位图，避免依赖外部资源导致显示为方块
                val starBmp = createTealStarBitmap(starSizePx)
                container.removeAllViews()

                if (width <= 0) {
                    // 宽度尚不可用，回退固定展示 6 个，确保可见
                    repeat(6) { idx ->
                        val iv = ImageView(ctx)
                        iv.setImageBitmap(starBmp)
                        val lp = LinearLayout.LayoutParams(starSizePx, starSizePx)
                        if (idx > 0) lp.leftMargin = spacingPx
                        container.addView(iv, lp)
                    }
                    return
                }

                var used = 0
                while (used + starSizePx <= width) {
                    val iv = ImageView(ctx)
                    iv.setImageBitmap(starBmp)
                    val lp = LinearLayout.LayoutParams(starSizePx, starSizePx)
                    if (used > 0) lp.leftMargin = spacingPx
                    container.addView(iv, lp)
                    used += starSizePx + spacingPx
                }
            }
        })
    }

    // 生成青绿色五角星位图（纯代码绘制），大小为 sizePx
    private fun createTealStarBitmap(sizePx: Int): Bitmap {
        val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(requireContext(), R.color.title_teal)
        }
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val outerR = sizePx * 0.5f
        val innerR = outerR * 0.382f // 五角星内外半径比例近似
        val path = android.graphics.Path()
        for (i in 0 until 10) {
            val angle = (-90.0 + i * 36.0) * PI / 180.0
            val r = if (i % 2 == 0) outerR else innerR
            val x = (cx + r * cos(angle)).toFloat()
            val y = (cy + r * sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, paint)
        return bmp
    }

    // 在一行里按可用宽度添加“小方块”装饰，颜色使用标题青绿色，匹配截图样式
    private fun fillBlocksRow(
        container: LinearLayout,
        sizeDp: Int = 8,
        spacingDp: Int = 6,
        cornerDp: Float = 1.5f,
        colorRes: Int = R.color.title_teal
    ) {
        container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val ctx = container.context
                val density = ctx.resources.displayMetrics.density
                val sizePx = (sizeDp * density).toInt()
                val spacingPx = (spacingDp * density).toInt()
                val cornerPx = cornerDp * density
                val width = container.width
                container.removeAllViews()

                fun addOne(index: Int) {
                    val v = View(ctx)
                    val gd = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(ContextCompat.getColor(ctx, colorRes))
                        cornerRadius = cornerPx
                    }
                    v.background = gd
                    val lp = LinearLayout.LayoutParams(sizePx, sizePx)
                    if (index > 0) lp.leftMargin = spacingPx
                    container.addView(v, lp)
                }

                if (width <= 0) {
                    // 宽度不可用时的兜底：固定绘制 10 个方块
                    repeat(10) { addOne(it) }
                    return
                }

                var used = 0
                var idx = 0
                while (used + sizePx <= width) {
                    addOne(idx)
                    used += sizePx + spacingPx
                    idx++
                }
            }
        })
    }

    // 生成指定像素大小的去白底星星位图
    private fun createStarBitmap(targetSizePx: Int, threshold: Int = 245): Bitmap {
        val src = resources.getDrawable(R.drawable.cat_4, null)
        val w = src.intrinsicWidth.takeIf { it > 0 } ?: targetSizePx
        val h = src.intrinsicHeight.takeIf { it > 0 } ?: targetSizePx

        fun render(drawable: Drawable): Bitmap {
            val b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            drawable.setBounds(0, 0, w, h)
            drawable.draw(c)
            return b
        }

        // 原始位图
        val original = render(src)

        // 去白处理
        val processed = original.copy(Bitmap.Config.ARGB_8888, true)
        val pixels = IntArray(w * h)
        processed.getPixels(pixels, 0, w, 0, 0, w, h)
        var before = 0
        for (p in pixels) if (((p ushr 24) and 0xFF) > 0) before++
        for (i in pixels.indices) {
            val color = pixels[i]
            val a = (color ushr 24) and 0xFF
            val r = (color ushr 16) and 0xFF
            val g = (color ushr 8) and 0xFF
            val b = color and 0xFF
            if (a > 0 && r >= threshold && g >= threshold && b >= threshold) {
                pixels[i] = (0x00 shl 24) or (r shl 16) or (g shl 8) or b
            }
        }
        processed.setPixels(pixels, 0, w, 0, 0, w, h)
        var after = 0
        for (p in pixels) if (((p ushr 24) and 0xFF) > 0) after++

        // 如果去白让主体丢失，则使用原图（不去白）
        val base = if (after < before / 20) original else processed

        // 统一着色为标题青绿色，避免白星在浅底上不可见
        val colored = Bitmap.createBitmap(base.width, base.height, Bitmap.Config.ARGB_8888)
        run {
            val cnv = Canvas(colored)
            cnv.drawBitmap(base, 0f, 0f, Paint().apply {
                colorFilter = PorterDuffColorFilter(resources.getColor(R.color.title_teal, null), PorterDuff.Mode.SRC_IN)
                isAntiAlias = true
            })
        }

        // 缩放到目标尺寸
        return Bitmap.createScaledBitmap(colored, targetSizePx, targetSizePx, true)
    }

    // 生成基于 cat_4.png 的“去白底”平铺背景
    private fun createStarsTiledDrawable(threshold: Int = 245): Drawable? {
        val src = resources.getDrawable(R.drawable.cat_4, null) ?: return null
        // 渲染到位图
        val w = src.intrinsicWidth.takeIf { it > 0 } ?: 24
        val h = src.intrinsicHeight.takeIf { it > 0 } ?: 24
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        src.setBounds(0, 0, w, h)
        src.draw(c)
        // 去白底
        val pixels = IntArray(w * h)
        bmp.getPixels(pixels, 0, w, 0, 0, w, h)
        for (i in pixels.indices) {
            val color = pixels[i]
            val a = (color ushr 24) and 0xFF
            val r = (color ushr 16) and 0xFF
            val g = (color ushr 8) and 0xFF
            val b = color and 0xFF
            if (a > 0 && r >= threshold && g >= threshold && b >= threshold) {
                pixels[i] = (0x00 shl 24) or (r shl 16) or (g shl 8) or b
            }
        }
        bmp.setPixels(pixels, 0, w, 0, 0, w, h)
        // 计算非透明区域包围盒，裁剪掉透明留白
        var minX = w; var minY = h; var maxX = -1; var maxY = -1
        run {
            var y = 0; var idx = 0
            while (y < h) {
                var x = 0
                while (x < w) {
                    val a = (pixels[idx] ushr 24) and 0xFF
                    if (a > 0) {
                        if (x < minX) minX = x
                        if (x > maxX) maxX = x
                        if (y < minY) minY = y
                        if (y > maxY) maxY = y
                    }
                    x++; idx++
                }
                y++
            }
        }
        val cropBmp = if (maxX >= minX && maxY >= minY) {
            Bitmap.createBitmap(bmp, minX, minY, (maxX - minX + 1), (maxY - minY + 1))
        } else bmp

        // 用 BitmapShader 平铺，横纵均重复；并按 View 高度缩放使星星完整可见
        val shader = BitmapShader(cropBmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        val paint = Paint().apply { isAntiAlias = true; this.shader = shader }
        val matrix = Matrix()
        return object : Drawable() {
            override fun draw(canvas: Canvas) {
                val rect = bounds
                // 依据目标高度等比缩放位图，让单个星星在竖直方向完整显示
                val srcH = cropBmp.height.toFloat()
                val scale = if (srcH > 0) rect.height().toFloat() / srcH else 1f
                matrix.reset()
                matrix.setScale(scale, scale)
                shader.setLocalMatrix(matrix)
                canvas.drawRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), paint)
            }
            override fun setAlpha(alpha: Int) { paint.alpha = alpha }
            override fun getAlpha(): Int = paint.alpha
            override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) { paint.colorFilter = colorFilter }
            @Deprecated("Deprecated in Java")
            override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT
        }
    }
}
