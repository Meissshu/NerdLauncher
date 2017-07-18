package com.meishu.android.nerdlauncher.activities;

import android.support.v4.app.Fragment;

import com.meishu.android.nerdlauncher.fragments.NerdLauncherFragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
