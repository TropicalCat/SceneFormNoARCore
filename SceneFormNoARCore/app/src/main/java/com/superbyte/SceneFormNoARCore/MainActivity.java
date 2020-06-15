package com.superbyte.SceneFormNoARCore;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
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
        scene = sceneView.getScene();
        renderObject(Uri.parse("cupcake.sfb")); // Render the object
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
