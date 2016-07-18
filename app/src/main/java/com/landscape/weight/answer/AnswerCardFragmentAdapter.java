package com.landscape.weight.answer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.utils.datahelper.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2016/7/18.
 */
public class AnswerCardFragmentAdapter extends PagerAdapter {

    FragmentManager fm;
    private FragmentTransaction mCurTransaction = null;

    List<Fragment> cards = new ArrayList<>();
    List<Boolean> refreshFlags = new ArrayList<>();


    public AnswerCardFragmentAdapter(FragmentManager fm, List<Fragment> cards) {
        this.fm = fm;
        this.cards.addAll(cards);
        for (Fragment f : cards) {
            refreshFlags.add(false);
        }
    }

    public void resetData(List<Fragment> cards) {
        if (!CollectionUtils.isEmpty(this.cards)) {
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.cards){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        refreshFlags.clear();
        this.cards.clear();
        this.cards.addAll(cards);
        for (Fragment f : cards) {
            refreshFlags.add(true);
        }
        notifyDataSetChanged();
    }

    public Fragment getItem(int position) {
        return cards.get(position);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment)super.instantiateItem(container, position);
//        String fragmentTag = fragment.getTag();
//        if (refreshFlags.get(position)) {
//            FragmentTransaction ft =fm.beginTransaction();
//            ft.remove(fragment);
//            ft.commit();
//            ft =fm.beginTransaction();
//            fragment = cards.get(position);
//            ft.add(container.getId(), fragment, fragmentTag);
//            ft.attach(fragment);
//            ft.commit();
//            refreshFlags.set(position, false);
//        }


        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }

        Fragment fragment = getItem(position);
//        if (mSavedState.size() > position) {
//            Fragment.SavedState fss = mSavedState.get(position);
//            if (fss != null) {
//                fragment.setInitialSavedState(fss);
//            }
//        }
        if (fragment != null) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
            if (!fragment.isAdded()) {
                mCurTransaction.add(container.getId(), fragment);
//                mCurTransaction.commit();
            }
        }

        return fragment;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            fm.executePendingTransactions();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }
        mCurTransaction.remove(fragment);
//        mCurTransaction.commit();
    }
}
