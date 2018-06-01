package purplefront.com.kriddrpetparent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Model.Client_info_Model;
import com.Model.UserModel;
import com.iface.BottomNavigationClicked;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;

import com.navigation.scrn.fragment.ActivityFragment;

import com.navigation.scrn.fragment.OwnFeedFragment;
import com.navigation.scrn.fragment.ViewPublicFeedFragment;
import com.navigation.scrn.fragment.View_Parent_Profile;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;

import java.util.List;


public class KridderNavigationActivity extends AppCompatActivity implements InterfaceUserModel, InterfaceActionBarUtil, FragmentCallInterface {

    private TextView mTextMessage;
    Toolbar toolbar;
    static ActionBarUtil actionBarUtilObj;
    static GenFragmentCall_Main fragmentCall_mainObj;
    static UserModel userModelObj;

    public static String USER_MODEL_TAG = "USER_MODEL_OBJ";
    static BottomNavigationView navigation;

    public static void setNavigaationVisible(boolean isVisible) {
        if (isVisible) {
            navigation.setVisibility(View.VISIBLE);
        } else
            navigation.setVisibility(View.GONE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            for(Fragment fragment:getSupportFragmentManager().getFragments()) {
                if ( fragment instanceof OwnFeedFragment){
                    ((OwnFeedFragment)fragment).setRemoveScrollChangeListener();
                }
                if ( fragment instanceof ViewPublicFeedFragment){
                    ((ViewPublicFeedFragment)fragment).setRemoveScrollChangeListener();
                }
            }
            switch (item.getItemId()) {
                case R.id.navigation_client:
//                    mTextMessage.setText(R.string.title_home);
                    //    actionBarUtilObj.setTitle("Feed");
                    fragmentCall_mainObj.Fragment_call(null,new OwnFeedFragment(), "frag_feed", null);
                    return true;
                case R.id.navigation_invoice:
                    //actionBarUtilObj.setTitle("Activity");
                    //  mTextMessage.setText(R.string.title_dashboard);
                    fragmentCall_mainObj.Fragment_call(null,new ActivityFragment(), "frag_act", null);
                    return true;


                case R.id.navigation_profile:
                    fragmentCall_mainObj.Fragment_call(null,new View_Parent_Profile(), "view_parent", null);
                    //     actionBarUtilObj.setTitle("Profile");
                    //  mTextMessage.setText(R.string.title_dashboard);

                    // fragmentCall_mainObj=new GenFragmentCall_Main(KridderNavigationActivity.this);
                    //fragmentCall_mainObj.Fragment_call(new ProfileFragment(),"profilefrag",null);
                 /*   Fragment test;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    test = new ProfileFragment();
                    fragmentTransaction.replace(R.id.frame_layout,test,"profilefrag");
                    fragmentTransaction.addToBackStack("profilefrag");
                    fragmentTransaction.commit();*/
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kridder_navigation);




        int ScreenFromVal = getIntent().getIntExtra(MainActivity.SCREEN_FROM_TAG, -1);
        userModelObj = getIntent().getParcelableExtra(USER_MODEL_TAG);
        actionBarUtilObj = new ActionBarUtil(this);
        fragmentCall_mainObj = new GenFragmentCall_Main(this);


        actionBarUtilObj.setTitle("Feed");
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //BottomNavigationView bottomNavigationView = (BottomNavigationView) .findViewById(R.id.bottom_navigation);
      /*  ImageView img_mnu_ClientIcon = (ImageView) navigation.findViewById(R.id.navigation_client).findViewById(R.id.icon);
        img_mnu_ClientIcon.setMinimumWidth(100);
        img_mnu_ClientIcon.setMinimumHeight(100);

        ImageView img_mnu_ProIcon = (ImageView) navigation.findViewById(R.id.navigation_profile).findViewById(R.id.icon);
        img_mnu_ProIcon.setMinimumWidth(100);
        img_mnu_ProIcon.setMinimumHeight(100);

        ImageView img_mnu_invIcon = (ImageView) navigation.findViewById(R.id.navigation_invoice).findViewById(R.id.icon);
        img_mnu_invIcon.setMinimumWidth(100);
        img_mnu_invIcon.setMinimumHeight(100);

        Toast.makeText(this, "I am here", Toast.LENGTH_SHORT).show();
*/

/*
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

*/


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_client);
        // navigation.getMenu().getItem(2).setChecked(true);
       // throw null;
    }

    public  void global_state(){
        actionBarUtilObj = new ActionBarUtil(this);
        fragmentCall_mainObj = new GenFragmentCall_Main(this);
        userModelObj = getIntent().getParcelableExtra(USER_MODEL_TAG);
    }

    public void callFragment() {
        fragmentCall_mainObj.Fragment_call(null,new View_Parent_Profile(), "view_parent", null);
    }

    @Override
    public void setUserModel(UserModel userModel) {
        userModelObj = userModel;
    }

    @Override
    public UserModel getUserModel() {
        return userModelObj;
    }

    @Override
    public GenFragmentCall_Main Get_GenFragCallMainObj() {
        return fragmentCall_mainObj;
    }

    @Override
    public ActionBarUtil getActionBarUtilObj() {
        return actionBarUtilObj;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

}
