package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.UserGroup;

import java.util.List;

public class ExpensesAdapter extends ArrayAdapter<Expense> {

    // View lookup cache
    private static class ViewHolder {
        ImageView expenseIcon;
        TextView description;
        TextView info;
        TextView owingLabel;
        TextView amount;
    }

    public ExpensesAdapter(Context context, List<Expense> expenses) {
        super(context, R.layout.list_item_group, expenses);
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
            convertView = inflater.inflate(R.layout.list_item_group, parent, false);
            viewHolder.expenseIcon = (ImageView) convertView.findViewById(R.id.image_group_type_icon);
            viewHolder.description = (TextView) convertView.findViewById(R.id.text_expense_desc);
            viewHolder.info = (TextView) convertView.findViewById(R.id.text_exprense_info);
            viewHolder.owingLabel = (TextView) convertView.findViewById(R.id.text_owing_label);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.text_owing);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.description.setText(expense.getDescription());
        viewHolder.expenseIcon.setImageResource(Helper.getDrawableId(getContext(), expense.getCategory().getIcon()));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());

        // Return the completed view to render on screen
        return convertView;
    }
}
