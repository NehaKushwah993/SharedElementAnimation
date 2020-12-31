package com.example.neha.transitionanim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.neha.transitionanim.detailFragments.WallDetailFragment;
import com.example.neha.transitionanim.detailFragments.ProfileDetailFragment;
import com.example.neha.transitionanim.sharedElementAnim.TransitionFragment;

public class RecyclerViewFragment extends Fragment {


    private View fragmentContainer;
    private TransitionFragment detailsFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_fragment_layout, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentContainer = getView().findViewById(R.id.container_fragment2);
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);


    }

    public void onBackPressed() {

        if(detailsFragment !=null){
            detailsFragment.removeMe(null);
            detailsFragment = null;
        }

    }

    public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {


        @NonNull
        @Override
        public Adapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Adapter.Holder holder, final int position) {
            holder.itemView.findViewById(R.id.image_list_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showWallDetailsPage(v);

                }
            });

            holder.itemView.findViewById(R.id.profile_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showProfileDetailsPage(v);

                }
            });
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        public class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
            }
        }
    }

    private void showWallDetailsPage(View imageView) {

        detailsFragment = new WallDetailFragment();
        detailsFragment.addFragmentWithAnimation(RecyclerViewFragment.this.getActivity(), getChildFragmentManager(), imageView, fragmentContainer);

    }

    private void showProfileDetailsPage(View imageView) {
        detailsFragment = new ProfileDetailFragment();
        detailsFragment.addFragmentWithAnimation(RecyclerViewFragment.this.getActivity(), getChildFragmentManager(), imageView, fragmentContainer);
    }

}
