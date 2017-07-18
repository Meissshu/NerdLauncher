package com.meishu.android.nerdlauncher.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meishu.android.nerdlauncher.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Meishu on 18.07.2017.
 */

public class NerdLauncherFragment extends Fragment {
    public static final String TAG = "NerdLauncherFragment";

    private RecyclerView recyclerView;

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    private void setupAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        o1.loadLabel(pm).toString(),
                        o2.loadLabel(pm).toString()
                );
            }
        });
        recyclerView.setAdapter(new ActivityAdapter(activities));
        Log.i(TAG, "Found " + activities.size() + " activities");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.fragment_NL_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ResolveInfo resolveInfo;
        private TextView nameTextView;

        public ActivityHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView;
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
            String name = resolveInfo.loadLabel(getActivity().getPackageManager()).toString();
            nameTextView.setText(name);
            nameTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.packageName, activityInfo.name);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> resolveInfoList;

        public ActivityAdapter(List<ResolveInfo> list) {
            resolveInfoList = list;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ActivityHolder(v);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            holder.bindActivity(resolveInfoList.get(position));
        }

        @Override
        public int getItemCount() {
            return resolveInfoList.size();
        }
    }
}
