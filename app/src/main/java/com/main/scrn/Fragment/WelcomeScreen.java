package com.main.scrn.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Adapter.SliderPagerAdapter;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import purplefront.com.kriddrpetparent.R;

/**
 * Created by pf-05 on 1/30/2018.
 */

public class WelcomeScreen extends Fragment {

    View rootView;
    private ViewPager vp_slider;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<Map<Integer, Object>> slider_image_list;
    private TextView header_firsttitle, header_secondtitle;
    Button signup_button;
    android.support.v7.app.ActionBar mActionBar;
    private RelativeLayout signin_layout;
    GenFragmentCall_Main genFragmentCall_mainObj;
    ActionBarUtil actionBarUtilObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.frag_welcome, container, false);

        signin_layout = rootView.findViewById(R.id.signin_layout);


        signin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genFragmentCall_mainObj.Fragment_call(null,new Login(), "SgnInFrag", null);

            }
        });

        actionBarUtilObj.SetActionBarHide();
        init();
        addBottomDots(0);

        return rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallInterface) {
            FragmentCallInterface callInterface = (FragmentCallInterface) context;
            genFragmentCall_mainObj = callInterface.Get_GenFragCallMainObj();
        }
        if(context instanceof InterfaceActionBarUtil){
            actionBarUtilObj=((InterfaceActionBarUtil)context).getActionBarUtilObj();

        }
    }

    private void init() {
        vp_slider = (ViewPager) rootView.findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) rootView.findViewById(R.id.layoutDots);
        header_firsttitle = (TextView) rootView.findViewById(R.id.header_firsttitle);
        header_secondtitle = (TextView) rootView.findViewById(R.id.header_secondtitle);


        slider_image_list = new ArrayList<>();

        Map<Integer, Object> myList = new HashMap<>();
        myList.put(0, R.drawable.intro1);
        myList.put(1, "Store");
        myList.put(2, "Ask your service providers to put it in Kriddr!");
        slider_image_list.add(myList);
        myList = new HashMap<>();
        myList.put(0, R.drawable.intro2);
        myList.put(1, "Share");
        myList.put(2, "Because every app has sharing...");
        slider_image_list.add(myList);
        myList = new HashMap<>();
        myList.put(0, R.drawable.intro3);
        myList.put(1, "Record");
        myList.put(2, "Help us decode dog barks!");
        slider_image_list.add(myList);


        sliderPagerAdapter = new SliderPagerAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        signup_button = (Button) rootView.findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                genFragmentCall_mainObj.Fragment_call(null,new SignUp(), "SgnUpFrag", null);
            }
        });
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        int[] colorsActive = getResources().getIntArray(R.array.orange_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.orange_dot_inactive);

        Map<Integer, Object> myList = slider_image_list.get(currentPage);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            header_firsttitle.setText(String.valueOf(myList.get(1)));
            header_secondtitle.setText(String.valueOf(myList.get(2)));

            dots[i].setTextSize(50);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }
}
