package com.example.chillbox.ui.ambiance

import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chillbox.R
import com.example.chillbox.ui.components.BackButton

@Composable
fun AmbianceScreen(
    navController: NavController,
    viewModel: AmbianceViewModel = viewModel(),
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp)
    ) {
        // Back button to home screen
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
            BackButton(
                navController = navController,
                scaleFactor = scaleFactor
            )
        }

        // Display the interactive image
        DisplayImage(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )
    }
}

@Composable
fun DisplayImage(modifier: Modifier = Modifier, viewModel: AmbianceViewModel) {
    val context = LocalContext.current
    var relativePosition by remember { mutableStateOf<Rect?>(null) }
    var imageView by remember { mutableStateOf<ImageView?>(null) }

    // Observe changes in the current image
    val currentImageResId by remember {
        derivedStateOf {
            when (viewModel.currentImage.value) {
                0 -> R.drawable.island_true
                1 -> R.drawable.mountain
                else -> R.drawable.island_true
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.setImageResource(currentImageResId)
                imageView = view
                view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        relativePosition = getRelativePosition(view)
                    }
                })
            }
        )

        // Draw a point in all corners of the image using the relative position
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    relativePosition?.let {
                        imageView?.let { view ->
                            val imageBounds = getImageBounds(view)
                            val imageWidth = imageBounds.right - imageBounds.left
                            val imageHeight = imageBounds.bottom - imageBounds.top

                            for ((index, ring) in viewModel.rings.withIndex()) {
                                val ringBounds = getRingBounds(
                                    imageBounds,
                                    imageWidth,
                                    imageHeight,
                                    ring.mainRingXPercentage,
                                    ring.mainRingYPercentage,
                                    ring.mainRingWidthPercentage,
                                    ring.mainRingHeightPercentage
                                )

                                if (ringBounds.contains(offset.x.toInt(), offset.y.toInt())) {
                                    viewModel.playSound(context, ring.soundResId)
                                    if (index == viewModel.rings.size - 1) {
                                        viewModel.switchImage()
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
            }
        ) {
            relativePosition?.let {
                imageView?.let { view ->
                    val imageBounds = getImageBounds(view)

                    // Width and height of the image
                    val imageWidth = imageBounds.right - imageBounds.left
                    val imageHeight = imageBounds.bottom - imageBounds.top

                    val strokeWidth = 17f // Adjust the stroke width as needed

                    val shadowOffset = 5f // Adjust the shadow offset as needed

                    // Draw those rings if currentImageResId is R.drawable.island_true
                    if (currentImageResId == R.drawable.island_true) {

                        drawRingWithShadow(
                            color = Color.Red,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.52f,
                            mainRingYPercentage = 0.2f,
                            mainRingWidthPercentage = 0.19f,
                            mainRingHeightPercentage = 0.27f
                        )

                        drawRingWithShadow(
                            color = Color.Yellow,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.22f,
                            mainRingYPercentage = 0.35f,
                            mainRingWidthPercentage = 0.16f,
                            mainRingHeightPercentage = 0.27f
                        )

                        drawRingWithShadow(
                            color = Color.Green,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.35f,
                            mainRingYPercentage = 0.18f,
                            mainRingWidthPercentage = 0.16f,
                            mainRingHeightPercentage = 0.27f
                        )

                        drawRingWithShadow(
                            color = Color.Cyan,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.20f,
                            mainRingYPercentage = 0.75f,
                            mainRingWidthPercentage = 0.3f,
                            mainRingHeightPercentage = 0.16f
                        )

                        drawRingWithShadow(
                            color = Color.Black,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.515f,
                            mainRingYPercentage = 0.87f,
                            mainRingWidthPercentage = 0.1f,
                            mainRingHeightPercentage = 0.1f
                        )

                    } else {
                        // Draw those rings if currentImageResId is R.drawable.mountain
                        drawRingWithShadow(
                            color = Color.Blue,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.4f,
                            mainRingYPercentage = 0.7f,
                            mainRingWidthPercentage = 0.35f,
                            mainRingHeightPercentage = 0.15f
                        )

                        drawRingWithShadow(
                            color = Color.White,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.42f,
                            mainRingYPercentage = 0.17f,
                            mainRingWidthPercentage = 0.12f,
                            mainRingHeightPercentage = 0.1f
                        )

                        drawRingWithShadow(
                            color = Color.Gray,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.17f,
                            mainRingYPercentage = 0.52f,
                            mainRingWidthPercentage = 0.2f,
                            mainRingHeightPercentage = 0.1f
                        )

                        drawRingWithShadow(
                            color = Color.Red,
                            imageBounds = imageBounds,
                            imageWidth = imageWidth,
                            imageHeight = imageHeight,
                            strokeWidth = strokeWidth,
                            shadowOffset = shadowOffset,
                            mainRingXPercentage = 0.08f,
                            mainRingYPercentage = 0.75f,
                            mainRingWidthPercentage = 0.17f,
                            mainRingHeightPercentage = 0.17f
                        )
                    }
                }
            }
        }
    }
}

// Helper function to get the bounds of a ring
fun getRingBounds(
    imageBounds: RectF,
    imageWidth: Float,
    imageHeight: Float,
    mainRingXPercentage: Float,
    mainRingYPercentage: Float,
    mainRingWidthPercentage: Float,
    mainRingHeightPercentage: Float
): Rect {
    val ringX = imageBounds.left + (mainRingXPercentage * imageWidth)
    val ringY = imageBounds.top + (mainRingYPercentage * imageHeight)
    val ringWidth = mainRingWidthPercentage * imageWidth
    val ringHeight = mainRingHeightPercentage * imageHeight

    return Rect(
        ringX.toInt(),
        ringY.toInt(),
        (ringX + ringWidth).toInt(),
        (ringY + ringHeight).toInt()
    )
}

fun DrawScope.drawRingWithShadow(
    color: Color,
    imageBounds: RectF,
    imageWidth: Float,
    imageHeight: Float,
    strokeWidth: Float,
    shadowOffset: Float,
    mainRingXPercentage: Float,
    mainRingYPercentage: Float,
    mainRingWidthPercentage: Float,
    mainRingHeightPercentage: Float
) {
    // Draw the main ring
    drawArc(
        color = color,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(imageBounds.left + mainRingXPercentage * imageWidth, imageBounds.top + mainRingYPercentage * imageHeight),
        size = Size(mainRingWidthPercentage * imageWidth, mainRingHeightPercentage * imageHeight),
        style = Stroke(width = strokeWidth)
    )

    // Draw a shadow to simulate the depth of the ring
    drawArc(
        color = Color.Black.copy(alpha = 0.5f), // Shadow color with alpha
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(imageBounds.left + mainRingXPercentage * imageWidth + shadowOffset, imageBounds.top + mainRingYPercentage * imageHeight + shadowOffset), // Slightly offset the shadow
        size = Size(mainRingWidthPercentage * imageWidth, mainRingHeightPercentage * imageHeight),
        style = Stroke(width = strokeWidth)
    )
}

private fun getRelativePosition(myView: View): Rect {
    val rect = Rect()
    var currentView: View? = myView

    while (currentView != null) {
        rect.left += currentView.left
        rect.top += currentView.top

        if (currentView.parent === currentView.rootView) {
            break
        }

        currentView = currentView.parent as? View
    }

    rect.right = rect.left + myView.width
    rect.bottom = rect.top + myView.height

    return rect
}

private fun getImageBounds(view: ImageView): RectF {
    val drawable = view.drawable ?: return RectF()
    val imageRatio = drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()
    val viewRatio = view.width.toFloat() / view.height.toFloat()

    val rect = RectF()

    if (imageRatio > viewRatio) {
        // Image is wider than the view
        val height = view.width / imageRatio
        val top = (view.height - height) / 2
        rect.set(0f, top, view.width.toFloat(), top + height)
    } else {
        // Image is taller than the view
        val width = view.height * imageRatio
        val left = (view.width - width) / 2
        rect.set(left, 0f, left + width, view.height.toFloat())
    }

    return rect
}