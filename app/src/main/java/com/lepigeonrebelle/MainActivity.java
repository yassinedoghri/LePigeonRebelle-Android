package com.lepigeonrebelle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    private FloatingActionButton newGroupBtn;
    private FloatingActionButton newExpenseBtn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    switchToFragment(new FriendsFragment());
                    return true;
                case R.id.navigation_groups:
                    switchToFragment(new GroupsFragment());
                    return true;
                case R.id.navigation_debts:
                    switchToFragment(new DebtsFragment());
                    return true;
            }
            return false;
        }
    };

    public void switchToFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if default user is set
        initDefaultUser();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        newGroupBtn = (FloatingActionButton) findViewById(R.id.button_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDefaultUser() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        // set defaultUser
        ((MyApplication) this.getApplication()).setDefaultUser(databaseAccess.getDefaultUser());

        if (((MyApplication) this.getApplication()).getDefaultUser() == null) {
            // redirect to splash screen
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
        // default user has already been set,  continue.
    }

}
