package com.ritnoawcy.currentmarket;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private static final String LOG = MyAdapter.class.getName();
    private static final int LAYOUT_TYPE_NO_ITEM_VIEW = 0;
    private static final int LAYOUT_TYPE_TWO_ITEM_VIEW = 1;
    private static final int LAYOUT_TYPE_ONE_ITEM_VIEW = 2;
    private ArrayList<Data> list=new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    public void setSwipeRefreshLayout(SwipeRefreshLayout view){
        this.swipeRefreshLayout =view;
    }
    public void loadRefresh() {
        list.clear();
        notifyDataSetChanged();
        volley.add(stringRequest);
    }

    public static class MyData{
        String name;
        String imgUrl;
        int number;
    }
    public class Data{
        ArrayList<MyData> list=new ArrayList<>();
        int type;

        String error="";
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType==0){
            view = new TextView(context);
        }else{
            view=context.getLayoutInflater().inflate(R.layout.category_item,parent,false);
        }


        return new MyHolder(view,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if(list.get(position).type ==LAYOUT_TYPE_TWO_ITEM_VIEW){
            //item:1
            holder.tv1.setHint(""+position);
           ArrayList<MyData> mlist=list.get(position).list;
            holder.tv1.setText(""+mlist.get(0).name);
            Picasso.get()
                    .load(mlist.get(0).imgUrl)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img_1)
                    .into(holder.img1);
            //item:2
            holder.tv2.setText(""+mlist.get(1).name);
            Picasso.get()
                    .load(mlist.get(1).imgUrl)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img_1)
                    .into(holder.img2);

        }else if(list.get(position).type ==LAYOUT_TYPE_ONE_ITEM_VIEW){
            //item:1
            holder.tv1.setHint(""+position);
            holder.item2.setVisibility(View.INVISIBLE);
            ArrayList<MyData> mlist=list.get(position).list;
            holder.tv1.setText(""+mlist.get(0).name);
            Picasso.get()
                    .load(mlist.get(0).imgUrl)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img_1)
                    .into(holder.img1);
        }else {
            holder.tvLog.setText(list.get(position).error);
        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(int i) {

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv1,tv2;
        private ImageView img1,img2;
        private CardView item1,item2;
        private TextView tvLog;
        public MyHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            if(viewType ==LAYOUT_TYPE_NO_ITEM_VIEW){
                tvLog=(TextView) itemView;
                return;
            }
            item1 = itemView.findViewById(R.id.categoryItem_item1);
            item2 = itemView.findViewById(R.id.categoryItem_item2);
            LinearLayout ll1 = itemView.findViewById(R.id.categoryItem_ll1);
            LinearLayout ll2 = itemView.findViewById(R.id.categoryItem_ll2);
            img1 = (ImageView) ll1.getChildAt(0);
            tv1 = (TextView) ll1.getChildAt(1);

            img2 = (ImageView) ll2.getChildAt(0);
            tv2 = (TextView) ll2.getChildAt(1);
            tv1.setSelected(true);
            tv2.setSelected(true);

            itemView.post(()->{
                int w= itemView.getWidth();
                int h=itemView.getHeight();

                int iw=img1.getWidth();
                int ih=w/3;

                img1.getLayoutParams().width = iw;
                img1.getLayoutParams().height = ih;


                img2.getLayoutParams().width = iw;
                img2.getLayoutParams().height = ih;

                if(BuildConfig.DEBUG){
                    //Log.i(LOG,String.format("h=%d,w=%d,iw=%d,ih=%d",h,w,iw,ih));
                }
                //Toast.makeText(context, "h="+h +",ih="+ ih+ " w="+w+",iw="+iw, Toast.LENGTH_SHORT).show();

            });
           // Toast.makeText(context, ""+ll1.getChildCount(), Toast.LENGTH_SHORT).show();

            ll1.setOnClickListener((v)->{
                try {
                    if(list.size()==0)return;
                    int position = Integer.parseInt(tv1.getHint().toString());
                    Intent intent =new Intent(context,CategoryOpenActivity.class);
                    MyData myData = list.get(position).list.get(0);
                    intent.putExtra("open",myData.number);
                    intent.putExtra("name",myData.name);
                    intent.putExtra("imgUrl",myData.imgUrl);
                    context.startActivity(intent);
                    // Toast.makeText(context, ""+tv1.getHint(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    if(BuildConfig.DEBUG){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
            ll2.setOnClickListener((v) ->{
                try {
                    if(list.size()==0)return;
                    //Toast.makeText(context, ""+tv1.getHint(), Toast.LENGTH_SHORT).show();
                    int position = Integer.parseInt(tv1.getHint().toString());
                    Intent intent =new Intent(context,CategoryOpenActivity.class);
                    MyData myData = list.get(position).list.get(1);
                    intent.putExtra("open",myData.number);
                    intent.putExtra("name",myData.name);
                    intent.putExtra("imgUrl",myData.imgUrl);
                    context.startActivity(intent);
                }catch (Exception e){
                    if(BuildConfig.DEBUG){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private MainActivity context;
    private RequestQueue volley;
    private StringRequest stringRequest;
    public MyAdapter(@NonNull MainActivity context) {
        this.context = context;
         volley = Volley.newRequestQueue(context);

        String url =context.getString(R.string.host)+"projects/CurrentMarket/category.php";

        stringRequest=new StringRequest(Request.Method.GET, url, (response) ->{
            //Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray= jsonObject.optJSONArray("category");

                int len =jsonArray.length();
                int remainLen=jsonArray.length() %2;
                if( remainLen!=0){
                    len =len-1;
                }
                for (int i=0;i<len;i+=2){

                    Data data=new Data();
                    data.type=1;
                    //item:1
                    JSONObject category = jsonArray.getJSONObject(i);
                    MyData mydata=new MyData();
                    mydata.name=category.optString("name");
                    mydata.imgUrl = category.optString("imgUrl");
                    mydata.number=category.optInt("number");
                    if(mydata.imgUrl.isEmpty())mydata.imgUrl="da";
                    data.list.add(mydata);
                    //item:2
                    category = jsonArray.getJSONObject(i+1);
                    mydata=new MyData();
                    mydata.name=category.optString("name");
                    mydata.imgUrl = category.optString("imgUrl");
                    mydata.number=category.optInt("number");
                    if(mydata.imgUrl.isEmpty())mydata.imgUrl="da";
                    data.list.add(mydata);

                    list.add(data);
                }

                if(remainLen!=0){
                    Data data=new Data();
                    data.type=2;
                    //item:1
                    JSONObject category = jsonArray.getJSONObject(len);
                    MyData mydata=new MyData();
                    mydata.name=category.optString("name");
                    mydata.imgUrl = category.optString("imgUrl");
                    mydata.number=category.optInt("number");
                    if(mydata.imgUrl.isEmpty())mydata.imgUrl="da";
                    data.list.add(mydata);
                    list.add(data);
                }
                notifyDataSetChanged();
                if(swipeRefreshLayout!=null)swipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                //Log.w(LOG,e.getMessage());
                if(BuildConfig.DEBUG){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


              }, (error)->{
                Data data= new Data();
                data.error = error.getMessage();
                list.add(data);

                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                //list.add(error.getMessage());
                if(swipeRefreshLayout.isRefreshing())swipeRefreshLayout.setRefreshing(false);
                notifyDataSetChanged();
              }){
           @Nullable
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               return super.getParams();
           }
       };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
       volley.add(stringRequest);
    }
}
