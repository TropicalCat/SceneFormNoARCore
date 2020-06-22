package com.superbyte.SceneFormNoARCore;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class MainActivity extends AppCompatActivity {

    private SceneView sceneView;
    private Scene scene;
    private Node cupCakeNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sceneView = (SceneView) findViewById(R.id.sceneView);
        Camera camera = sceneView.getScene().getCamera();
        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));

        scene = sceneView.getScene();
        scene.setOnTouchListener(new Scene.OnTouchListener() {
            @Override
            public boolean onSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                onTouch(hitTestResult, motionEvent);
                return false;
            }
        });

        //renderObject(Uri.parse("cupcake.sfb")); // Render the object
    }

    public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        renderObject(Uri.parse("cupcake.sfb")); // Render the object
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        sceneView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void renderObject(Uri parse){
        ModelRenderable.builder()
                .setSource(this, parse)
                .build()
                .thenAccept(
                        modelRenderable -> {
                            addNodeToScene(modelRenderable); })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load Tiger renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    private void addNodeToScene(ModelRenderable model) {
        if(model != null) {
            //cupCakeNode
            cupCakeNode = new Node();
            cupCakeNode.setParent(scene);
            cupCakeNode.setLocalPosition(new Vector3(0f, 0f, -1f));
            cupCakeNode.setLocalScale(new Vector3(3f, 3f, 3f));
            cupCakeNode.setName("Cupcake");
            cupCakeNode.setRenderable(model);

            scene.addChild(cupCakeNode);
        }
    }
}
