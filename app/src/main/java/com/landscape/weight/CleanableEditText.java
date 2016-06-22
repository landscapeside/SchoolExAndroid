package com.landscape.weight;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.landscape.schoolexandroid.R;
import com.utils.image.ImageHelper;
import com.utils.system.ScreenParam;

import java.lang.reflect.Field;

public class CleanableEditText extends RelativeLayout {
    EditText editContent;
    DrawRightTextView tvDes;
    Context mContext;

    int drawLeftResId = 0, drawRightResId = 0, textColorResId = 0;
    int inputType = EditorInfo.TYPE_CLASS_TEXT;
    int maxLength = -1, maxEms = -1;
    String hint = "";
    int drawPadding = 24;
    ColorStateList textColorHint, textColor;
    int textSize = -1;
    int filters = 0;

    private Drawable mLeftDrawable, mRightDrawable;
    private boolean ignoreFilter = false;
    private boolean isHasFocus;

    public CleanableEditText(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_clean_edit, this, true);
        init();
//        hideSoftKeyBord();
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_clean_edit, this, true);
        initParam(context, attrs);
        init();
//        hideSoftKeyBord();
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_clean_edit, this, true);
        initParam(context, attrs);
        init();
//        hideSoftKeyBord();
    }

    private void initParam(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CleanEdit);
        drawLeftResId = a.getResourceId(R.styleable.CleanEdit_drawLeft, 0);
        drawRightResId = a.getResourceId(R.styleable.CleanEdit_drawRight, 0);
        textColorResId = a.getResourceId(R.styleable.CleanEdit_textColor, 0);
        ignoreFilter = a.getBoolean(R.styleable.CleanEdit_ignoreFilter, false);
        filters = a.getInt(R.styleable.CleanEdit_inputFilter, 0);
        a.recycle();

        Resources.Theme theme = context.getTheme();
        TypedArray defType = null;
        try {
            // TypedArray
            Class<?> styleableCls = Class.forName("com.android.internal.R$styleable");
            Object styleableInstance = styleableCls.newInstance();
            Field field = styleableCls.getField("TextView");
            int[] styleableValues = (int[]) field.get(styleableInstance);
            Class<?> styleableAttrCls = Class.forName("com.android.internal.R$attr");
            Field defStyleAttrField = styleableAttrCls.getField("textViewStyle");
            Object styleAttrInstance = styleableAttrCls.newInstance();
            int defStyleAttrValue = (int) defStyleAttrField.get(styleAttrInstance);
            defType = theme.obtainStyledAttributes(attrs, styleableValues, defStyleAttrValue, 0);
            // inputType
            Field inputField = styleableCls.getField("TextView_inputType");
            int inputValue = (int) inputField.get(styleableCls);
            inputType = defType.getInt(inputValue, EditorInfo.TYPE_CLASS_TEXT);
            // hint
            Field hintField = styleableCls.getField("TextView_hint");
            int hintValue = (int) hintField.get(styleableCls);
            hint = defType.getString(hintValue);
            // maxLength
            Field maxLengthField = styleableCls.getField("TextView_maxLength");
            int maxLengthValue = (int) maxLengthField.get(styleableCls);
            maxLength = defType.getInt(maxLengthValue, -1);
            // maxEms
            Field maxEmsField = styleableCls.getField("TextView_maxEms");
            int maxEmsValue = (int) maxEmsField.get(styleableCls);
            maxEms = defType.getInt(maxEmsValue, -1);
            // drawPadding
            Field drawPaddingField = styleableCls.getField("TextView_drawablePadding");
            int drawablePaddingValue = (int) drawPaddingField.get(styleableCls);
            drawPadding = defType.getDimensionPixelSize(drawablePaddingValue, 24);
            // hintTextColor
            Field hintTextColorField = styleableCls.getField("TextView_textColorHint");
            int hintTextColorValue = (int) hintTextColorField.get(styleableCls);
            textColorHint = defType.getColorStateList(hintTextColorValue);
            // textColor
            Field textColorField = styleableCls.getField("TextView_textColor");
            int textColorValue = (int) textColorField.get(styleableCls);
            textColor = defType.getColorStateList(textColorValue);
            // textSize
            Field textSizeField = styleableCls.getField("TextView_textSize");
            int textSizeValue = (int) textSizeField.get(styleableCls);
            textSize = defType.getDimensionPixelSize(textSizeValue, -1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        defType.recycle();
    }

    private void init() {
        editContent = (EditText) findViewById(R.id.edit_content);
        tvDes = (DrawRightTextView) findViewById(R.id.tv_descirption);
        if (drawLeftResId != 0) {
            mLeftDrawable = mContext.getResources().getDrawable(drawLeftResId);
            mLeftDrawable = ImageHelper.scaleDrawable(
                    getResources(), mLeftDrawable,
                    ((float) ScreenParam.dp2px(mContext, 30f)) / (float) mLeftDrawable.getMinimumHeight());
            mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
            editContent.setCompoundDrawables(mLeftDrawable, null, null, null);
            editContent.setCompoundDrawablePadding(drawPadding);
        }
        if (drawRightResId != 0) {
            mRightDrawable = mContext.getResources().getDrawable(drawRightResId);
            mRightDrawable = ImageHelper.scaleDrawable(
                    getResources(), mRightDrawable,
                    ((float) ScreenParam.dp2px(mContext, 30f)) / (float) mRightDrawable.getMinimumHeight());
            mRightDrawable.setBounds(0, 0, mRightDrawable.getMinimumWidth(), mRightDrawable.getMinimumHeight());
            tvDes.setCompoundDrawables(null, null, mRightDrawable, null);
            tvDes.setCompoundDrawablePadding(10);
        }
        editContent.setInputType(inputType);
        editContent.setHint(hint);
        if (textColorHint != null) {
            editContent.setHintTextColor(textColorHint);
        }
        if (textColor != null) {
            editContent.setTextColor(textColor);
            tvDes.setTextColor(textColor);
        }
        if (!ignoreFilter) {
            if (maxEms != -1) {
                editContent.setMaxEms(maxEms);
            } else if (maxLength != -1) {
                EditInputFilter.addFilter(editContent,new InputFilter.LengthFilter(maxLength));
            }
            setFiler(filters);
        }
        /**
         * 默认设置14sp
         */
        if (textSize == -1) {
            textSize = 14;
            textSize = ScreenParam.sp2px(mContext, textSize);
        }
        if (textSize != -1) {
            editContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tvDes.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        editContent.setOnFocusChangeListener(new FocusChangeListenerImpl());
        editContent.addTextChangedListener(new TextWatcherImpl());
        tvDes.setOnRightClickListener(() -> editContent.setText(""));
        setClearDrawableVisible(false);
    }

    private void setFiler(int filters) {
        if (filters == 0) {
            return;
        }
        EditInputFilter.addFilter(editContent,EditInputFilter.getFilter(filters));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setClearDrawableVisible(true);
        } else {
            setClearDrawableVisible(false);
        }
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {
                boolean isVisible = !TextUtils.isEmpty(editContent.getText().toString().trim());
                setClearDrawableVisible(isVisible);
            } else {
                setClearDrawableVisible(false);
            }
            if (onEditFocusChangeListener != null) {
                onEditFocusChangeListener.onFocusChange(isHasFocus);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tvDes.dispatchTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (isHasFocus) {
                boolean isVisible = !TextUtils.isEmpty(editContent.getText().toString().trim());
                setClearDrawableVisible(isVisible);
                if (dynamicCheckListener != null) {
                    dynamicCheckListener.onInputChanged(s.toString());
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

    protected void setClearDrawableVisible(boolean isVisible) {
        if (isEnabled()) {
            if (isVisible) {
                tvDes.setVisibility(VISIBLE);
            } else {
                tvDes.setVisibility(GONE);
            }
        } else {
            tvDes.setVisibility(GONE);
        }
    }

    public void setPrompt(String promptMsg) {
        if (TextUtils.isEmpty(promptMsg)) {
            tvDes.setText("");
        } else {
            tvDes.setText(promptMsg);
            tvDes.setShakeAnimation();
        }
    }

    public void setText(CharSequence text) {
        editContent.setText(text);
        editContent.setSelection(editContent.getText().toString().length());
    }

    public Editable getText() {
        return editContent.getText();
    }

    public void setIgnoreFilter(boolean ignoreFilter) {
        this.ignoreFilter = ignoreFilter;
        if (ignoreFilter) {
            editContent.setFilters(new InputFilter[]{});
        }
    }

    public void setInputType(int inputType) {
        if ((inputType ^ EditorInfo.TYPE_CLASS_TEXT) == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
            editContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editContent.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        } else if ((inputType ^ EditorInfo.TYPE_CLASS_TEXT) == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editContent.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        } else if ((inputType ^ EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS) == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) {
//            editContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editContent.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        } else if ((inputType ^ EditorInfo.TYPE_TEXT_FLAG_CAP_CHARACTERS) == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
//            editContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editContent.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editContent.setInputType(inputType);
        }
        editContent.setSelection(editContent.getText().toString().length());
    }

    public int getInputType() {
        return editContent.getInputType();
    }

    public void setMaxLength(int maxLength) {
        if (!ignoreFilter && maxLength != -1) {
            EditInputFilter.addFilter(editContent,new InputFilter.LengthFilter(maxLength));
        }
    }

    public void setHint(CharSequence hint) {
        editContent.setHint(hint);
    }

    public CharSequence getHint() {
        return editContent.getHint();
    }

    public void setDrawLeft(int drawLeftResId) {
        this.drawLeftResId = drawLeftResId;
        if (drawLeftResId != 0) {
            mLeftDrawable = mContext.getResources().getDrawable(drawLeftResId);
            mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
            editContent.setCompoundDrawables(mLeftDrawable, null, null, null);
            editContent.setCompoundDrawablePadding(drawPadding);
        }
    }

    public void hideSoftKeyBord() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0); //强制隐藏键盘
    }

    private DynamicCheckListener dynamicCheckListener = null;

    public void setDynamicCheckListener(DynamicCheckListener dynamicCheckListener) {
        this.dynamicCheckListener = dynamicCheckListener;
    }

    public interface DynamicCheckListener {
        void onInputChanged(String text);
    }

    private OnEditFocusChangeListener onEditFocusChangeListener = null;

    public void setOnEditFocusChangeListener(OnEditFocusChangeListener onEditFocusChangeListener) {
        this.onEditFocusChangeListener = onEditFocusChangeListener;
    }

    public interface OnEditFocusChangeListener {
        void onFocusChange(boolean hasFocus);
    }
}