package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.User;
import com.lepigeonrebelle.models.UserGroup;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class ExpensesAdapter extends ArrayAdapter<Expense> {

    // View lookup cache
    private static class ViewHolder {
        ImageView expenseIcon;
        TextView description;
        TextView info;
        TextView owingLabel;
        TextView amount;
        TextView todo;
    }

    public ExpensesAdapter(Context context, List<Expense> expenses) {
        super(context, R.layout.list_item_expense, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Expense expense = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_expense, parent, false);
            viewHolder.expenseIcon = (ImageView) convertView.findViewById(R.id.image_expense_icon);
            viewHolder.description = (TextView) convertView.findViewById(R.id.text_expense_desc);
            viewHolder.info = (TextView) convertView.findViewById(R.id.text_exprense_info);
            viewHolder.owingLabel = (TextView) convertView.findViewById(R.id.text_owing_label);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.text_owing);
            viewHolder.todo = (TextView) convertView.findViewById(R.id.text_todo);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.expenseIcon.setImageResource(Helper.getDrawableId(getContext(), expense.getCategory().getIcon()));
        viewHolder.description.setText(expense.getDescription());

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
        List<Debt> debts = databaseAccess.getExpenseDebts(expense);

        User defaultUser = ((MyApplication) convertView.getContext().getApplicationContext()).getDefaultUser();

        User userPaying = new User();
        userPaying.setName("Nobody");
        userPaying.setId(-1);
        if (debts.size() > 0) {
            userPaying = databaseAccess.getUserById(debts.get(0).getUserPaying().getId());
        }
        double amountOwing = 0.0;
        double amountOwed = 0.0;
        double totalAmount = 0.0;
        for (Debt debt : debts) {
            if (defaultUser.getId() == debt.getUserOwing().getId()) {
                amountOwing += debt.getAmount();
            } else {
                amountOwed += debt.getAmount();
            }
            totalAmount += debt.getAmount();
        }

        String infoText;
        String owingText;
        double amount;
        DecimalFormat df = new DecimalFormat("##.##");
        if (userPaying.getId() == defaultUser.getId()) {
            infoText = "You paid " + df.format(totalAmount) + "€";
            owingText = "You are owed";
            amount = amountOwed;
        } else {
            infoText = userPaying.getName() + " paid " + df.format(totalAmount) + "€";
            owingText = "You owe";
            amount = amountOwing;
        }
        viewHolder.info.setText(infoText);

        viewHolder.owingLabel.setText(owingText);
        viewHolder.amount.setText(df.format(amount) + "€");

        if (expense.isTodo() == 1) {
            viewHolder.todo.setVisibility(View.VISIBLE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
