package com.softperl.urdunovelscollections.FragmentUtil;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softperl.urdunovelscollections.ConstantUtil.Constant;
import com.softperl.urdunovelscollections.ObjectUtil.OnBoardObject;
import com.softperl.urdunovelscollections.R;
import com.softperl.urdunovelscollections.Utility.Utility;


public class OnBoardFragment extends Fragment {

    private ImageView imageBoard;
    private TextView txtTitle;
    private TextView txtTagline;
    private OnBoardObject onBoardObject;

    /**
     * <p>It is used to get Fragment Instance for using in Pager</p>
     *
     * @param onBoardObject
     * @return
     */
    public static Fragment getFragmentInstance(OnBoardObject onBoardObject) {
        Bundle args = new Bundle();
        args.putParcelable(Constant.IntentKey.ON_BOARD, onBoardObject);
        Fragment fragment = new OnBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_on_board_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getIntentData(); //Receive Intent Data
        initUI(view); //Initialize UI
    }

    /**
     * <p>It is used to receive Intent data</p>
     */
    private void getIntentData() {

        onBoardObject = getArguments().getParcelable(Constant.IntentKey.ON_BOARD);

    }

    /**
     * <p>It initialize the UI</p>
     */
    private void initUI(View view) {

        imageBoard = (ImageView) view.findViewById(R.id.image_board);
        txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTagline = (TextView) view.findViewById(R.id.txt_tagline);

        if (onBoardObject.getIcon() != 0)
            imageBoard.setImageResource(onBoardObject.getIcon());

        if (!Utility.isEmptyString(onBoardObject.getTitle()))
            txtTitle.setText(onBoardObject.getTitle());

        if (!Utility.isEmptyString(onBoardObject.getTagline()))
            txtTagline.setText(onBoardObject.getTagline());


    }


}