package com.adnan.lgstudies.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adnan.lgstudies.Fragments.AnouncementFragment;
import com.adnan.lgstudies.Fragments.ClassTopicFragment;
import com.adnan.lgstudies.Fragments.DescriptionFragment;

public class VPClassroomFragmentAdapter extends FragmentStateAdapter {


    public VPClassroomFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
     switch (position){
         case 0:
             return new DescriptionFragment();
         case 1:
             return new ClassTopicFragment();
         case 2:
             return new AnouncementFragment();
         default:
             return new ClassTopicFragment();
     }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
