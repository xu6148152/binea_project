/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zepp.www.transitionsample.library.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Static utility methods for Transitions.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public class TransitionUtils {
    private static int MAX_IMAGE_SIZE = (1024 * 1024);

    public static Animator mergeAnimators(Animator animator1, Animator animator2) {
        if (animator1 == null) {
            return animator2;
        } else if (animator2 == null) {
            return animator1;
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animator1, animator2);
            return animatorSet;
        }
    }

    public static Transition mergeTransitions(Transition... transitions) {
        int count = 0;
        int nonNullIndex = -1;
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                count++;
                nonNullIndex = i;
            }
        }

        if (count == 0) {
            return null;
        }

        if (count == 1) {
            return transitions[nonNullIndex];
        }

        TransitionSet transitionSet = new TransitionSet();
        for (int i = 0; i < transitions.length; i++) {
            if (transitions[i] != null) {
                transitionSet.addTransition(transitions[i]);
            }
        }
        return transitionSet;
    }

    /**
     * Creates a View using the bitmap copy of <code>view</code>. If <code>view</code> is large,
     * the copy will use a scaled bitmap of the given view.
     *
     * @param sceneRoot The ViewGroup in which the view copy will be displayed.
     * @param view The view to create a copy of.
     * @param parent The parent of view
     */
    public static View copyViewImage(ViewGroup sceneRoot, View view, View parent) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(-parent.getScrollX(), -parent.getScrollY());
        ViewUtils.transformMatrixToGlobal(view, matrix);
        ViewUtils.transformMatrixToLocal(sceneRoot, matrix);
        RectF bounds = new RectF(0, 0, view.getWidth(), view.getHeight());
        matrix.mapRect(bounds);
        int left = Math.round(bounds.left);
        int top = Math.round(bounds.top);
        int right = Math.round(bounds.right);
        int bottom = Math.round(bounds.bottom);

        ImageView copy = new ImageView(view.getContext());
        copy.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bitmap = createViewBitmap(view, matrix, bounds);
        if (bitmap != null) {
            copy.setImageBitmap(bitmap);
        }
        int widthSpec = View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY);
        copy.measure(widthSpec, heightSpec);
        copy.layout(left, top, right, bottom);
        return copy;
    }

    /**
     * Get a copy of bitmap of given drawable, return null if intrinsic size is zero
     */
    public static Bitmap createDrawableBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        float scale = Math.min(1f, ((float) MAX_IMAGE_SIZE) / (width * height));
        if (drawable instanceof BitmapDrawable && scale == 1f) {
            // return same bitmap if scale down not needed
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int bitmapWidth = (int) (width * scale);
        int bitmapHeight = (int) (height * scale);
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect existingBounds = drawable.getBounds();
        int left = existingBounds.left;
        int top = existingBounds.top;
        int right = existingBounds.right;
        int bottom = existingBounds.bottom;
        drawable.setBounds(0, 0, bitmapWidth, bitmapHeight);
        drawable.draw(canvas);
        drawable.setBounds(left, top, right, bottom);
        return bitmap;
    }

    /**
     * Creates a Bitmap of the given view, using the Matrix matrix to transform to the local
     * coordinates. <code>matrix</code> will be modified during the bitmap creation.
     *
     * <p>If the bitmap is large, it will be scaled uniformly down to at most 1MB size.</p>
     *
     * @param view The view to create a bitmap for.
     * @param matrix The matrix converting the view local coordinates to the coordinates that
     * the bitmap will be displayed in. <code>matrix</code> will be modified before
     * returning.
     * @param bounds The bounds of the bitmap in the destination coordinate system (where the
     * view should be presented. Typically, this is matrix.mapRect(viewBounds);
     * @return A bitmap of the given view or null if bounds has no width or height.
     */
    public static Bitmap createViewBitmap(View view, Matrix matrix, RectF bounds) {
        Bitmap bitmap = null;
        int bitmapWidth = Math.round(bounds.width());
        int bitmapHeight = Math.round(bounds.height());
        if (bitmapWidth > 0 && bitmapHeight > 0) {
            float scale = Math.min(1f, ((float) MAX_IMAGE_SIZE) / (bitmapWidth * bitmapHeight));
            bitmapWidth *= scale;
            bitmapHeight *= scale;
            matrix.postTranslate(-bounds.left, -bounds.top);
            matrix.postScale(scale, scale);
            try {
                bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.concat(matrix);
                view.draw(canvas);
            } catch (OutOfMemoryError e) {
                // ignore
            }
        }
        return bitmap;
    }
}
