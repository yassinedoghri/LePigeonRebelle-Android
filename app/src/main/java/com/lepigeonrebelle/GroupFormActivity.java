package com.lepigeonrebelle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.model.Group;
import com.lepigeonrebelle.model.GroupType;
import com.lepigeonrebelle.model.User;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class GroupFormActivity extends AppCompatActivity implements Validator.ValidationListener {

    private List<GroupType> groupTypes;
    private GroupType groupType;
    private List<User> groupMembers = new ArrayList<>();
    private List<User> users;
    private Validator validator;

    @NotEmpty
    private EditText groupNameEdit;

    private ListView groupMembersLv;
    private ImageButton groupTypeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);

        validator = new Validator(this);
        validator.setValidationListener(this);

        groupNameEdit = (EditText) findViewById(R.id.edit_group_name);

        groupTypeBtn = (ImageButton) findViewById(R.id.button_group_type);
        groupTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGroupTypeModal();
            }
        });

        Button addMemberBtn = (Button) findViewById(R.id.button_add_member);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMemberModal();
            }
        });

        // Populate GroupTypes
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        this.groupTypes = databaseAccess.getGroupTypes();
        this.users = databaseAccess.getUsers();
        databaseAccess.close();

        // set default group type
        this.groupType = Helper.getGroupTypeById(this.groupTypes, 5);
        groupTypeBtn.setImageResource(Helper.getDrawableId(this.getBaseContext(), this.groupType.getIcon()));

        // add default user by default in groupMembers
        this.groupMembers.add(((MyApplication) this.getApplication()).getDefaultUser());

        MembersAdapter adapter = new MembersAdapter(this, this.groupMembers);

        groupMembersLv = (ListView) findViewById(R.id.group_members_list);
        groupMembersLv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_group:
                // Save group to database and go to GroupActivity
                validator.validate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        saveGroup();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showGroupTypeModal() {

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                // change groupType
                groupType = Helper.getGroupTypeById(groupTypes, (int) item.getId());
                groupTypeBtn.setImageResource(Helper.getDrawableId(dialog.getContext(), groupType.getIcon()));

                // close dialog
                dialog.dismiss();
            }
        });

        for (GroupType groupType : this.groupTypes) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .id(groupType.getId())
                    .content(groupType.getWording())
                    .icon(Helper.getDrawableId(this.getBaseContext(), groupType.getIcon()))
                    .backgroundColor(Color.WHITE)
                    .build());
        }

        new MaterialDialog.Builder(this)
                .title(R.string.title_group_type_modal)
                .adapter(adapter, null)
                .show();
    }

    public void showAddMemberModal() {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                if (item.getId() == -1) {
                    // Add new friend
                    // add form to list requesting friend's name
                } else {
                    // add friend to list
                }
            }
        });

        for (User user : this.users) {
            // generate textDrawable
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(user.getName().charAt(0)).toUpperCase(), Color.RED);
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .content(user.getName())
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
        }

        adapter.add(new MaterialSimpleListItem.Builder(this)
                .id(-1)
                .content(R.string.new_member)
                .icon(R.drawable.ic_heroicons_plus_circle)
                .iconPaddingDp(8)
                .build());

        new MaterialDialog.Builder(this)
                .title(R.string.choose_friend)
                .adapter(adapter, null)
                .show();
    }

    public void saveGroup() {
        // build Group Object
        String groupName = groupNameEdit.getText().toString();
        Group group = new Group(-1, groupName, groupType, groupMembers);

        // save to database
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        long newGroupId = databaseAccess.createGroup(group);
        databaseAccess.close();

        // redirect to group activity if success
        if (newGroupId != -1) {
            Intent intent = new Intent(GroupFormActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // destroy Activity
        }
    }
}
