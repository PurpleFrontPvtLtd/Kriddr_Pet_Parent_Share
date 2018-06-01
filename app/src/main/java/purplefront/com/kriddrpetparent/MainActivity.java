package purplefront.com.kriddrpetparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.Model.UserModel;
import com.iface.FragmentCallInterface;
import com.iface.InterfaceActionBarUtil;
import com.iface.InterfaceUserModel;
import com.main.scrn.Fragment.Login;
import com.main.scrn.Fragment.WelcomeScreen;
import com.util.ActionBarUtil;
import com.util.GenFragmentCall_Main;

public class MainActivity extends AppCompatActivity implements FragmentCallInterface,InterfaceActionBarUtil{


    public static enum Screens {

        SPLASH_SCREEN,LOGIN_SCRN

    }
    int Screen_From_Val;
    GenFragmentCall_Main genFragmentCall_main;
    ActionBarUtil actionBarUtilObj;



    public static String SCREEN_FROM_TAG = "screen_from";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genFragmentCall_main = new GenFragmentCall_Main(this);
        actionBarUtilObj=new ActionBarUtil(this);
        actionBarUtilObj.SetActionBarHide();
            genFragmentCall_main.Fragment_call(null,new WelcomeScreen(), "MainFrag", null);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        final int frag_count = getSupportFragmentManager().getBackStackEntryCount();
        // Toast.makeText(MainActivity.this,"Count "+frag_count,Toast.LENGTH_SHORT).show();
        Log.d("FRAGCOUNT", "FRAGCOUNT" + frag_count);


        if (frag_count == 0) {
            finish();
        }
    }

    @Override
    public GenFragmentCall_Main Get_GenFragCallMainObj() {
        return genFragmentCall_main;
    }

    @Override
    public ActionBarUtil getActionBarUtilObj() {
        return actionBarUtilObj;
    }
}
