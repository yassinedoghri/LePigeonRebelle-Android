package com.lepigeonrebelle;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FriendsFragment extends Fragment {

    ListView friendList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.friends_view, container, false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        FriendsAdapter adapter = new FriendsAdapter(getActivity(), databaseAccess.getFriends());

        friendList = rootView.findViewById(R.id.list_friends);
        friendList.setAdapter(adapter);

        return rootView;
    }
}