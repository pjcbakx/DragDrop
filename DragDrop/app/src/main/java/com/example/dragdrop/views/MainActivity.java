package com.example.dragdrop.views;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dragdrop.R;
import com.example.dragdrop.models.datatypes.DraggableObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    ArrayList<DraggableObject> draggableObjects;
    @BindView(R.id.area1) LinearLayout area1;
    @BindView(R.id.area2) LinearLayout area2;
    @BindView(R.id.area3) LinearLayout area3;
    @BindView(R.id.area4) LinearLayout area4;
    @BindView(R.id.area5) LinearLayout area5;
    @BindView(R.id.area6) LinearLayout area6;
    @BindView(R.id.sourceLayout) LinearLayout sourceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActivity();
    }

    private void initActivity() {
        setupExampleObjects();

        sourceLayout.setOnDragListener(new DragListener());
        area1.setOnDragListener(new DragListener());
        area2.setOnDragListener(new DragListener());
        area3.setOnDragListener(new DragListener());
        area4.setOnDragListener(new DragListener());
        area5.setOnDragListener(new DragListener());
        area6.setOnDragListener(new DragListener());
    }

    private void setupExampleObjects() {
        draggableObjects = new ArrayList<>();
        draggableObjects.add(draggableObjects.size(), new DraggableObject(draggableObjects.size(), "Alert", getAndroidDrawable(android.R.drawable.ic_dialog_alert)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(draggableObjects.size(), "Dialer", getAndroidDrawable(android.R.drawable.ic_dialog_dialer)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(draggableObjects.size(), "Email", getAndroidDrawable(android.R.drawable.ic_dialog_email)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(draggableObjects.size(), "Info", getAndroidDrawable(android.R.drawable.ic_dialog_info)));
        draggableObjects.add(draggableObjects.size(), new DraggableObject(draggableObjects.size(), "Map", getAndroidDrawable(android.R.drawable.ic_dialog_map)));

        for (DraggableObject obj : draggableObjects) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(obj.Drawable);
            imageView.setPadding(10, 0, 10, 0);
            imageView.setId(obj.getID());
            imageView.setOnClickListener(new ClickListener());
            imageView.setOnLongClickListener(new LongClickListener());
            sourceLayout.addView(imageView);
        }
    }

    private Drawable getAndroidDrawable(int id){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id);
        }

        return getDrawable(id);
    }

    private class LongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, shadowBuilder, view, 0);
            } else {
                view.startDrag(data, shadowBuilder, view, 0);
            }
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int viewID = view.getId();
            DraggableObject draggableObject = draggableObjects.get(viewID);

            Toast.makeText(MainActivity.this, String.format("This is object %s", draggableObject.Name), Toast.LENGTH_LONG).show();
        }
    }

    private class DragListener implements View.OnDragListener{
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            View dragView = (View) dragEvent.getLocalState();
            int viewID = dragView.getId();
            DraggableObject draggableObject = draggableObjects.get(viewID);

            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i("DragAction", String.format("ACTION_DRAG_STARTED: %s for %s \n", view.getTag(), draggableObject.Name));
                    //do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("DragAction",String.format("ACTION_DRAG_ENTERED: %s for %s \n", view.getTag(), draggableObject.Name));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("DragAction",String.format("ACTION_DRAG_EXITED: %s for %s \n", view.getTag(), draggableObject.Name));
                    //do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("DragAction",String.format("ACTION_DROP: %s for %s \n", view.getTag(), draggableObject.Name));
                    LinearLayout oldParent = (LinearLayout) dragView.getParent();
                    LinearLayout newParent = (LinearLayout)view;

                    dragView.setVisibility(View.VISIBLE);
                    if(newParent.getChildCount() < 1 || newParent == sourceLayout){
                        oldParent.removeView(dragView);
                        newParent.addView(dragView);
                    }
                    else {
                        //Do something with the 2 items
                        if(newParent != oldParent){
                            Toast.makeText(MainActivity.this, "Only 1 item is allowed", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("DragAction",String.format("ACTION_DRAG_ENDED: %s for %s \n", view.getTag(), draggableObject.Name));
                default:
                    break;
            }
            return true;
        }
    }
}
