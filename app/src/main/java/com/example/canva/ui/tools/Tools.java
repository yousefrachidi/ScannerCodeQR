package com.example.canva.ui.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.canva.R;

public class Tools {

    private static int countDots = 6;
    private static int withDot = 60;
    private static int heightDot = 60;

    public static void addDots(int posX, int povX, int posY, int povY, Context context, RelativeLayout parentRelative) {

        ///add parent bar
        RelativeLayout parent = new RelativeLayout(context);
        RelativeLayout.LayoutParams paramsParent = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        parent.setX(posX);
        parent.setY(posY);
        parent.setLayoutParams(paramsParent);

        /////
        for (int i = 1; i <= countDots; i++) {
            int posXDot = randomPosX(posX, povX - (withDot / 2), countDots, i,"x");
            int posYDot = randomPosX(posY, povY - (heightDot / 2), countDots, i,"y");
            View viewDot = LayoutInflater.from(context).inflate(R.layout.circle_layout, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(withDot, heightDot);
            viewDot.setLayoutParams(params);

            viewDot.setX(posXDot);
            viewDot.setPivotX(posXDot);
            viewDot.setY(posYDot);
            viewDot.setPivotY(posYDot);
            //animation
            View btn = viewDot.findViewById(R.id.circlebutton);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animate_zoom);
            btn.startAnimation(animation);
            parent.addView(viewDot);
        }
        parentRelative.addView(parent);
    }

    private static int randomPosX(int start, int end, int countItems, int step,String type) {
        //Generate random int value from left to right
        System.out.println(type+" Random value in int from " + start + " to " + end + ":");
        float widget = end - start;
        float widgetPart = widget / countItems;

        float startPart = widgetPart * step;
        System.out.println(type+" Random startPart " +startPart  );
        float endPart = startPart + widgetPart;

        int random_int = (int) Math.floor(Math.random() * (endPart - startPart + 3) + startPart);
        System.out.println(type+" Random value  " + random_int);
        return random_int;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void addLine(int with, int height, int posX, int posY, Context context, RelativeLayout parentRelative) {

        ///add parent bar
        RelativeLayout parent = new RelativeLayout(context);
        RelativeLayout.LayoutParams paramsParent = new RelativeLayout.LayoutParams(with, height);

        parent.setX(posX);
        parent.setY(posY);
        // parent.setPivotX(height);
        // parent.setPivotY(height);
        parent.setLayoutParams(paramsParent);

        ///add bar
        View view = LayoutInflater.from(context).inflate(R.layout.line, null);
        RelativeLayout.LayoutParams paramsRelativeLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 50
        );

        // background
        view.setBackground(context.getDrawable(R.drawable.ic_line));
        //animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scal_animation);
        view.startAnimation(animation);

        //
        parent.addView(view, paramsRelativeLayout);
        parentRelative.addView(parent);
    }

}
