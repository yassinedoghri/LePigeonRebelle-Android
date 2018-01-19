package com.lepigeonrebelle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    mTextMessage.setText(R.string.title_friends);
                    return true;
                case R.id.navigation_groups:
                    mTextMessage.setText(R.string.title_groups);
                    return true;
                case R.id.navigation_debts:
                    mTextMessage.setText(R.string.title_debts);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if default user is set
        initDefaultUser();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextMessage.setText("Welcome, " + ((MyApplication) this.getApplication()).getDefaultUser().getName());

        final FloatingActionButton removeAction = (FloatingActionButton) findViewById(R.id.button_new_group);
        removeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GroupFormActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDefaultUser() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        // set defaultUser
        ((MyApplication) this.getApplication()).setDefaultUser(databaseAccess.getDefaultUser());
        databaseAccess.close();

        if (((MyApplication) this.getApplication()).getDefaultUser().getId() == -1) {
            // redirect to splash screen
            Intent intent = new Intent(MainActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
        // default user has already been set,  continue.
    }

}
