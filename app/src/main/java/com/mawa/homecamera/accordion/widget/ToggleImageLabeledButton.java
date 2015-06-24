/**
 * Copyright (c) 2011, 2012 Sentaca Communications Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.mawa.homecamera.accordion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.mawa.homecamera.R;

import java.util.concurrent.atomic.AtomicBoolean;

public final class ToggleImageLabeledButton extends ImageView {

    private int imageOn;
    private int imageOff;
    private AtomicBoolean on = new AtomicBoolean(false);

    public ToggleImageLabeledButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.image_labeled_button, 0, 0);
            imageOn = a.getResourceId(R.styleable.image_labeled_button_icon_resource, 0);
            a.recycle();

            a = context.obtainStyledAttributes(attrs, R.styleable.toggle_image_labeled_button, 0, 0);
            imageOff = a.getResourceId(R.styleable.toggle_image_labeled_button_icon_resource_off, 0);
            setImageResource(imageOff);
            a.recycle();
        }

    }

    private void handleNewState(boolean newState) {
        if (newState) {
            setImageResource(imageOn);
        } else {
            setImageResource(imageOff);
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        OnClickListener wrappingListener = new OnClickListener() {

            public void onClick(View v) {
                boolean newState = !on.get();
                on.set(newState);
                handleNewState(newState);
                l.onClick(v);
            }

        };

        super.setOnClickListener(wrappingListener);
    }

    public void setState(boolean b) {
        on.set(b);
        handleNewState(b);
    }

}