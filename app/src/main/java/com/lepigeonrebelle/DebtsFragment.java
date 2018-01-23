package com.lepigeonrebelle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lepigeonrebelle.models.Group;


public class DebtsFragment extends Fragment {

    ListView debtsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.debts_view, container, false);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        DebtsAdapter adapter = new DebtsAdapter(getActivity(), databaseAccess.getDebts());

        debtsList = rootView.findViewById(R.id.list_debts);
        debtsList.setAdapter(adapter);

        return rootView;
    }

}