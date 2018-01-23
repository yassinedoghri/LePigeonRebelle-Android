package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.models.Debt;

import java.util.List;

public class ExpenseDebtsAdapter extends ArrayAdapter<Debt> {

    // View lookup cache
    private static class ViewHolder {
        ImageButton remove;
        ImageView avatar;
        TextView name;
        EditText amount;
    }

    public ExpenseDebtsAdapter(Context context, List<Debt> debts) {
        super(context, R.layout.list_item_expense_debt, debts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Debt debt = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_expense_debt, parent, false);
            viewHolder.remove = (ImageButton) convertView.findViewById(R.id.button_remove_debt);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar_member);
            viewHolder.name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.amount = (EditText) convertView.findViewById(R.id.edit_amount);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        // Check if user is default user
        String memberName = debt.getUserOwing().toString();
        viewHolder.name.setText(memberName);

        int color = getContext().getColor(R.color.colorAccent);
        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(memberName.charAt(0)).toUpperCase(), color);
        viewHolder.avatar.setImageDrawable(drawable);

        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(debt);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
