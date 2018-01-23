package com.lepigeonrebelle;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.ExpenseCategory;
import com.lepigeonrebelle.models.ExpenseType;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.User;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.angmarch.views.NiceSpinner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseFormActivity extends AppCompatActivity implements Validator.ValidationListener {


    private EditText groupNameEdit;

    @NotEmpty
    private EditText dateEdit;
    private ImageButton expenseCategoryBtn;
    private Switch isExpenseTodo;


    @NotEmpty
    private EditText expenseDesc;

    @NotEmpty
    private EditText amountEdit;

    @NotEmpty
    private NiceSpinner payedBySpinner;

    @NotEmpty
    private NiceSpinner typeSpinner;

    private Button addFriendBtn;
    private ListView debtsLV;

    private List<User> groupMembers;

    private Group currentGroup;
    private List<ExpenseCategory> expenseCategories;
    private List<ExpenseType> expenseTypes;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private User userPaying;

    private Validator validator;
    private Calendar myCalendar = Calendar.getInstance();

    private MaterialSimpleListAdapter simpleMembersAdapter;

    private List<Debt> debts = new ArrayList<>();
    private ExpenseDebtsAdapter expenseDebtsAdapter;
    private MaterialDialog addDebtModal;

    // simpleListItems
    List<Integer> removedMemberItems = new ArrayList<>();
    List<MaterialSimpleListItem> memberItems = new ArrayList<>();

    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form);

        databaseAccess = DatabaseAccess.getInstance(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        expenseDesc = (EditText) findViewById(R.id.expense_description);
        groupNameEdit = (EditText) findViewById(R.id.edit_group_name);
        dateEdit = (EditText) findViewById(R.id.edit_expense_date);

        isExpenseTodo = (Switch) findViewById(R.id.is_expense_todo);

        // set expenseType by default to Equally
        expenseType = databaseAccess.getExpenseTypes().get(0);

        // check if currentGroup is not null and set it as default
        currentGroup = ((MyApplication) this.getApplication()).getCurrentGroup();
        if (currentGroup != null) {
            groupNameEdit.setText(currentGroup.getName());
            groupMembers = ((MyApplication) this.getApplication()).getCurrentGroupUsers();
        }

        // set default date to today
        myCalendar.set(Calendar.HOUR_OF_DAY, 0);
        updateDate();

        expenseCategoryBtn = (ImageButton) findViewById(R.id.button_expense_category);
        expenseCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseCategoryModal();
            }
        });

        expenseCategories = databaseAccess.getExpenseCategories();

        // set default expense category + icon
        expenseCategory = Helper.getExpenseCategoryById(expenseCategories, 7);
        expenseCategoryBtn.setImageResource(Helper.getDrawableId(this, expenseCategory.getIcon()));

        Button addMemberBtn = (Button) findViewById(R.id.button_add_friend);
        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddMemberModal();
            }
        });

        // add default user by default to payingMembers
        User defaultUser = ((MyApplication) this.getApplication()).getDefaultUser();
        Debt defaultUserDebt = new Debt();
        defaultUserDebt.setUserOwing(defaultUser);
        defaultUserDebt.setAmount(0.0);
        removedMemberItems.add(defaultUserDebt.getUserOwing().getId());
        debts.add(defaultUserDebt);

        // set default user
        userPaying = defaultUser;

        // Show date picker when clicking on date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ExpenseFormActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        payedBySpinner = (NiceSpinner) findViewById(R.id.spinner_payed_by);
        final PayerAdapter payerAdapter = new PayerAdapter(this, groupMembers);
        payedBySpinner.setAdapter(payerAdapter);
        payedBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                userPaying = payerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        expenseTypes = databaseAccess.getExpenseTypes();
        final ExpenseTypeAdapter expenseTypeAdapter = new ExpenseTypeAdapter(this, expenseTypes);
        typeSpinner = (NiceSpinner) findViewById(R.id.spinner_expense_type);
        typeSpinner.setAdapter(expenseTypeAdapter);
        typeSpinner.setSelectedIndex(0);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                expenseType = expenseTypeAdapter.getItem(position);
                updateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });


        expenseDebtsAdapter = new ExpenseDebtsAdapter(this, debts);

        debtsLV = (ListView) findViewById(R.id.debts_list);
        debtsLV.setAdapter(expenseDebtsAdapter);
        debtsLV.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateAmount();
                debtsLV.removeOnLayoutChangeListener(this);
            }
        });


        amountEdit = (EditText) findViewById(R.id.edit_expense_amount);
        amountEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                // set amount according to type
                updateAmount();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void updateDate() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        dateEdit.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expense_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_expense:
                // Save group to database and go to GroupActivity
                validator.validate();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        saveExpense();
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

    public void showExpenseCategoryModal() {

        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                // change groupType
                expenseCategory = Helper.getExpenseCategoryById(expenseCategories, (int) item.getId());
                expenseCategoryBtn.setImageResource(Helper.getDrawableId(dialog.getContext(), expenseCategory.getIcon()));

                // close dialog
                dialog.dismiss();
            }
        });

        for (ExpenseCategory expenseCategory : expenseCategories) {
            adapter.add(new MaterialSimpleListItem.Builder(this)
                    .id(expenseCategory.getId())
                    .content(expenseCategory.getWording())
                    .icon(Helper.getDrawableId(this.getBaseContext(), expenseCategory.getIcon()))
                    .backgroundColor(Color.WHITE)
                    .build());
        }

        new MaterialDialog.Builder(this)
                .title(R.string.title_expense_type_modal)
                .adapter(adapter, null)
                .show();
    }

    public void showAddMemberModal() {
        simpleMembersAdapter = new MaterialSimpleListAdapter(new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                // Check if friend is not already in debts
                User selectedFriend = databaseAccess.getUserById((int) item.getId());
                if (!Helper.isFriendAlreadyOwing(debts, selectedFriend)) {
                    // create new debt with selected user
                    Debt debt = new Debt();
                    debt.setUserOwing(selectedFriend);
                    debts.add(debt);

                    // remove item from list
                    removedMemberItems.add((int) simpleMembersAdapter.getItem(index).getId());
                    simpleMembersAdapter.clear();

                    for (MaterialSimpleListItem memberItem : memberItems) {
                        simpleMembersAdapter.add(memberItem);
                    }

                    // notify change
                    expenseDebtsAdapter.notifyDataSetChanged();

                    setListViewHeightBasedOnChildren(debtsLV);
                } else {
                    // show toast: Already a member!
                    String message = "Already in list!";
                    Toast.makeText(dialog.getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (memberItems.size() != groupMembers.size()) {
            for (User user : groupMembers) {
                // generate textDrawable
                int color = this.getColor(R.color.colorAccent);
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(String.valueOf(user.getName().charAt(0)).toUpperCase(), color);

                memberItems.add(new MaterialSimpleListItem.Builder(this)
                        .id(user.getId())
                        .content(user.toString())
                        .icon(drawable)
                        .backgroundColor(Color.WHITE)
                        .build());
            }
        }
        for (final MaterialSimpleListItem memberItem : memberItems) {
            simpleMembersAdapter.add(memberItem);
        }

        addDebtModal = new MaterialDialog.Builder(this)
                .title(R.string.choose_friend)
                .adapter(simpleMembersAdapter, null)
                .show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void updateAmount() {
        // you can call or do what you want with your EditText here
        View v;
        EditText et;
        ImageButton ib;
        TextView tv;
        String amountVal = amountEdit.getText().toString();
        double amount;
        double diff;
        double firstAmount = 0.00;
        double splitRound;
        double split;
        String splitRoundStr;
        DecimalFormat df = new DecimalFormat("##.##");
        if (!TextUtils.isEmpty(amountVal)) {
            amount = Double.valueOf(amountVal); // eg. 33.33
            split = amount / debtsLV.getCount(); // 16.665 (/2)
            splitRound = Double.parseDouble(df.format(split)); // 16.66
            diff = amount - (splitRound * debtsLV.getCount()); // 33.33 - 33.32 = 0.01
            if (diff > 0) {
                firstAmount = splitRound + diff;
            } else {
                firstAmount = splitRound;
            }
        } else {
            splitRound = 0.00;
        }
        for (int i = 0; i < debtsLV.getCount(); i++) {
            // set more only for first
            if (i == 0) {
                splitRoundStr = df.format(firstAmount);
            } else {
                splitRoundStr = df.format(splitRound);
            }
            v = debtsLV.getChildAt(i);
            et = (EditText) v.findViewById(R.id.edit_amount);
            ib = (ImageButton) v.findViewById(R.id.button_currency_amount);
            tv = (TextView) v.findViewById(R.id.text_shares);
            switch (expenseType.getId()) {
                case 1: // Equally
                    ib.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    et.setHint("0.00");
                    et.setText(splitRoundStr);
                    et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    et.setFocusable(false);
                    break;
                case 2: // Unequally
                    ib.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    et.setHint("0.00");
                    et.setText(splitRoundStr);
                    et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    et.setFocusableInTouchMode(true);
                    break;
                case 3: // by Shares
                    ib.setVisibility(View.GONE);
                    tv.setVisibility(View.VISIBLE);
                    et.setHint("0");
                    et.setText("1");
                    et.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et.setFocusableInTouchMode(true);
                    break;
                case 4: // Refund
                    ib.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    et.setHint("0.00");
                    et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    et.setText(splitRoundStr);
                    et.setFocusableInTouchMode(true);
                    break;
            }
        }
    }

    public Boolean saveExpense() {
        double amountVal = Double.parseDouble(amountEdit.getText().toString());

        // check if all debts have been set properly
        View v;
        EditText et;
        double totalAmount = 0.0;
        if (expenseType.getId() != 3) {
            for (int i = 0; i < debtsLV.getCount(); i++) {
                v = debtsLV.getChildAt(i);
                et = (EditText) v.findViewById(R.id.edit_amount);
                String amountStr = et.getText().toString();
                if (TextUtils.isEmpty(amountStr)) {
                    // amount is required, stop method
                    Toast.makeText(this, "You must set an amount for each friend!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                double amt = Double.parseDouble(amountStr);
                totalAmount += amt;
                debts.get(i).setAmount(amt);
                debts.get(i).setUserPaying(userPaying);
            }
            // TODO: check for shares as well
            if (amountVal != totalAmount) {
                // the amounts don't match
                Toast.makeText(this, "The amounts don't match!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else { // split by shares
            int shareNum = 0;
            for (int i = 0; i < debtsLV.getCount(); i++) {
                v = debtsLV.getChildAt(i);
                et = (EditText) v.findViewById(R.id.edit_amount);
                String amountStr = et.getText().toString();
                if (TextUtils.isEmpty(amountStr)) {
                    // amount is required, stop method
                    Toast.makeText(this, "You must set shares for each friend!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                int share = Integer.parseInt(amountStr);
                shareNum += share;
            }
            double shareCost = amountVal / shareNum;
            for (int i = 0; i < debtsLV.getCount(); i++) {
                v = debtsLV.getChildAt(i);
                et = (EditText) v.findViewById(R.id.edit_amount);
                String amountStr = et.getText().toString();
                if (TextUtils.isEmpty(amountStr)) {
                    // amount is required, stop method
                    Toast.makeText(this, "You must set shares for each friend!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                int numOfShares = Integer.parseInt(amountStr);
                debts.get(i).setAmount(shareCost * numOfShares);
                debts.get(i).setUserPaying(userPaying);
            }
        }


        // create Expense object
        String expenseDescription = expenseDesc.getText().toString();

        Expense expense = new Expense();
        expense.setCategory(expenseCategory);
        expense.setDate(myCalendar.getTime());
        expense.setTodo(isExpenseTodo.isChecked() ? 1 : 0);
        expense.setGroup(currentGroup);
        expense.setType(expenseType);
        expense.setDescription(expenseDescription);
        expense.setDebts(debts);

        Expense newExpense = databaseAccess.createExpense(expense);

        if (newExpense != null) {
            Toast.makeText(this, "Yay! Expense created!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ExpenseFormActivity.this, GroupActivity.class);
            intent.putExtra("groupId", expense.getGroup().getId());
            startActivity(intent);
            finish(); // destroy Activity
        }

        return true;
    }

}
