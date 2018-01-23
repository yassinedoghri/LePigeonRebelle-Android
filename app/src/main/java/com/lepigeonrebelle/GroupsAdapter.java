package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lepigeonrebelle.models.Group;
import com.lepigeonrebelle.models.GroupType;
import com.lepigeonrebelle.models.UserGroup;

import java.text.DecimalFormat;
import java.util.List;

public class GroupsAdapter extends ArrayAdapter<Group> {

    // View lookup cache
    private static class ViewHolder {
        ImageView groupIcon;
        TextView name;
        TextView budget;
    }

    public GroupsAdapter(Context context, List<Group> groups) {
        super(context, R.layout.list_item_group, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Group group = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_group, parent, false);
            viewHolder.groupIcon = (ImageView) convertView.findViewById(R.id.image_group_type_icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.group_name);
            viewHolder.budget = (TextView) convertView.findViewById(R.id.group_budget);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(group.getName());
        viewHolder.groupIcon.setImageResource(Helper.getDrawableId(getContext(), group.getType().getIcon()));

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());

        // Show group budget
        List<UserGroup> groupMembers = databaseAccess.getGroupMembers(group);
        Double budget = 0.0;
        for (UserGroup member : groupMembers) {
            budget += member.getBudget();
        }
        DecimalFormat df = new DecimalFormat("##.##");

        viewHolder.budget.setText(df.format(budget) + "€");

        // Return the completed view to render on screen
        return convertView;
    }
}
