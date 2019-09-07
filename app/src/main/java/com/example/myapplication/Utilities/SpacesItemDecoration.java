package com.example.myapplication.Utilities;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private final float radius;
    private final RectF defaultRectToClip;
    public SpacesItemDecoration(int space,float radius) {
        this.space = space;
        this.radius = radius;
        defaultRectToClip = new RectF(Float.MAX_VALUE, Float.MAX_VALUE, 0, 0);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = space;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final RectF rectToClip = getRectToClip(parent);

        // has no items with ViewType == `R.layout.item_image`
        if (rectToClip.equals(defaultRectToClip)) {
            return;
        }

        final Path path = new Path();
        path.addRoundRect(rectToClip, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
    }
    private RectF getRectToClip(RecyclerView parent) {
        final RectF rectToClip = new RectF(defaultRectToClip);
        final Rect childRect = new Rect();
        for (int i = 0; i < parent.getChildCount(); i++) {


            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, childRect);

            rectToClip.left = Math.min(rectToClip.left, childRect.left);
            rectToClip.top = Math.min(rectToClip.top, childRect.top);
            rectToClip.right = Math.max(rectToClip.right, childRect.right);
            rectToClip.bottom = Math.max(rectToClip.bottom, childRect.bottom);
        }
        return rectToClip;
    }
}
