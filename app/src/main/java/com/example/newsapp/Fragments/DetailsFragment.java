package com.example.newsapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";


    // TODO: Rename and change types of parameters
    private TextView detailsTitle;
    private TextView detailsAuthor;
    private TextView detailsDescription;
    private TextView detailsPublishedAt;
    private TextView detailsContent;
    private ImageView detailsImg;
    private Button addButton;

    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        detailsTitle = view.findViewById(R.id.details_title);
        detailsAuthor = view.findViewById(R.id.details_author);
        detailsDescription = view.findViewById(R.id.details_description);
        detailsPublishedAt = view.findViewById(R.id.details_publishedAt);
        detailsContent = view.findViewById(R.id.details_content);
        detailsImg = view.findViewById(R.id.details_img);


        detailsTitle.setText(mParam1);
        detailsAuthor.setText(mParam2);
        detailsDescription.setText(mParam3);
        detailsPublishedAt.setText(mParam4);
        detailsContent.setText(mParam5);
        Picasso.get().load(mParam6).into(detailsImg);
        return view;
    }
}
