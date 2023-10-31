package com.example.androidapp1.adapters;

import static com.example.androidapp1.fragments.RatingFragment.currentRatingType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.androidapp1.R;
import com.example.androidapp1.models.User;
import com.example.androidapp1.models.UserData;

import java.util.List;
import java.util.Objects;

public class PlayerRatingAdapter extends ArrayAdapter<UserData> {
    private Context context;
    private List<UserData> usersData;
    private List<User> users;


    public PlayerRatingAdapter(Context context, int resource, List<UserData> objects, List<User> users) {
        super(context, resource, objects);
        this.context = context;
        this.usersData = objects;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_player_rating, parent, false);
        }

        TextView playerName = convertView.findViewById(R.id.playerName);
        TextView playerLevel = convertView.findViewById(R.id.playerLevel);

        UserData userData = usersData.get(position);
        User user = null;
        for (User el : users){
            if (Objects.equals(el.getId(), userData.getId()))
            {
                user= el;
                break;
            }
        }


        System.out.println("position = " + position + " user = " + user);
        System.out.println("position = " + position + " userdata = " + userData);

        System.out.println(users);
        System.out.println(usersData);

        playerName.setText(user.getNickname());
        if (currentRatingType == 1)
            playerLevel.setText(String.valueOf(userData.getLvl()));
        if (currentRatingType == 2)
            playerLevel.setText(String.valueOf(userData.totalCharacterLevel()));

        return convertView;
    }
}
