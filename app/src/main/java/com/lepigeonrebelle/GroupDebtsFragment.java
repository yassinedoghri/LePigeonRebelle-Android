package com.lepigeonrebelle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lepigeonrebelle.models.Group;


public class GroupDebtsFragment extends Fragment {


    ListView debtsList;
    Group currentGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.group_debts_view, container, false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        // get list of expenses for current group
        currentGroup = ((MyApplication) getActivity().getApplication()).getCurrentGroup();
        DebtsAdapter adapter = new DebtsAdapter(getActivity(), databaseAccess.getGroupDebts(currentGroup));

        debtsList = getView().findViewById(R.id.list_group_debts);
        debtsList.setAdapter(adapter);

    }
}
