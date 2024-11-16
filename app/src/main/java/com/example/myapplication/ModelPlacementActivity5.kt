package com.example.myapplication

import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

// MainActivity class - entry point of the app extending AppCompatActivity for Android components
class ModelPlacementActivity5 : AppCompatActivity() {
    // Lateinit properties for ARFragment, gesture detectors, model node, and anchor node
    private lateinit var arFragment: ArFragment
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var modelNode: TransformableNode? = null
    private var anchorNode: AnchorNode? = null

    // onCreate method - initializes activity state and UI components
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Set the layout for the activity

        // Initialize ARFragment by finding it in the layout
        arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment

        // Find the button in the layout and set its click listener
        val placeModelButton: Button = findViewById(R.id.place_model_button)
        placeModelButton.setOnClickListener {
            // Check if the AR camera is tracking and get the frame
            arFragment.arSceneView.arFrame?.let { frame ->
                if (frame.camera.trackingState == com.google.ar.core.TrackingState.TRACKING) {
                    // Perform a hit test to find AR planes
                    frame.hitTest(getScreenCenter().x.toFloat(), getScreenCenter().y.toFloat())
                        .firstOrNull { hit ->
                            hit.trackable is Plane && (hit.trackable as Plane).isPoseInPolygon(hit.hitPose)
                        }?.let { hitResult ->
                            // Place the model at the hit result location
                            placeModel(hitResult.createAnchor())
                        }
                }
            }
        }

        // Setup gesture detectors for the AR scene
        setupGestureDetectors()
    }

    // Method to place a 3D model at the given anchor
    private fun placeModel(anchor: Anchor) {
        // Remove existing model if any
        anchorNode?.let {
            arFragment.arSceneView.scene.removeChild(it)
            it.anchor?.detach()
        }

        // Build and render the 3D model
        ModelRenderable.builder()
            .setSource(
                this,
                RenderableSource.builder()
                    .setSource(
                        this,
                        Uri.parse("file:///android_asset/BoschBattery.glb"), // Path to the model file
                        RenderableSource.SourceType.GLB // Type of model file
                    )
                    .setRecenterMode(RenderableSource.RecenterMode.ROOT) // Center the model
                    .build()
            )
            .setRegistryId("model") // Set a unique ID for the model
            .build()
            .thenAccept { modelRenderable -> addModelToScene(anchor, modelRenderable) } // Add the model to the scene
            .exceptionally { throwable ->
                Log.e("MainActivity", "Unable to load renderable", throwable) // Log any errors
                null
            }
    }

    // Method to add the model to the AR scene at the specified anchor
    private fun addModelToScene(anchor: Anchor, modelRenderable: ModelRenderable) {
        anchorNode = AnchorNode(anchor) // Create an AnchorNode with the given anchor
        anchorNode?.setParent(arFragment.arSceneView.scene) // Attach it to the AR scene

        // Create and set up the TransformableNode to handle transformations
        modelNode = TransformableNode(arFragment.transformationSystem).apply {
            renderable = modelRenderable // Set the model to render
            setParent(anchorNode) // Attach the model to the anchor node
            select() // Select the node for transformations
        }

        // Update listener to log the model's position
        arFragment.arSceneView.scene.addOnUpdateListener {
            modelNode?.let {
                val position = it.worldPosition
                Log.d("MainActivity", "Model Position: (${position.x}, ${position.y}, ${position.z})")
            }
        }
    }

    // Method to set up gesture detectors for scaling and scrolling
    private fun setupGestureDetectors() {
        // Initialize gesture detectors with their respective listeners
        gestureDetector = GestureDetector(this, GestureListener())
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // Set a touch listener on the AR scene view to handle gestures
        arFragment.arSceneView.scene.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event) // Handle scroll gestures
            scaleGestureDetector.onTouchEvent(event) // Handle scale gestures
            return@setOnTouchListener true
        }
    }

    // Inner class to handle scroll gestures
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private var previousX: Float = 0f
        private var previousY: Float = 0f

        // Handle scroll events
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (e1 != null) {
                modelNode?.apply {
                    // Calculate displacement and update the model's position
                    val dx = e2.rawX - previousX
                    val dy = e2.rawY - previousY
                    localPosition.x += dx * 0.001f
                    localPosition.y -= dy * 0.001f
                    previousX = e2.rawX
                    previousY = e2.rawY
                }
            }
            return true
        }

        // Handle initial touch events to set previous positions
        override fun onDown(e: MotionEvent): Boolean {
            e.let {
                previousX = it.rawX
                previousY = it.rawY
            }
            return true
        }
    }

    // Inner class to handle scale gestures
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        // Handle scale events
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            modelNode?.let {
                val scaleFactor = detector.scaleFactor // Get the scale factor
                it.localScale = it.localScale.scaled(scaleFactor) // Scale the model
            }
            return true
        }
    }

    // Method to get the center of the screen
    private fun getScreenCenter(): Point {
        val vw = arFragment.requireView().width
        val vh = arFragment.requireView().height
        return Point(vw / 2, vh / 2) // Return the center point of the screen
    }
}
