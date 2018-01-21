package com.lepigeonrebelle;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class GroupsFragment extends Fragment {

    ListView groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.groups_view, container, false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        final GroupsAdapter adapter = new GroupsAdapter(getActivity(), databaseAccess.getGroups());

        groupList = rootView.findViewById(R.id.list_groups);
        groupList.setAdapter(adapter);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int groupId = adapter.getItem(position).getId();
                Intent i = new Intent(getActivity(), GroupActivity.class);
                i.putExtra("groupId", groupId);
                startActivity(i);
            }
        });

        return rootView;
    }
}
