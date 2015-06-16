package com.adhi.TabUI;


import com.adhi.controlcentre.R;

import android.content.*;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class TabsContent extends ViewFlipper
{

    ViewFlipper VF;

    public TabsContent(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        VF = (ViewFlipper)findViewById(R.id.tabbutton);
        BroadcastReceiver tab1 = new BroadcastReceiver() {
            public void onReceive(Context context1, Intent intent)
            {
                VF.setDisplayedChild(0);
            }
        }
                ;
        BroadcastReceiver tab2 = new BroadcastReceiver() {
            public void onReceive(Context context1, Intent intent)
            {
                VF.setDisplayedChild(1);
            }
        }
                ;
        BroadcastReceiver tab3 = new BroadcastReceiver() {

            public void onReceive(Context context1, Intent intent)
            {
                VF.setDisplayedChild(2);
            }

        }
                ;
        context.registerReceiver(tab1, new IntentFilter("pineappleTabUI1"));
        context.registerReceiver(tab2, new IntentFilter("pineappleTabUI2"));
        context.registerReceiver(tab3, new IntentFilter("pineappleTabUI3"));
    }
}
