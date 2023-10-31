package com.example.androidapp1.fragments;

import static com.example.androidapp1.activities.HomeActivity.current_user;
import static com.example.androidapp1.activities.HomeActivity.current_user_data;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.androidapp1.R;

public class ProfileFragment extends Fragment {

    private ImageButton profile_to_home;
    private EditText userDescription;
    private Context mContext;
    TextView profile_nickname, profile_lvl;
    AppCompatButton change_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_screen, container, false);

        mContext = getContext();

        init_all(view);
        setOnClickListeners();
        setUsersData();
        return view;
    }

    private void setUsersData() {
        profile_nickname.setText(current_user.getNickname());
        profile_lvl.setText("Lv. " + current_user_data.getLvl());
    }

    private void init_all(View view){
        userDescription = view.findViewById(R.id.user_description);
        profile_to_home = view.findViewById(R.id.profile_to_home);
        profile_nickname = view.findViewById(R.id.profile_nickname);
        profile_lvl = view.findViewById(R.id.profile_lvl);
        change_name = view.findViewById(R.id.change_name);
    }

    private void setOnClickListeners(){
        profile_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FragmentInteractionListener) {
                    ((FragmentInteractionListener) getActivity()).onFragmentClosed();
                }
            }
        });

        userDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNicknameChangeDialog();
            }
        });
    }

    private void showNicknameChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Change Nickname");

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNickname = input.getText().toString().trim();
                if (!newNickname.isEmpty()) {
                    current_user.changeName(newNickname);
                    profile_nickname.setText(newNickname);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Type your description");

        // Set up the input
        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setText(userDescription.getText());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userDescription.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
