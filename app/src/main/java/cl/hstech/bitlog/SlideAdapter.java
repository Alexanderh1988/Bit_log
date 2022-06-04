package cl.hstech.bitlog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static cl.hstech.bitlog.AppIntroduction.pictures;
import static cl.hstech.bitlog.AppIntroduction.titlesIntro;


public class SlideAdapter extends PagerAdapter {

    private static final String TAG = "debug";
    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
          container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return pictures.length;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ServiceCast")
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider, container, false);

       TextView title = view.findViewById(R.id.slider_text);
         title.setText(titlesIntro[position]);
        ImageView slideImage = view.findViewById(R.id.slider_img);
        ProgressBar progressBar = view.findViewById(R.id.slider_img_progress);

        //slideImage.setImageResource(Img[position]);

        Picasso.get().load(pictures[position]).into(slideImage, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        //  slideImage.resetPivot();


        // TextView title = view.findViewById(R.id.slider_title);
        // title.setText(Title[position]);

        // TextView descr = view.findViewById(R.id.slider_descr);
        // descr.setText(Descr[position]);
        // Log.d(TAG, "position: " + position);

        container.addView(view);

        return view;
    }
 }
