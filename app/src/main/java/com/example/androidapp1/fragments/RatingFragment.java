package com.example.androidapp1.fragments;

import static com.example.androidapp1.activities.HomeActivity.ifTester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.androidapp1.R;
import com.example.androidapp1.adapters.PlayerRatingAdapter;
import com.example.androidapp1.service.UsersFirebaseManager;
import com.example.androidapp1.models.User;
import com.example.androidapp1.models.UserData;

import java.util.Comparator;
import java.util.List;

public class RatingFragment extends Fragment {

    private List<UserData> usersData; // Здесь вы будете загружать ваш список пользователей
    private final List<User> users;
    public static int currentRatingType = 1;
    ListView listView;

    Spinner ratingCriteriaSpinner;
    PlayerRatingAdapter adapter;
    ImageButton toHome;

    public RatingFragment(List<UserData> usersData, List<User> users){
        this.usersData = usersData;
        this.users = users;
        applyRoles();
    }
    public void applyRoles(){
        UsersFirebaseManager usersFirebaseManager = new UsersFirebaseManager();
        for (UserData userData: this.usersData){
            usersFirebaseManager.getUserByID(userData.getId()).thenAccept(user -> {
                if (!ifTester(user))
                    usersData.remove(userData);
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        init_all(view);
        return view;
    }

    private void sortDataByLvl() {
        usersData.sort(new Comparator<UserData>() {
            @Override
            public int compare(UserData user1, UserData user2) {
                return Integer.compare(user2.getLvl(), user1.getLvl()); // Убывающий порядок
            }
        });
        adapter.notifyDataSetChanged(); // Обновление адаптера после сортировки
    }

    private void sortDataByTotalCharacterLevel() {
        usersData.sort(new Comparator<UserData>() {
            @Override
            public int compare(UserData user1, UserData user2) {
                return Integer.compare(user2.totalCharacterLevel(), user1.totalCharacterLevel());
            }
        });
        adapter.notifyDataSetChanged(); // Обновление адаптера после сортировки
    }



    private void init_all(View view) {
        toHome = view.findViewById(R.id.ratingToHome);
        listView = view.findViewById(R.id.ratingListView);
        ratingCriteriaSpinner = view.findViewById(R.id.ratingCriteriaSpinner);
        ratingCriteriaSpinner.setSelection(0, false);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.rating_criteria, R.layout.spinner_item);
        spinner_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ratingCriteriaSpinner.setAdapter(spinner_adapter);


        adapter = new PlayerRatingAdapter(getContext(), R.layout.list_item_player_rating, usersData, users);
        listView.setAdapter(adapter);

        // Переносим сортировку выше, до установки слушателя
        //sortDataByLvl();
        //ratingCriteriaSpinner.setSelection(0, false); // Устанавливаем выбранный элемент без активации слушателя

        ratingCriteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Здесь можно ничего не делать, так как это пустой элемент
                        break;
                    case 1:
                        currentRatingType = 1;
                        sortDataByLvl();
                        break;
                    case 2:
                        currentRatingType = 2;
                        sortDataByTotalCharacterLevel();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FragmentInteractionListener) {
                    ((FragmentInteractionListener) getActivity()).onFragmentClosed();
                }
            }
        });
    }



}