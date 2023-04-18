package com.ritnoawcy.currentmarket;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class CategoryOpenActivity extends AppCompatActivity {
    private PDFView pdfView;
    private TextView tvLog;
    private SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_open_activity);
        tvLog =findViewById(R.id.tvLog);
        TextView tvOpenCatergoryName = findViewById(R.id.categoryOpen_name);
        ImageView imgCategory=findViewById(R.id.categoryOpen_img);
        tvOpenCatergoryName.setSelected(true);
       swipeRefreshLayout=findViewById(R.id.categoryOpen_refresh);
       swipeRefreshLayout.setColorSchemeColors(getColor(R.color.my_light_primary));
        int openNumber=getIntent().getIntExtra("open",0);
        tvOpenCatergoryName.setText(getIntent().getStringExtra("name"));
        Picasso.get()
                .load(getIntent().getStringExtra("imgUrl"))
                .placeholder(R.drawable.img)
                .error(R.drawable.img_1)
                .into(imgCategory);
        pdfView =findViewById(R.id.pdfViewer);

        String url = getString(R.string.host)+"projects/CurrentMarket/?";
        swipeRefreshLayout.setOnRefreshListener(()-> {
            openPdf(url,openNumber);
        });
        swipeRefreshLayout.setRefreshing(true);
        openPdf(url,openNumber);

    }
    private void openPdf(String url,int openNumber){
        new Thread(()->{
            try {
                InputStream inputStream=new URL(url+"number="+openNumber).openStream();
                loadPdf(inputStream);
            } catch (IOException e) {
                runOnUiThread(()-> {
                    if(BuildConfig.DEBUG){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    tvLog.setText("Not found:"+e.getMessage());
                    if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
                });

            }
        }).start();
    }


    public void loadPdf(InputStream inputStream){

        pdfView.fromStream(inputStream)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                .onDrawAll(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }) // called after document is loaded and starts to be rendered
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .onPageScroll(new OnPageScrollListener() {

                    @Override
                    public void onPageScrolled(int page, float positionOffset) {
                        tvLog.setText(""+page);
                        if(page==0||page==1){
                            if(!swipeRefreshLayout.isEnabled())swipeRefreshLayout.setEnabled(true);
                        }else{
                            if(swipeRefreshLayout.isEnabled())swipeRefreshLayout.setEnabled(false);
                        }
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {

                        if(BuildConfig.DEBUG){
                            tvLog.setText(t.getMessage());
                        }
                        if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        tvLog.setText(t.getMessage());
                        if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
                    }
                })
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

                    }
                }) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        return false;
                    }
                })
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }
}
