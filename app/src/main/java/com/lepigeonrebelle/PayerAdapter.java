package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lepigeonrebelle.models.User;

import java.util.List;

public class PayerAdapter extends ArrayAdapter<User> {

    // View lookup cache
    private static class ViewHolder {
        TextView name;
    }

    public PayerAdapter(Context context, List<User> expenseTypes) {
        super(context, R.layout.list_item_payer, expenseTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User payer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_payer, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.payer_name);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(payer.getName());

        // Return the completed view to render on screen
        return convertView;
    }
}
