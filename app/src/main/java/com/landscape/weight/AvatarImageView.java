package com.landscape.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by 1 on 2016/7/4.
 */
public class AvatarImageView extends ImageView {
    public AvatarImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTag(int key, Object tag) {
        super.setTag(key, tag);
        if (tagChangeListener != null) {
            tagChangeListener.tagChanged(key,tag);
        }
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        if (tagChangeListener != null) {
            tagChangeListener.tagChanged(tag);
        }
    }

    public interface TAGChangeListener{
        void tagChanged(Object tag);
        void tagChanged(int key, Object tag);
    }

    TAGChangeListener tagChangeListener = null;

    public void setTAGChangeListener(TAGChangeListener tagChangeListener) {
        this.tagChangeListener = tagChangeListener;
    }
}
