package com.lepigeonrebelle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lepigeonrebelle.models.Group;

public class GroupActivity extends AppCompatActivity {

    BottomNavigationView navigation;

    private Group currentGroup;
    private FloatingActionButton newGroupBtn;
    private FloatingActionButton newExpenseBtn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_group_expenses:
                    switchToFragment(new GroupExpensesFragment());
                    return true;
                case R.id.navigation_group_debts:
                    switchToFragment(new GroupDebtsFragment());
                    return true;
            }
            return false;
        }
    };

    public void switchToFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_group, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        // set currentGroup globally
        ((MyApplication) this.getApplication()).setCurrentGroup(databaseAccess.getGroupById(getIntent().getIntExtra("groupId", -1)));

        // check if currentGroup is not null
        currentGroup = ((MyApplication) this.getApplication()).getCurrentGroup();
        if (currentGroup != null) {
            setTitle(currentGroup.getName());
        } else {
            finish();
        }

        newGroupBtn = (FloatingActionButton) findViewById(R.id.button_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, GroupFormActivity.class);
                startActivity(intent);
            }
        });

        newExpenseBtn = (FloatingActionButton) findViewById(R.id.button_new_expense);
        newExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start expense form activity
            }
        });
    }

}
