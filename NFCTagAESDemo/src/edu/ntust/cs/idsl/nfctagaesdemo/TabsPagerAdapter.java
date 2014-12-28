package edu.ntust.cs.idsl.nfctagaesdemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.ntust.cs.idsl.nfctagaesdemo.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final Fragment[] FRAGMENTS = new Fragment[] { new TagReaderFragment(), new TagWriterFragment() };
    private static final int TAG_READING_FRAGMENT_INDEX = 0;
    private static final int TAG_WRITING_FRAGMENT_INDEX = 1;
    private Context context;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return FRAGMENTS[i];
    }

    @Override
    public int getCount() {
        return FRAGMENTS.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
        case TAG_READING_FRAGMENT_INDEX:
            return context.getString(R.string.title_fragment_tag_reader);
        case TAG_WRITING_FRAGMENT_INDEX:
            return context.getString(R.string.title_fragment_tag_writer);
        }
        return null;
    }

}
