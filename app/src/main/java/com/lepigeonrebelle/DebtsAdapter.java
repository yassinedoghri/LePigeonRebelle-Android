package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.models.Debt;
import com.lepigeonrebelle.models.Expense;
import com.lepigeonrebelle.models.User;

import java.text.DecimalFormat;
import java.util.List;

public class DebtsAdapter extends ArrayAdapter<Debt> {

    // View lookup cache
    private static class ViewHolder {
        ImageView avatarOwing;
        ImageView avatarOwed;
        TextView amountOwed;
        TextView owingName;
        TextView owedName;
    }

    public DebtsAdapter(Context context, List<Debt> expenses) {
        super(context, R.layout.list_item_debt, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Debt debt = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_debt, parent, false);
            viewHolder.avatarOwing = (ImageView) convertView.findViewById(R.id.image_avatar_owing);
            viewHolder.avatarOwed = (ImageView) convertView.findViewById(R.id.image_avatar_owed);
            viewHolder.amountOwed = (TextView) convertView.findViewById(R.id.amount_owed);
            viewHolder.owedName = (TextView) convertView.findViewById(R.id.owed_name);
            viewHolder.owingName = (TextView) convertView.findViewById(R.id.owing_name);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());

        User userPayed = databaseAccess.getUserById(debt.getUserPaying().getId());
        User userOwing = databaseAccess.getUserById(debt.getUserOwing().getId());

        int color = getContext().getColor(R.color.colorAccent);
        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(userPayed.getName().charAt(0)).toUpperCase(), color);
        viewHolder.avatarOwed.setImageDrawable(drawable);

        int color2 = getContext().getColor(R.color.colorPrimary);
        TextDrawable drawable2 = TextDrawable.builder().buildRound(String.valueOf(userOwing.getName().charAt(0)).toUpperCase(), color2);
        viewHolder.avatarOwing.setImageDrawable(drawable2);

        DecimalFormat df = new DecimalFormat("##.##");
        viewHolder.amountOwed.setText(df.format(debt.getAmount()) + "€");

        viewHolder.owedName.setText(userPayed.getName());
        viewHolder.owingName.setText(userOwing.getName());

        // Return the completed view to render on screen
        return convertView;
    }
}
