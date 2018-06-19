package pooria.storeitems.Category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pooria.storeitems.R;

public class CategoryAdapter  extends  RecyclerView.Adapter<CategoryAdapter.ItemCategoryViewHolder> {

private ArrayList<String> mNumberItems;

private int viewHolderCount;

private CategoryAdapterOnClickHandler mClickHandler;

    public CategoryAdapter (ArrayList<String> ItemOfCategory , CategoryAdapterOnClickHandler adapterOnClickHandler){

        mNumberItems=ItemOfCategory;
mClickHandler=adapterOnClickHandler;
        viewHolderCount=0;
    }

    @NonNull
    @Override
    public ItemCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();

        View view= LayoutInflater.from(context).inflate(R.layout.category_recyceler_item,parent,false);
       return   new ItemCategoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemCategoryViewHolder holder, int position) {

        String name=mNumberItems.get(position);

        TextView textView=holder.itemView.findViewById(R.id.Category_name_View);

        textView.setText(name);

    }

    @Override
    public int getItemCount() {

       return viewHolderCount=mNumberItems.size();


     }


interface CategoryAdapterOnClickHandler{

        void onClick(String Title);


}







    class ItemCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    public ItemCategoryViewHolder(View itemView) {


        super(itemView);
        itemView.setOnClickListener(this);
    }


        @Override
        public void onClick(View v) {
            int ItemPosition=getAdapterPosition();


           String title= mNumberItems.get(ItemPosition);

           mClickHandler.onClick(title);

        }
    }







}
