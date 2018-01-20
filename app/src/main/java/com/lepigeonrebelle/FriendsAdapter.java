package com.lepigeonrebelle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.lepigeonrebelle.models.User;

import java.util.List;

public class FriendsAdapter extends ArrayAdapter<User> {

    // View lookup cache
    private static class ViewHolder {
        ImageView avatar;
        TextView name;
    }

    public FriendsAdapter(Context context, List<User> friends) {
        super(context, R.layout.list_item_group, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User friend = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_friend, parent, false);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.image_friend_avatar);
            viewHolder.name = (TextView) convertView.findViewById(R.id.friend_name);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        int color = getContext().getColor(R.color.colorAccent);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(friend.getName().charAt(0)).toUpperCase(), color);
        viewHolder.avatar.setImageDrawable(drawable);
        viewHolder.name.setText(friend.getName());

        // Return the completed view to render on screen
        return convertView;

    }
}
