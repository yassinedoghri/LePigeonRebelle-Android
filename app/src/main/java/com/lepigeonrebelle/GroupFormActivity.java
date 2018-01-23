package com.lepigeonrebelle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.GroupType;
import com.lepigeonrebelle.models.User;
import com.lepigeonrebelle.models.UserGroup;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class GroupFormActivity extends AppCompatActivity implements Validator.ValidationListener {

    private List<GroupType> groupTypes;
    private GroupType groupType;
    private List<UserGroup> groupMembers = new ArrayList<>();
    private List<User> users;
    private Validator validator;

    private MembersAdapter memberAdapter;

    @NotEmpty
    private EditText groupNameEdit;

    private ListView groupMembersLv;
    private ImageButton groupTypeBtn;

    private MaterialDialog addMemberModal;
    private MaterialSimpleListAdapter simpleMembersAdapter;

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
        groupTypes = databaseAccess.getGroupTypes();
        users = databaseAccess.getUsers();

        // set default group type + icon
        groupType = Helper.getGroupTypeById(groupTypes, 5);
        groupTypeBtn.setImageResource(Helper.getDrawableId(this, groupType.getIcon()));

        // add default user by default in groupMembers
        UserGroup defaultUserGroup = new UserGroup();
        defaultUserGroup.setUser(((MyApplication) this.getApplication()).getDefaultUser());
        defaultUserGroup.setBudget(0.0);
        groupMembers.add(defaultUserGroup);

        memberAdapter = new MembersAdapter(this, groupMembers);

        groupMembersLv = (ListView) findViewById(R.id.group_members_list);
        groupMembersLv.setAdapter(memberAdapter);
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
        simpleMembersAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                if (item.getId() == -1) {
                    // Add new friend
                    // add form to list requesting friend's name
                    new MaterialDialog.Builder(dialog.getContext())
                            .title(R.string.title_new_user_modal)
                            .content(R.string.content_new_user_modal)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(R.string.hint_input_new_user, 0, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {

                                }
                            })
                            .positiveText(R.string.positive_new_user_modal)
                            .negativeText(R.string.negative_new_user_modal)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    // Check if input is not empty
                                    String friendName = dialog.getInputEditText().getText().toString();

                                    // create user
                                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                                    User newFriend = databaseAccess.newUser(friendName);

                                    // add the friend to users
                                    int color = dialog.getContext().getColor(R.color.colorAccent);
                                    TextDrawable drawable = TextDrawable.builder()
                                            .buildRound(String.valueOf(newFriend.getName().charAt(0)).toUpperCase(), color);
                                    simpleMembersAdapter.add(new MaterialSimpleListItem.Builder(dialog.getContext())
                                            .id(newFriend.getId())
                                            .content(newFriend.getName())
                                            .icon(drawable)
                                            .backgroundColor(Color.WHITE)
                                            .build());
                                    simpleMembersAdapter.notifyDataSetChanged();
                                }
                            })
                            .show();
                } else {
                    // Check if friend is not already in membersList
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    User selectedFriend = databaseAccess.getUserById((int) item.getId());
                    if (!Helper.isFriendInMembers(groupMembers, selectedFriend)) {
                        // add friend to members
                        UserGroup member = new UserGroup();
                        member.setUser(selectedFriend);
                        groupMembers.add(member);

                        memberAdapter.notifyDataSetChanged();
                    } else {
                        // show toast: Already a member!
                        String message = "Already a member!";
                        Toast.makeText(dialog.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        simpleMembersAdapter.add(new MaterialSimpleListItem.Builder(this)
                .id(-1)
                .content(R.string.new_member)
                .icon(R.drawable.ic_heroicons_plus_circle)
                .iconPaddingDp(8)
                .build());

        for (User user : this.users) {
            // generate textDrawable
            int color = this.getColor(R.color.colorAccent);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(user.getName().charAt(0)).toUpperCase(), color);
            simpleMembersAdapter.add(new MaterialSimpleListItem.Builder(this)
                    .id(user.getId())
                    .content(user.getName())
                    .icon(drawable)
                    .backgroundColor(Color.WHITE)
                    .build());
        }

        addMemberModal = new MaterialDialog.Builder(this)
                .title(R.string.choose_friend)
                .adapter(simpleMembersAdapter, null)
                .show();
    }

    public Boolean saveGroup() {
        // build Group Object
        String groupName = groupNameEdit.getText().toString();

        // set all budgets of the EditText-Fields
        View v;
        EditText et;
        for (int i = 0; i < groupMembersLv.getCount(); i++) {
            v = groupMembersLv.getChildAt(i);
            et = (EditText) v.findViewById(R.id.edit_budget);
            String budgetStr = et.getText().toString();
            if (TextUtils.isEmpty(budgetStr)) {
                // budget is required, stop method
                Toast.makeText(this, "You must set a budget for each member!", Toast.LENGTH_SHORT).show();
                return false;
            }
            groupMembers.get(i).setBudget(Double.parseDouble(budgetStr));
        }

        Group group = new Group();
        group.setName(groupName);
        group.setType(groupType);
        group.setGroupMembers(groupMembers);

        // save to database
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        Group newGroup = databaseAccess.createGroup(group);

        // redirect to group activity if success
        if (newGroup != null) {
            Toast.makeText(this, "Yay! Group created!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GroupFormActivity.this, GroupActivity.class);
            intent.putExtra("groupId", newGroup.getId());
            startActivity(intent);
            finish(); // destroy Activity
        }
        return true;
    }
}
