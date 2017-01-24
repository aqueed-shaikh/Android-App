package org.pattonvillecs.pattonvilleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.pattonvillecs.pattonvilleapp.R;
import org.pattonvillecs.pattonvilleapp.fragments.calendar.CalendarFragment;
import org.pattonvillecs.pattonvilleapp.fragments.calendar.CalendarPinnedFragment;
import org.pattonvillecs.pattonvilleapp.fragments.news.NewsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    public static String[] sampleHeadlines = {"Pattonville App in development",
            "Pattonville Robotics Club wins recent match!",
            "PHS to host basketball celebration on Jan. 20",
            "Students, staff recognized at November board meeting",
            "Pattonville ranked No. 6 in Missouri, tops in U.S. on annual \"best of\" list",
            "Headline Six", "Headline Seven", "Headline Eight", "Headline Nine", "Headline Ten",
            "Headline Eleven", "Headline Twelve"};

    public static int[] sampleImages = {R.drawable.test_news_1, R.drawable.test_news_2,
            R.drawable.test_news_3, R.drawable.test_news_4, R.drawable.test_news_1,
            R.drawable.test_news_2, R.drawable.test_news_3, R.drawable.test_news_4,
            R.drawable.test_news_1, R.drawable.test_news_2, R.drawable.test_news_3,
            R.drawable.test_news_4};

    public static String[] samplePinnedEvents = {"Pinned Event 1", "Pinned Event 2", "Pinned Event 3"};

    CarouselView carouselView;
    ListView newsListView;
    ListView eventListView;
    ListView pinnedListView;
    TextView newsSeeMoreTextView;
    TextView upcomingSeeMoreTextView;
    TextView pinnedSeeMoreTextView;
    ImageView newsSeeMoreArrow;
    ImageView upcomingSeeMoreArrow;
    ImageView pinnedSeeMoreArrow;
    NavigationView mNavigationView;
    String[] sampleEvents = {"Board Meeting", "Pattonville Official App Release", "Independence Day"};

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
    private OnFragmentInteractionListener mListener;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.title_fragment_home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        List<HashMap<String, String>> homeNewsList = new ArrayList<>();
        List<HashMap<String, String>> homeEventsList = new ArrayList<>();
        List<HashMap<String, String>> homePinnedList = new ArrayList<>();

        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
        carouselView.setPageCount(4);
        carouselView.setImageListener(imageListener);

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> newsListItem = new HashMap<>();
            newsListItem.put("image", Integer.toString(sampleImages[i]));
            newsListItem.put("headline", sampleHeadlines[i]);
            homeNewsList.add(newsListItem);
        }


        for (int i = 0; i < 2; i++) {
            HashMap<String, String> eventListItem = new HashMap<>();
            eventListItem.put("event", sampleEvents[i]);
            homeEventsList.add(eventListItem);
        }

        for (int i = 0; i < 2; i++) {

            HashMap<String, String> pinnedListItem = new HashMap<>();
            pinnedListItem.put("pin", samplePinnedEvents[i]);
            homePinnedList.add(pinnedListItem);

        }

        newsListView = (ListView) view.findViewById(R.id.home_news_listview);
        eventListView = (ListView) view.findViewById(R.id.home_upcoming_events_listview);
        pinnedListView = (ListView) view.findViewById(R.id.home_pinned_events_listview);
        newsSeeMoreTextView = (TextView) view.findViewById(R.id.recent_news_see_more_textview);
        upcomingSeeMoreTextView = (TextView) view.findViewById(R.id.home_upcoming_events_see_more_textview);
        pinnedSeeMoreTextView = (TextView) view.findViewById(R.id.home_pinned_events_see_more_textview);
        newsSeeMoreArrow = (ImageView) view.findViewById(R.id.home_recent_news_see_more_arrow);
        upcomingSeeMoreArrow = (ImageView) view.findViewById(R.id.home_upcoming_events_see_more_arrow);
        pinnedSeeMoreArrow = (ImageView) view.findViewById(R.id.home_pinned_events_see_more_arrow);
        mNavigationView = (NavigationView) view.findViewById(R.id.nav_view);


        String[] homeNewsListFrom = {"image", "headline"};
        String[] homeEventListFrom = {"event"};
        String[] homePinnedListFrom = {"pin"};

        int[] homeNewsListTo = {R.id.home_news_listview_item_imageView, R.id.home_news_listview_item_textView};
        int[] homeEventListTo = {R.id.home_upcoming_events_listview_textview};
        int[] homePinnedEventTo = {R.id.home_pinned_events_listview_textview};

        SimpleAdapter newsListSimpleAdapter = new SimpleAdapter(view.getContext(), homeNewsList, R.layout.home_news_listview_item, homeNewsListFrom, homeNewsListTo);
        SimpleAdapter eventListSimpleAdapter = new SimpleAdapter(view.getContext(), homeEventsList, R.layout.home_upcoming_events_listview_item, homeEventListFrom, homeEventListTo);
        SimpleAdapter pinnedListSimpleAdapter = new SimpleAdapter(view.getContext(), homePinnedList, R.layout.home_pinned_events_listview_item, homePinnedListFrom, homePinnedEventTo);

        newsListView.setAdapter(newsListSimpleAdapter);
        eventListView.setAdapter(eventListSimpleAdapter);
        pinnedListView.setAdapter(pinnedListSimpleAdapter);


        newsSeeMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, NewsFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });
        upcomingSeeMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, CalendarFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });
        pinnedSeeMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, CalendarPinnedFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });

        newsSeeMoreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, NewsFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });
        upcomingSeeMoreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, CalendarFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });
        pinnedSeeMoreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_default, CalendarPinnedFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

            }
        });


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
