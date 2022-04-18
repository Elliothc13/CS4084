package com.example.myapplication;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ArrayList<Item> exampleList = new ArrayList<>();
        exampleList.add(new Item(R.drawable.ic_baseline_account_circle_24, "Line1", "Unfollowed", ""));
        exampleList.add(new Item(R.drawable.ic_baseline_account_circle_24, "Line3", "Unfollowed", ""));
        exampleList.add(new Item(R.drawable.ic_baseline_account_circle_24, "Line5", "Unfollowed", ""));


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new Adapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                if (!exampleList.get(position).getText2().equals("Followed")){
                    exampleList.get(position).changeText2("Followed");
                }else{
                    exampleList.get(position).changeText2("Unfollowed");
                }
                mAdapter.notifyItemChanged(position);
            }

            @Override
            public void onFollowClick(int position) {
                if (exampleList.get(position).getText3().equals("")){
                    exampleList.get(position).changeText3("Followed");
                }else{
                    exampleList.get(position).changeText3("");
                }
                mAdapter.notifyItemChanged(position);
            }
        });



        return rootView;
    }

    //THIS DOESNT FUCKING WORK AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}