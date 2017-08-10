package com.example.dragdrop.views;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dragdrop.R;
import com.example.dragdrop.models.datatypes.DraggableObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    ArrayList<DraggableObject> draggableObjects;
    @BindView(R.id.area1) LinearLayout area1;
    @BindView(R.id.area2) LinearLayout area2;
    @BindView(R.id.prompt) TextView prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActivity();
    }

    private void initActivity() {
        // make TextView scrollable
        prompt.setMovementMethod(new ScrollingMovementMethod());
        //clear prompt area if LongClick
        prompt.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                prompt.setText("");
                return true;
            }});

        setupExampleObjects();

        area1.setOnDragListener(new DragListener());
        area2.setOnDragListener(new DragListener());
    }

    private void setupExampleObjects() {
        draggableObjects = new ArrayList<>();
        draggableObjects.add(draggableObjects.size(), new DraggableObject(0, "Alert", getAndroidDrawable(android.R.drawable.ic_dialog_alert)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(1, "Dialer", getAndroidDrawable(android.R.drawable.ic_dialog_dialer)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(2, "Email", getAndroidDrawable(android.R.drawable.ic_dialog_email)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(3, "Info", getAndroidDrawable(android.R.drawable.ic_dialog_info)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(4, "Map", getAndroidDrawable(android.R.drawable.ic_dialog_map)));

        for (DraggableObject obj : draggableObjects) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(obj.Drawable);
            imageView.setId(obj.getID());
            imageView.setOnLongClickListener(new LongClickListener());
            area1.addView(imageView);
        }
    }

    private Drawable getAndroidDrawable(int id){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id);
        }

        return getDrawable(id);
    }

    private void moveDraggableObject(View draggableView, LinearLayout oldParent, LinearLayout newParent){
        draggableView.setVisibility(View.VISIBLE);

        oldParent.removeView(draggableView);
        newParent.addView(draggableView);
    }

    private class LongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private class DragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            View dragView = (View) dragEvent.getLocalState();
            int viewID = dragView.getId();
            DraggableObject draggableObject = draggableObjects.get(viewID);

            String area;
            if (view == area1) {
                area = "area1";
            } else if (view == area2) {
                area = "area2";
            } else {
                area = "unknown";
            }

            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    prompt.append(String.format("ACTION_DRAG_STARTED: %s for %s \n", area, draggableObject.Name));
                    //do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    prompt.append(String.format("ACTION_DRAG_ENTERED: %s for %s \n", area, draggableObject.Name));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    prompt.append(String.format("ACTION_DRAG_EXITED: %s for %s \n", area, draggableObject.Name));
                    //do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    prompt.append(String.format("ACTION_DROP: %s for %s \n", area, draggableObject.Name));
                    moveDraggableObject(dragView, (LinearLayout) dragView.getParent(), (LinearLayout) view);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    prompt.append(String.format("ACTION_DRAG_ENDED: %s for %s \n", area, draggableObject.Name));
                default:
                    break;
            }
            return true;
        }
    }
}
