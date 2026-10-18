package com.adnan.lgstudies.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.adnan.lgstudies.Fragments.NotesFragment;
import com.adnan.lgstudies.Fragments.VideoesFragment;

public class VideosNotesFragmentHolder extends FragmentStateAdapter {
    public VideosNotesFragmentHolder(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
      switch (position){
          case 0:
              return new VideoesFragment();
          case 1:
              return new NotesFragment();
          default:
              return new VideoesFragment();
      }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
