package com.example.canva.ui.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.example.canva.R;

import java.util.Random;

public class Tools {

    private static int countDots = 12;
    private static int withDot = 60;
    private static int heightDot = 60;

    public static void addDots(int posX, int posY, int povX, int povY, Context context, RelativeLayout parentRelative) {

        ///add parent bar
        RelativeLayout parent = new RelativeLayout(context);
        RelativeLayout.LayoutParams paramsParent = new RelativeLayout.LayoutParams(povX - posX, povY - posY);

        parent.setX(posX);
        parent.setY(posY);
        parent.setLayoutParams(paramsParent);

      //  parent.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        /////
        for (int i = 1; i <= countDots; i++) {
            int posXDot = randomPosX(posX, povX - (withDot / 2), countDots);
            int posYDot = randomPosX(posY, povY - (heightDot / 2), countDots);
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

    private static int randomPosX(int start, int end, int countItems) {
        Random rn = new Random();
        //Generate random int value from left to right
        System.out.println(" Random value in int from " + start + " to " + end + ":");
        int widget = end - start;
        int widgetPart = widget / countItems;

        int startPart = (widgetPart * (rn.nextInt(countItems) + 1));
        System.out.println(" Random startPart " + startPart);
        int endPart = startPart + widgetPart;


        int randomNum = rn.nextInt((endPart - startPart) + 1) + startPart;

        System.out.println(" Random value  " + randomNum);
        return randomNum;
    }

    private static int random(int start, int end, int countItems, int step, String type) {
        //Generate random int value from left to right
        System.out.println(" Random value in int from " + start + " to " + end + ":");
        float widget = end - start;
        float widgetPart = widget / countItems;

        float startPart = widgetPart * step;
        System.out.println(" Random startPart " + startPart);
        float endPart = startPart + widgetPart;

        int random_int = (int) Math.floor(Math.random() * (endPart - startPart + 3) + startPart);
        System.out.println(" Random value  " + random_int);
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
