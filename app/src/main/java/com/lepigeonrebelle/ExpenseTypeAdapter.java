package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lepigeonrebelle.models.ExpenseType;

import java.util.List;

public class ExpenseTypeAdapter extends ArrayAdapter<ExpenseType> {

    // View lookup cache
    private static class ViewHolder {
        TextView wording;
    }

    public ExpenseTypeAdapter(Context context, List<ExpenseType> expenseTypes) {
        super(context, R.layout.list_item_expense_type, expenseTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ExpenseType expenseType = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_expense_type, parent, false);
            viewHolder.wording = (TextView) convertView.findViewById(R.id.expense_type_wording);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.wording.setText(expenseType.getWording());
        
        // Return the completed view to render on screen
        return convertView;
    }
}
