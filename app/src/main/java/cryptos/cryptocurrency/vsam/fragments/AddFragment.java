package cryptos.cryptocurrency.vsam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.imageview.ShapeableImageView;

import cryptos.cryptocurrency.vsam.R;

public class AddFragment extends Fragment {

private ShapeableImageView gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_add, container, false);




        return view;
    }
}