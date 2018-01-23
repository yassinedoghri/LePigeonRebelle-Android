package com.lepigeonrebelle;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lepigeonrebelle.models.Group;


public class GroupExpensesFragment extends Fragment {

    ListView expensesList;
    Group currentGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.group_expenses_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());

        // get list of expenses for current group
        currentGroup = ((MyApplication) getActivity().getApplication()).getCurrentGroup();
        ExpensesAdapter adapter = new ExpensesAdapter(getActivity(), databaseAccess.getGroupExpenses(currentGroup));

        expensesList = getView().findViewById(R.id.list_group_expenses);
        expensesList.setAdapter(adapter);

    }
}
