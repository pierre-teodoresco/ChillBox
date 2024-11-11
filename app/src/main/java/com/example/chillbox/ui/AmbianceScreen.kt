package com.example.chillbox

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.chillbox.ui.components.BackButton
import com.example.chillbox.ui.theme.ChillBoxTheme

class AmbianceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChillBoxTheme {
                AmbianceScreen()
            }
        }
    }
}

@Composable
fun AmbianceScreen() {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Define scaling factor based on screen width (e.g., tablets or large devices)
    val scaleFactor = if (screenWidthDp > 600) 2.0f else 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((16 * scaleFactor).dp)
    ) {
        // Reusable BackButton from the BackButton.kt file
        BackButton(scaleFactor = scaleFactor)

        // Display the interactive image
        DisplayImage(
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DisplayImage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var relativePosition by remember { mutableStateOf<Rect?>(null) }
    var imageView by remember { mutableStateOf<ImageView?>(null) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    setImageResource(R.drawable.island_true)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
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
                    relativePosition?.let { position ->
                        imageView?.let { view ->
                            val imageBounds = getImageBounds(view)
                            val imageWidth = imageBounds.right - imageBounds.left
                            val imageHeight = imageBounds.bottom - imageBounds.top

                            val rings = listOf(
                                Ring(Color.Red, 0.52f, 0.2f, 0.19f, 0.27f, R.raw.temple),
                                Ring(Color.Yellow, 0.22f, 0.35f, 0.16f, 0.27f, R.raw.waterfall),
                                Ring(Color.Green, 0.35f, 0.18f, 0.16f, 0.27f, R.raw.forest),
                                Ring(Color.Cyan, 0.20f, 0.75f, 0.3f, 0.16f, R.raw.beach),
                                Ring(Color.Black, 0.515f, 0.87f, 0.1f, 0.1f, R.raw.twinkle_sound)
                            )

                            for (ring in rings) {
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
                                    playSound(context, mediaPlayer, ring.soundResId)
                                    break
                                }
                            }
                        }
                    }
                }
            }
        ) {
            relativePosition?.let { position ->
                imageView?.let { view ->
                    val imageBounds = getImageBounds(view)

                    // Width and height of the image
                    val imageWidth = imageBounds.right - imageBounds.left
                    val imageHeight = imageBounds.bottom - imageBounds.top

                    val strokeWidth = 17f // Adjust the stroke width as needed

                    val shadowOffset = 5f // Adjust the shadow offset as needed

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

// Data class to represent a ring with an associated sound resource
data class Ring(
    val color: Color,
    val mainRingXPercentage: Float,
    val mainRingYPercentage: Float,
    val mainRingWidthPercentage: Float,
    val mainRingHeightPercentage: Float,
    val soundResId: Int
)

// Helper function to play a sound
fun playSound(context: Context, mediaPlayer: MutableState<MediaPlayer?>, soundResId: Int) {
    mediaPlayer.value?.release()  // Release any existing MediaPlayer instance
    mediaPlayer.value = MediaPlayer.create(context, soundResId)
    mediaPlayer.value?.isLooping = true  // Set looping to true
    mediaPlayer.value?.start()
}

@Preview(showBackground = true)
@Composable
fun AmbianceScreenPreview() {
    ChillBoxTheme {
        AmbianceScreen()
    }
}
