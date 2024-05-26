package com.androidstudy.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class SearchPillActivity extends AppCompatActivity {
    ListView pillList;
    TextView searchBar;
    Button searchButton;

    ChipGroup symptomGroup;
    Chip coldChip, digestionChip, diarrheaChip,
            feverChip, acheChip, inflammationChip,
             adhdChip, othersChip;

    ArrayList<String> itemRooms = new ArrayList<>();

    // 리스트에 있는 전체 약의 개수
    static final int PILL_COUNT = 6;
    // 검색된 리스트 항목 개수
    int itemCount = PILL_COUNT;

    public class PillItem {
        Integer pillImageResource;
        String pillNameResource;
        String pillSymptomResource;

        PillItem(Integer pillImageResource, String pillNameResource, String pillSymptomResource) {
            this.pillImageResource = pillImageResource;
            this.pillNameResource = pillNameResource;
            this.pillSymptomResource = pillSymptomResource;
        }

        public Integer getPillImageResource() {
            return pillImageResource;
        }

        public String getPillNameResource() {
            return pillNameResource;
        }

        public String getPillSymptomResource() {
            return pillSymptomResource;
        }

        public void setPillImageResource(Integer pillImageResource) {
            this.pillImageResource = pillImageResource;
        }

        public void setPillNameResource(String pillNameResource) {
            this.pillNameResource = pillNameResource;
        }

        public void setPillSymptomResource(String pillSymptomResource) {
            this.pillSymptomResource = pillSymptomResource;
        }
    }

    PillItem exampleItems[] = new PillItem[PILL_COUNT];

    // 증상별 필터 ON/OFF 표시를 위한 boolean타입 배열
    boolean[] symptomSwitch = new boolean[] {
            false, false, false, false,
            false, false, false, false
    };


    // 증상별 고유 번호(symptomSwitch ArrayList의 인덱싱에 쓰임)
    static final int COLD = 0;
    static final int DIGESTION = 1;
    static final int DIARRHEA = 2;
    static final int FEVER = 3;
    static final int ACHE = 4;
    static final int INFLAMMATION = 5;
    static final int ADHD = 6;
    static final int OTHERS = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_pill);

        searchBar = (TextView) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);
        pillList = (ListView)findViewById(R.id.pillList);

        // 검색 버튼 클릭 리스너 설정
        setupSearchButtonClickListener(searchButton);

        // ChipGroup 및 Chip 요소 참조
        symptomGroup = findViewById(R.id.symptomGroup);

        coldChip = findViewById(R.id.cold);
        digestionChip = findViewById(R.id.digestion);
        diarrheaChip = findViewById(R.id.diarrhea);
        feverChip = findViewById(R.id.fever);
        acheChip = findViewById(R.id.ache);
        inflammationChip = findViewById(R.id.inflammation);
        adhdChip = findViewById(R.id.adhd);
        othersChip = findViewById(R.id.others);

        // Chip 클릭 리스너 설정
        setupChipClickListener(coldChip);
        setupChipClickListener(digestionChip);
        setupChipClickListener(diarrheaChip);
        setupChipClickListener(feverChip);
        setupChipClickListener(acheChip);
        setupChipClickListener(inflammationChip);
        setupChipClickListener(adhdChip);
        setupChipClickListener(othersChip);

        exampleItems[0] = new PillItem(R.drawable.pill, "감기약", "기침,콧물");
        exampleItems[1] = new PillItem(R.drawable.pill1, "소화제", "소화불량");
        exampleItems[2] = new PillItem(R.drawable.pill2, "설사약", "설사,소화불량,기침,코감기");
        exampleItems[3] = new PillItem(R.drawable.pill3, "자양강장", "피로회복");
        exampleItems[4] = new PillItem(R.drawable.pill4, "통증약", "두통,치통,신경통");
        exampleItems[5] = new PillItem(R.drawable.pill5, "염증약", "설사,소화불량");

        // 약 검색 리스트뷰의 어댑터 설정
        for(int i = 0; i < itemCount; i++) {
            itemRooms.add("temp");
        }

        CustomList adapter = new CustomList(SearchPillActivity.this);
        pillList.setAdapter(adapter);

    }

    // 약 검색 버튼 클릭시 실행(텍스트로 검색)
    public void setupSearchButtonClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            // 변수 선언
            boolean searchSuccess = false;
            int searchItemCount = 100; // 검색되는 약의 최대 개수를 임시로 100으로 설정
            int[] indexOfPill = new int[searchItemCount];
            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                searchSuccess = false;
                indexOfPill = new int[searchItemCount];
                indexOfPill[0] = -1;
                itemCount = 0;

                String searchString = searchBar.getText().toString();

                pillList.setVisibility(View.VISIBLE);

                for (int i = 0, position = -1; i < PILL_COUNT; i++) {
                    if(exampleItems[i].getPillNameResource().contains(searchString)) {
                        position++;
                        indexOfPill[position] = i;
                        itemCount++;
                        searchSuccess = true;
                    }
                }

                itemRooms.clear();
                for(int i = 0; i < itemCount; i++) {
                    itemRooms.add("temp");
                }

                CustomList newAdapterByText = new CustomList(SearchPillActivity.this) {
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        LayoutInflater inflater = context.getLayoutInflater();
                        View rowView = inflater.inflate(R.layout.pill_list_item, null);

                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                        TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

                        Log.d("onClick", "searchSuccess: " + searchSuccess);
                        Log.d("onClick", "position: " + position);

                        if(searchSuccess) {
                            pillList.setVisibility(View.VISIBLE);

                            if(position <= indexOfPill[position]) {
                                rowView.setVisibility(View.VISIBLE);

                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());

                                return rowView;
                            }
                            else {
                                Log.d("onClick", "position: " + position);
                                rowView.setVisibility(View.GONE);

                                return rowView;
                            }
                        }
                        else {
                            pillList.setVisibility(View.GONE);

                            return rowView;
                        }
                    }
                };
                pillList.setAdapter(newAdapterByText);
            }
        });
    }


    // 증상별 필터 클릭시 해당 증상에 대한 약만 표시되도록 리스트뷰 업데이트
    public void setupChipClickListener(Chip chip) {
        chip.setOnClickListener(new View.OnClickListener() {

            int searchItemCount = 100; // 검색되는 약의 최대 개수를 임시로 100으로 설정
            int[] indexOfPill = new int[searchItemCount];
            int symptomOnCount;

            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                indexOfPill = new int[searchItemCount];
                indexOfPill[0] = -1;
                symptomOnCount = 0;
                itemCount = 0;

                String symptomString = chip.getText().toString();

                // 해당 필터에 대한 boolean값 true/false 토글
                switch(symptomString) {
                    case "감기":
                        symptomSwitch[COLD] = toggleSymptom(symptomSwitch[COLD]);
                        break;
                    case "소화":
                        symptomSwitch[DIGESTION] = toggleSymptom(symptomSwitch[DIGESTION]);
                        break;
                    case "설사":
                        symptomSwitch[DIARRHEA] = toggleSymptom(symptomSwitch[DIARRHEA]);
                        break;
                    case "열":
                        symptomSwitch[FEVER] = toggleSymptom(symptomSwitch[FEVER]);
                        break;
                    case "통증":
                        symptomSwitch[ACHE] = toggleSymptom(symptomSwitch[ACHE]);
                        break;
                    case "염증":
                        symptomSwitch[INFLAMMATION] = toggleSymptom(symptomSwitch[INFLAMMATION]);
                        break;
                    case "ADHD":
                        symptomSwitch[ADHD] = toggleSymptom(symptomSwitch[ADHD]);
                        break;
                    case "기타":
                        symptomSwitch[OTHERS] = toggleSymptom(symptomSwitch[OTHERS]);
                        break;
                    default:
                        break;
                }

                pillList.setVisibility(View.VISIBLE);

                // ON 상태인 증상 개수 세기
                for (boolean symptom : symptomSwitch) {
                    if (symptom) {
                        symptomOnCount++;
                    }
                }

                if (symptomOnCount < 1) {
                    itemCount = PILL_COUNT;

                    itemRooms.clear();
                    for(int i = 0; i < itemCount; i++) {
                        itemRooms.add("temp");
                    }

                    CustomList adapter = new CustomList(SearchPillActivity.this);
                    pillList = (ListView)findViewById(R.id.pillList);
                    pillList.setAdapter(adapter);
                }
                else if (symptomOnCount == 1) { // symptomOnCount가 2이상에서 1로 되는 경우 오류있음
                    int i = 0, position = 0;
                    String symptomResource;

                    while(i < PILL_COUNT) {
                        symptomResource = exampleItems[i].getPillSymptomResource();

                        if (symptomSwitch[COLD]) {
                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[DIGESTION]) {
                            if(symptomResource.contains("소화불량")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[DIARRHEA]) {
                            if(symptomResource.contains("설사")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[FEVER]) {
                            if(symptomResource.contains("발열")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[ACHE]) {
                            if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                    || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                    || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                    || symptomResource.contains("염좌통")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[INFLAMMATION]) {
                            if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                    || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[ADHD]) {
                            if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }

                        if (symptomSwitch[OTHERS]) {
                            if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                    && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                    && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                    && !symptomResource.contains("발열") && !symptomResource.contains("두통")
                                    && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                    && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                    && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                    && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                    && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                    && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }
                    }
                    itemRooms.clear();
                    for(int k = 0; k < itemCount; k++) {
                        itemRooms.add("temp");
                    }

                    CustomList newAdapterBySymptom = new CustomList(SearchPillActivity.this) {
                        @Override
                        public View getView(int position, View view, ViewGroup parent) {
                            LayoutInflater inflater = context.getLayoutInflater();
                            View rowView = inflater.inflate(R.layout.pill_list_item, null);

                            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);


                            if(position <= indexOfPill[position]) {
                                rowView.setVisibility(View.VISIBLE);

                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());

                                return rowView;
                            }
                            else {
                                Log.d("onClick", "position: " + position);
                                rowView.setVisibility(View.GONE);

                                return rowView;
                            }
                        }
                    };
                    pillList.setAdapter(newAdapterBySymptom);
                }
                else { // 필터가 2개이상 ON일 경우
                    int i = 0, position = 0;
                    String prev = null;
                    String symptomResource;

                    if (symptomSwitch[COLD]) {
                        while (i < PILL_COUNT) {
                            symptomResource = exampleItems[i].getPillSymptomResource();
                            if(symptomResource.contains("기침") || symptomResource.contains("콧물")
                                    || symptomResource.contains("코막힘") || symptomResource.contains("코감기")) {
                                indexOfPill[position] = i;
                                position++;
                                itemCount++;
                            }
                            i++;
                        }
                        prev = "감기";
                    }

                    if (symptomSwitch[DIGESTION]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "소화";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("소화불량")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                prev = "소화";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "소화";
                            }
                        }
                    }

                    if (symptomSwitch[DIARRHEA]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "설사";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("설사")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                prev = "설사";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "설사";
                            }
                        }
                    }

                    if (symptomSwitch[FEVER]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "열";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("발열")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);


                                prev = "열";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "열";
                            }
                        }
                    }

                    if (symptomSwitch[ACHE]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "통증";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                        || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("두통") || symptomResource.contains("치통")
                                        || symptomResource.contains("신경통") || symptomResource.contains("관절통")
                                        || symptomResource.contains("근육통") || symptomResource.contains("요통")
                                        || symptomResource.contains("염좌통")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                prev = "통증";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "통증";
                            }
                        }
                    }

                    if (symptomSwitch[INFLAMMATION]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "염증";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("위염") || symptomResource.contains("장염")
                                || symptomResource.contains("대장염") || symptomResource.contains("식도염")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                prev = "염증";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "염증";
                            }
                        }
                    }

                    if (symptomSwitch[ADHD]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                            prev = "ADHD";
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(symptomResource.contains("adhd") || symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                prev = "ADHD";
                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                                prev = "ADHD";
                            }
                        }
                    }

                    if (symptomSwitch[OTHERS]) {
                        if (prev == null) {
                            while (i < PILL_COUNT) {
                                symptomResource = exampleItems[i].getPillSymptomResource();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }
                        }
                        else {
                            i = 0;
                            position = 0;
                            itemCount = 0;

                            if (indexOfPill[i] >= 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = indexOfPill[i];
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            while (indexOfPill[i] > 0) {
                                symptomResource = exampleItems[indexOfPill[i]].getPillSymptomResource();
                                if(!symptomResource.contains("기침") && !symptomResource.contains("콧물")
                                        && !symptomResource.contains("코막힘") && !symptomResource.contains("코감기")
                                        && !symptomResource.contains("소화불량") && !symptomResource.contains("설사")
                                        && !symptomResource.contains("발열") &&!symptomResource.contains("두통")
                                        && !symptomResource.contains("치통") && !symptomResource.contains("신경통")
                                        && !symptomResource.contains("관절통") && !symptomResource.contains("근육통")
                                        && !symptomResource.contains("요통") && !symptomResource.contains("염좌통")
                                        && !symptomResource.contains("위염") && !symptomResource.contains("장염")
                                        && !symptomResource.contains("대장염") && !symptomResource.contains("식도염")
                                        && !symptomResource.contains("adhd") && !symptomResource.contains("집중력")) {
                                    indexOfPill[position] = i;
                                    position++;
                                    itemCount++;
                                }
                                i++;
                            }

                            if (itemCount == 0) {
                                itemRooms.clear();

                                CustomList adapter = new CustomList(SearchPillActivity.this);
                                pillList = (ListView)findViewById(R.id.pillList);
                                pillList.setAdapter(adapter);

                                return;
                            }
                            else {
                                itemRooms.clear();
                                for(int k = 0; k < itemCount; k++) {
                                    itemRooms.add("temp");
                                }

                                if (indexOfPill[itemCount] > 0) {
                                    for (int j = itemCount; j < PILL_COUNT; j++) {
                                        indexOfPill[j] = 0;
                                    }
                                }
                            }
                        }
                    }

                    CustomList newAdapterBySymptom = new CustomList(SearchPillActivity.this) {
                        @Override
                        public View getView(int position, View view, ViewGroup parent) {
                            LayoutInflater inflater = context.getLayoutInflater();
                            View rowView = inflater.inflate(R.layout.pill_list_item, null);

                            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

                            if(position <= indexOfPill[position]) {
                                rowView.setVisibility(View.VISIBLE);

                                pillImage.setImageResource(exampleItems[indexOfPill[position]].getPillImageResource());
                                pillName.setText(exampleItems[indexOfPill[position]].getPillNameResource());
                                pillSymptom.setText(exampleItems[indexOfPill[position]].getPillSymptomResource());

                                return rowView;
                            }
                            else {
                                Log.d("onClick", "position: " + position);
                                rowView.setVisibility(View.GONE);

                                return rowView;
                            }
                        }
                    };
                    pillList.setAdapter(newAdapterBySymptom);
                }
            }
        });
    }

    // 증상별 필터 ON/OFF로 전환해주는 Toggle 기능
    public boolean toggleSymptom(boolean symptom) {
        return !symptom;
    }


    // 리스트뷰에 활용할 어댑터 정의
    public class CustomList extends ArrayAdapter<String> {
        public final Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.pill_list_item, itemRooms);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

            pillImage.setImageResource(exampleItems[position].getPillImageResource());
            pillName.setText(exampleItems[position].getPillNameResource());
            pillSymptom.setText(exampleItems[position].getPillSymptomResource());

            return rowView;
        }
    }
}