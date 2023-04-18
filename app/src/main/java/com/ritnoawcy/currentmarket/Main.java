package com.ritnoawcy.currentmarket;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Main {
    //main content view for activity
    private LinearLayout mainLayout;
    public static Main main;
    //subcontent view for unchangeable view
    private View oldContentView;
    private MainActivity activity;

    private Main(MainActivity activity){
        this.activity = activity;
    }
    public static void loadContentView(MainActivity activity) {
        if(main.mainLayout.getParent()!=null){
            ((ViewGroup)main.mainLayout.getParent()).removeView(main.mainLayout);
        }
        activity.setContentView(Main.main.mainLayout);
    }

    public void setContentView(View view){
        if(oldContentView!=null)mainLayout.removeView(oldContentView);
       mainLayout.addView(view);
        oldContentView = view;
    }


    public static void init(MainActivity activity){
        if(main==null){
            onInit(activity);
        }
    }

    private static void onInit(MainActivity context) {
        main = new Main(context);
        main.mainLayout= (LinearLayout) context.getLayoutInflater().inflate(R.layout.home_window,null);
        main._init_();

    }
    public View getContentView(){
        return main.mainLayout;
    }

    private void _init_() {
        View viewFlash =activity.getLayoutInflater().inflate(R.layout.flash_screen,null);
        viewFlash.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        setContentView(viewFlash);
        initLayout();
    }

    @SuppressLint("MissingInflatedId")
    private void initLayout() {

        View v=activity.getLayoutInflater().inflate(R.layout.activity_main,null);

        TextView tvFlash = (TextView)findViewById(R.id.tvFlash);

        Handler handler=new Handler(activity.getMainLooper());
        LinearLayout lladd =v.findViewById(R.id.llAdd);

        MyAdapter adapter=new MyAdapter(activity);
        View listVie = activity.getLayoutInflater().inflate(R.layout.list,null);
        SwipeRefreshLayout swipeRefreshLayout=listVie.findViewById(R.id.listCategoryRefreshLayout);
        RecyclerView recyclerView=listVie.findViewById(R.id.listView);
        lladd.addView(listVie);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(activity.getColor(R.color.my_light_primary));
        adapter.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(()-> {
            recyclerView.setEnabled(false);
            adapter.loadRefresh();
        });

        new Handler().postDelayed(()->{setContentView(v);},2000);

        //
        if(true)return;
        int a=0;


        Runnable runnable=new Runnable() {
            int cout=0;
            @Override
            public void run() {
                tvFlash.setText(""+ cout);

                for (int i=0;i<10000;i++){
                   adapter.add(i+cout);

                }
                if(cout<100){
                    cout++;
                    handler.postDelayed(this,10);
                }else{
                    setContentView(v);
                    handler.removeCallbacks(this);
                }


            }
        };

        handler.post(runnable);





    }
    private View findViewById(int id) {
        return mainLayout.findViewById(id);
    }
}
