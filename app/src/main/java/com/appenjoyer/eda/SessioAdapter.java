package com.appenjoyer.eda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Joan on 28/05/2016.
 */
public class SessioAdapter extends ArrayAdapter<Sessio> {

    private LayoutInflater mInflater;
    private TreeSet<Sessio> ALAdapter;
    private ArrayList<Sessio> ArrayListReference;
    public SessioAdapter(Context context, ArrayList<Sessio> list){
        super(context,0,list);
        ALAdapter = new TreeSet<>(list);
        this.ArrayListReference = list;
        init(context);
    }
    private void init(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_item_dialog, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final Sessio sessio = getItem(position);
        vh.cbAssist.setOnCheckedChangeListener(null);
        vh.tvSessioName.setText(sessio.getName());
        vh.cbAssist.setChecked(sessio.isState());
        vh.cbAssist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ArrayListReference.get(position).setState(isChecked);
            }
        });
        return convertView;
    }
    //Return the treeset with reference
    public TreeSet<Sessio> getArrayListModified(){return this.ALAdapter;}
    static class ViewHolder {
        TextView tvSessioName;
        CheckBox cbAssist;
        private ViewHolder(View rootView) {
            tvSessioName = (TextView) rootView.findViewById(R.id.tvDataAs);
            cbAssist = (CheckBox) rootView.findViewById(R.id.checkbox);
        }
    }
}
