package cool.project.fridgemanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends AppCompatActivity {

    List<Map<String, Object>> mMapList;
    SimpleAdapter mSimpleAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private Integer[] itemImage = { R.drawable.img_not_found };
    private String[] itemName = { "高椰菜", "花麗菜", "豬舌", "牛頭皮", "未知食品" };
    private String[] itemTag= { "青菜", "青菜", "肉", "肉", "未知" };
    private String[] itemPutDate = { "11/3/18", "11/1/18", "10/28/18", "10/31/18", "?/?/??" };
    private Integer[] itemRemainedDays = { 3, 5, 20, 12, 0 };
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ItemDB itemDB = new ItemDB(this);
        //ArrayList<ArrayList<String>> a = itemDB.getFullData();
        items = itemDB.getItems();
//        itemDB.clear();
        mGridView = findViewById(R.id.item_grid);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(Main.this, create_item.class));
            //Toast.makeText(this, "Nothing happened!", Toast.LENGTH_LONG).show();
        });

        gridViewUpdate(itemDB);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        recreate();
//    }

    private void gridViewUpdate(ItemDB itemDB){
        mMapList = new ArrayList<>();
//        for (int i = 0; i < itemName.length; i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("img", itemImage[0]);
//            item.put("name", itemName[i]);
//            item.put("tag", itemTag[i]);
//            item.put("put_date", itemPutDate[i]);
//            item.put("remained_days", itemRemainedDays[i].toString());
//            mMapList.add(item);
//        }
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", items.get(i).name);
            item.put("img", items.get(i).uri);
            item.put("tag", items.get(i).tag);
            item.put("put_date", items.get(i).getDateStr());
            item.put("remained_days", items.get(i).getRemainedDays());
            mMapList.add(item);
        }

        mSimpleAdapter = new SimpleAdapter(this, mMapList, R.layout.single_grid_item,
                new String[]{"img", "name", "tag", "put_date", "remained_days"},
                new int[]{R.id.grid_item_img, R.id.grid_item_name,
                        R.id.grid_item_tag, R.id.grid_item_put_date, R.id.grid_item_remained_days});
        mGridView.setNumColumns(2);
        mGridView.setAdapter(mSimpleAdapter);
    }

}
