package org.supla.android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MenuItemsLayout extends LinearLayout implements View.OnClickListener {

    private LinearLayout mMainButtonsAreaLayout = null;
    private boolean mInitialized = false;
    private int availableButtons = 0;
    private OnClickListener mOnClickListener;
    ArrayList<Button> buttons = new ArrayList<>();

    public static final int BTN_SETTINGS = 0x1;
    public static final int BTN_ADD_DEVICE = 0x2;
    public static final int BTN_ABOUT = 0x4;
    public static final int BTN_DONATE = 0x8;
    public static final int BTN_HELP = 0x10;
    public static final int BTN_HOMEPAGE = 0x20;
    public static final int BTN_FREE_SPACE = 0x40;
    public static final int BTN_Z_WAVE = 0x80;
    public static final int BTN_ALL = 0xFFFF;

    private void initialize() {
        if (!mInitialized) {
            mInitialized = true;
            setVisibility(GONE);
            setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            setOrientation(VERTICAL);

            mMainButtonsAreaLayout = new LinearLayout(getContext());
            mMainButtonsAreaLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            mMainButtonsAreaLayout.setOrientation(VERTICAL);
            mMainButtonsAreaLayout.setBackgroundColor(getResources().getColor(R.color.menubar));
            addView(mMainButtonsAreaLayout);
        }
    }

    public MenuItemsLayout(Context context) {
        super(context);
        initialize();
    }

    public MenuItemsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MenuItemsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuItemsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void addLongSeparator() {
        View view = new View(getContext());
        view.setBackgroundColor(getResources().getColor(R.color.menuseparator));
        view.setAlpha(0.4f);
        mMainButtonsAreaLayout.addView(view);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.menuitem_separator_height));
        view.setLayoutParams(params);
    }

    private void addShortSeparator() {

        LinearLayout ll = new LinearLayout(getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        ll.setOrientation(HORIZONTAL);
        mMainButtonsAreaLayout.addView(ll);

        View view = new View(getContext());
        view.setBackgroundColor(Color.TRANSPARENT);
        ll.addView(view);

        int margin = getResources().getDimensionPixelSize(R.dimen.menuitem_iamge_margin);
        int width = getResources().getDimensionPixelSize(R.dimen.menuitem_height);
        int separator_height =
                getResources().getDimensionPixelSize(R.dimen.menuitem_separator_height);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                margin * 2 + width, separator_height
                );
        view.setLayoutParams(params);

        view = new View(getContext());
        view.setBackgroundColor(getResources().getColor(R.color.menuseparator));
        view.setAlpha(0.4f);
        ll.addView(view);

        params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, separator_height
        );
        view.setLayoutParams(params);

    }

    private void addButton(int id, @DrawableRes int iconResId, @StringRes int textResId) {

        if ((availableButtons & id) == 0) {
            return;
        }

        if (buttons.size() > 0) {
            addShortSeparator();
        } else {
            addLongSeparator();
        }

        LinearLayout ll = new LinearLayout(getContext());
        ll.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        ll.setOrientation(HORIZONTAL);
        mMainButtonsAreaLayout.addView(ll);

        ImageView iv = new ImageView(getContext());
        iv.setClickable(true);
        iv.setTag(id);
        iv.setOnClickListener(this);
        ll.addView(iv);

        int height = getResources().getDimensionPixelSize(R.dimen.menuitem_height);
        int margin = getResources().getDimensionPixelSize(R.dimen.menuitem_iamge_margin);
        int padding = getResources().getDimensionPixelSize(R.dimen.menuitem_padding);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                height, height);
        params.setMargins(margin,0,margin,0);

        iv.setLayoutParams(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            iv.setBackground(getResources().getDrawable(iconResId));
        } else {
            iv.setBackgroundDrawable(getResources().getDrawable(iconResId));
        }

        Button btn = new Button(getContext(), null, R.attr.borderlessButtonStyle);
        btn.setOnClickListener(this);
        Typeface type = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/OpenSans-Regular.ttf");
        btn.setTypeface(type);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.menuitem_text_size));
        btn.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
        btn.setTextColor(Color.WHITE);
        btn.setPadding(0, 0, 0, padding);
        btn.setTransformationMethod(null);
        btn.setText(getResources().getString(textResId).toUpperCase());
        btn.setTag(id);
        buttons.add(btn);
        ll.addView(btn);

        params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.menuitem_height));
        btn.setLayoutParams(params);

    }

    private void addFooter() {

        Button btn;
        LayoutParams params;

        if ((availableButtons & BTN_HOMEPAGE) != 0) {
            addLongSeparator();

            btn = new Button(getContext(), null, R.attr.borderlessButtonStyle);
            btn.setTag(BTN_HOMEPAGE);
            btn.setText(getResources().getString(R.string.homepage).toUpperCase());
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.menuitem_homepage_text_size));
            btn.setTextColor(Color.WHITE);
            btn.setOnClickListener(this);
            Typeface type = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/OpenSans-Bold.ttf");
            btn.setTypeface(type);
            mMainButtonsAreaLayout.addView(btn);

            params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.menuitem_height));
            btn.setLayoutParams(params);
            btn.setTransformationMethod(null);
        }

        if ((availableButtons & BTN_FREE_SPACE) != 0) {
            btn = new Button(getContext(), null, R.attr.borderlessButtonStyle);
            btn.setTag(BTN_FREE_SPACE);
            btn.setOnClickListener(this);
            addView(btn);

            params = new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            btn.setLayoutParams(params);
            btn.setTransformationMethod(null);
        }

    }

    public void setButtonsAvailable(int available) {

        if (availableButtons == available) {
            return;
        }

        availableButtons = available;
        mMainButtonsAreaLayout.removeAllViews();
        buttons.clear();

        addButton(BTN_SETTINGS, R.drawable.settings, R.string.settings);
        addButton(BTN_ADD_DEVICE, R.drawable.add_device, R.string.add_device);
        addButton(BTN_Z_WAVE, R.drawable.z_wave, R.string.z_wave);
        addButton(BTN_ABOUT, R.drawable.info, R.string.about);
        addButton(BTN_DONATE, R.drawable.donate, R.string.donate_title);
        addButton(BTN_HELP, R.drawable.help, R.string.help);

        addFooter();

    }

    public int getBtnAreaHeight() {
        return mMainButtonsAreaLayout == null ? getHeight() : mMainButtonsAreaLayout.getHeight();
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    public static int getButtonId(View v) {
        if (v != null && v.getTag() instanceof Integer) {
            return ((Integer) v.getTag()).intValue();
        }
        return 0;
    }
}
