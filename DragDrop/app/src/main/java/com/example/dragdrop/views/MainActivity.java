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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    DraggableObject[] draggableObjects;
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

        area1.setOnDragListener(myOnDragListener);
        area2.setOnDragListener(myOnDragListener);
    }

    View.OnLongClickListener myOnLongClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            //v.setVisibility(View.INVISIBLE);
            return true;
        }

    };

    View.OnDragListener myOnDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View dragView = (View) event.getLocalState();
            DraggableObject draggableObject = draggableObjects[dragView.getId()];

            String area;
            if(v == area1){
                area = "area1";
            }else if(v == area2){
                area = "area2";
            }else{
                area = "unknown";
            }

            switch (event.getAction()) {
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
                    View view = (View)event.getLocalState();
                    LinearLayout oldParent = (LinearLayout)view.getParent();
                    oldParent.removeView(view);
                    LinearLayout newParent = (LinearLayout)v;
                    newParent.addView(view);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    prompt.append(String.format("ACTION_DRAG_ENDED: %s for %s \n", area, draggableObject.Name));
                default:
                    break;
            }
            return true;
        }

    };

    private void setupExampleObjects() {
        draggableObjects = new DraggableObject[5];
        draggableObjects[0] = new DraggableObject(0, "Alert", getAndroidDrawable(android.R.drawable.ic_dialog_alert));
        draggableObjects[1] = new DraggableObject(1, "Dialer", getAndroidDrawable(android.R.drawable.ic_dialog_dialer));
        draggableObjects[2] = new DraggableObject(2, "Email", getAndroidDrawable(android.R.drawable.ic_dialog_email));
        draggableObjects[3] = new DraggableObject(3, "Info", getAndroidDrawable(android.R.drawable.ic_dialog_info));
        draggableObjects[4] = new DraggableObject(4, "Map", getAndroidDrawable(android.R.drawable.ic_dialog_map));

        for (DraggableObject obj : draggableObjects) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(obj.Drawable);
            imageView.setId(obj.getID());
            imageView.setOnLongClickListener(myOnLongClickListener);
            area1.addView(imageView);
        }
    }

    private Drawable getAndroidDrawable(int id){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id);
        }

        return getDrawable(id);
    }
}
