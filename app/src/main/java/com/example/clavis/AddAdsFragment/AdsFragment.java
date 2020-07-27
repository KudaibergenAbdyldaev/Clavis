package com.example.clavis.AddAdsFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.clavis.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_ads, container, false);

        ImageView imageView = view.findViewById(R.id.imageView5);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts(
                        "mailto", "clavisholding@gmail.com", null));
                requireActivity().startActivity(Intent.createChooser(emailIntent, null));
            }
        });

        ImageView imageView2 = view.findViewById(R.id.imageView6);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urll = "https://www.instagram.com/clavis.kg/?hl=ru";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urll));
                startActivity(i);
            }
        });

        ImageView imageView3 = view.findViewById(R.id.imageView7);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+"+996551947777";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ImageView imageViewKG = view.findViewById(R.id.imageView8);
        imageViewKG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts(
                        "mailto", "clavisholding@gmail.com", null));
                requireActivity().startActivity(Intent.createChooser(emailIntent, null));
            }
        });

        ImageView imageView2KG = view.findViewById(R.id.imageView9);
        imageView2KG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urll = "https://www.instagram.com/clavis.kg/?hl=ru";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urll));
                startActivity(i);
            }
        });

        ImageView imageView3KG = view.findViewById(R.id.imageView10);
        imageView3KG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+"+996551947777";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}
