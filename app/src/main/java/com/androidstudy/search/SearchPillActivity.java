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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class SearchPillActivity extends AppCompatActivity {
    ListView pillList;
    TextView searchBar;
    Button searchButton;

    ChipGroup symptomGroup;
    Chip coldChip, digestionChip, diarrheaChip,
            feverChip, atopyChip, insomniaChip,
            anxietyChip, adhdChip, impulseChip, autismChip;


    // 약품 이미지 예시
    Integer[] pillImages = {
            R.drawable.pill,
            R.drawable.pill1,
            R.drawable.pill2,
            R.drawable.pill3,
            R.drawable.pill4,
            R.drawable.pill5,
    };

    // 약품 이름 예시
    String[] pillNames = {
            "감기약",
            "소화제",
            "설사약",
            "해열제",
            "불면증 약",
            "불안 억제제"
    };

    // 약품 제조사 예시
    String[] pillManufacturers = {
            "삼성",
            "LG",
            "현대",
            "넥슨",
            "네이버",
            "카카오"
    };

    // 해당 약품이 필요한 증상 예시
    String[] pillSymptoms = {
            "감기",
            "소화",
            "설사",
            "열",
            "불면증",
            "불안"
    };

    // 증상별 필터 ON/OFF 표시를 위한 boolean타입 전역 변수
    boolean cold = false;
    boolean digestion = false;
    boolean diarrhea = false;
    boolean fever = false;
    boolean atopy = false;
    boolean insomnia = false;
    boolean anxiety = false;
    boolean adhd = false;
    boolean impulse = false;
    boolean autism = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.search_pill);

        // 약 검색 리스트뷰의 어댑터 설정
        CustomList adapter = new CustomList(SearchPillActivity.this);
        pillList = (ListView)findViewById(R.id.pillList);
        pillList.setAdapter(adapter);

        searchBar = (TextView) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);

        // 검색 버튼 클릭 리스너 설정
        setupSearchButtonClickListener(searchButton);

        // ChipGroup 및 Chip 요소 참조
        symptomGroup = findViewById(R.id.symptomGroup);
        coldChip = findViewById(R.id.cold);
        digestionChip = findViewById(R.id.digestion);
        diarrheaChip = findViewById(R.id.diarrhea);
        feverChip = findViewById(R.id.fever);
        atopyChip = findViewById(R.id.atopy);
        insomniaChip = findViewById(R.id.insomnia);
        anxietyChip = findViewById(R.id.anxiety);
        adhdChip = findViewById(R.id.adhd);
        impulseChip = findViewById(R.id.impulse);
        autismChip = findViewById(R.id.autism);

        // Chip 클릭 리스너 설정
        setupChipClickListener(coldChip);
        setupChipClickListener(digestionChip);
        setupChipClickListener(diarrheaChip);
        setupChipClickListener(feverChip);
        setupChipClickListener(atopyChip);
        setupChipClickListener(insomniaChip);
        setupChipClickListener(anxietyChip);
        setupChipClickListener(adhdChip);
        setupChipClickListener(impulseChip);
        setupChipClickListener(autismChip);
    }

    // 약 검색 버튼 클릭시 실행(텍스트로 검색)
    public void setupSearchButtonClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            // 변수 선언
            boolean searchSuccess = false;
            int searchItemCount = 30; // 검색되는 약의 최대 개수를 임시로 30으로 설정
            int[] indexOfSearch = new int[searchItemCount];
            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                searchSuccess = false;
                indexOfSearch = new int[searchItemCount];
                indexOfSearch[0] = -1;

                String searchString = searchBar.getText().toString();

                pillList.setVisibility(View.VISIBLE);
                Log.d("onClick", "searchString: " + searchString);

                for (int i = 0, lastIndex = -1; i < pillNames.length; i++) {
                    if(pillNames[i].contains(searchString)) {
                        Log.d("onClick", "pillNames[" + i + "]: " + pillNames[i].toString());
                        lastIndex++;
                        indexOfSearch[lastIndex] = i;
                        searchSuccess = true;
                        Log.d("onClick", "lastIndex: " + lastIndex);
                    }
                    Log.d("onClick", "searchSuccess: " + searchSuccess);
                }

                CustomList newAdapterByText = new CustomList(SearchPillActivity.this) {
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        LayoutInflater inflater = context.getLayoutInflater();
                        View rowView = inflater.inflate(R.layout.pill_list_item, null);

                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                        TextView pillManufacturer = (TextView) rowView.findViewById(R.id.pillManufacturer);
                        TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

                        Log.d("onClick", "searchSuccess: " + searchSuccess);
                        Log.d("onClick", "position: " + position);

                        if(searchSuccess) {
                            pillList.setVisibility(View.VISIBLE);

                            Log.d("onClick", "position: " + position);
                            Log.d("onClick", "indexOfSearch[0]: " + indexOfSearch[0]);
                            Log.d("onClick", "indexOfSearch[1]: " + indexOfSearch[1]);
                            Log.d("onClick", "indexOfSearch[2]: " + indexOfSearch[2]);
                            if(position <= indexOfSearch[position]) {
                                rowView.setVisibility(View.VISIBLE);

                                pillImage.setImageResource(pillImages[indexOfSearch[position]]);
                                pillName.setText(pillNames[indexOfSearch[position]]);
                                pillManufacturer.setText("제조사: " + pillManufacturers[indexOfSearch[position]]);
                                pillSymptom.setText(pillSymptoms[position]);

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

            int searchItemCount = 30; // 검색되는 약의 최대 개수를 임시로 30으로 설정
            int[] indexOfSearch = new int[searchItemCount];
            @Override
            public void onClick(View v) {
                // 버튼 클릭할 때마다 일부 변수값 초기화
                indexOfSearch = new int[searchItemCount];
                indexOfSearch[0] = -1;

                Log.d("onClick", "1. cold: " + cold);
                Log.d("onClick", "1. digestion: " + digestion);
                Log.d("onClick", "1. diarrhea: " + diarrhea);

                String symptomString = chip.getText().toString();

                switch(symptomString) {
                    case "감기":
                        cold = toggleSymptom(cold);
                        break;
                    case "소화":
                        digestion = toggleSymptom(digestion);
                        break;
                    case "설사":
                        diarrhea = toggleSymptom(diarrhea);
                        break;
                    case "열":
                        fever = toggleSymptom(fever);
                        break;
                    case "아토피":
                        atopy = toggleSymptom(atopy);
                        break;
                    case "불면증":
                        insomnia = toggleSymptom(insomnia);
                        break;
                    case "불안":
                        anxiety = toggleSymptom(anxiety);
                        break;
                    case "ADHD":
                        adhd = toggleSymptom(adhd);
                        break;
                    case "충동":
                        impulse = toggleSymptom(impulse);
                        break;
                    case "자폐":
                        autism = toggleSymptom(autism);
                        break;
                    default:
                        break;
                }

                pillList.setVisibility(View.VISIBLE);





                for (int i = 0, lastIndex = -1; i < pillSymptoms.length; i++) {
                    if(pillSymptoms[i].contains(symptomString)) {
                        Log.d("onClick", "pillSymptoms[" + i + "]: " + pillSymptoms[i]);
                        lastIndex++;
                        indexOfSearch[lastIndex] = i;
                        Log.d("onClick", "lastIndex: " + lastIndex);
                    }
                }

                CustomList newAdapterBySymptom = new CustomList(SearchPillActivity.this) {
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        LayoutInflater inflater = context.getLayoutInflater();
                        View rowView = inflater.inflate(R.layout.pill_list_item, null);

                        ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
                        TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
                        TextView pillManufacturer = (TextView) rowView.findViewById(R.id.pillManufacturer);
                        TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

                        Log.d("onClick", "position: " + position);

                        Log.d("onClick", "cold: " + cold);
                        Log.d("onClick", "digestion: " + digestion);
                        Log.d("onClick", "diarrhea: " + diarrhea);

                        Log.d("onClick", "indexOfSearch[0]: " + indexOfSearch[0]);
                        Log.d("onClick", "indexOfSearch[1]: " + indexOfSearch[1]);
                        Log.d("onClick", "indexOfSearch[2]: " + indexOfSearch[2]);

                        if(position <= indexOfSearch[position]) {
                            rowView.setVisibility(View.VISIBLE);

                            pillImage.setImageResource(pillImages[indexOfSearch[position]]);
                            pillName.setText(pillNames[indexOfSearch[position]]);
                            pillManufacturer.setText("제조사: " + pillManufacturers[indexOfSearch[position]]);
                            pillSymptom.setText(pillSymptoms[position]);

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
            super(context, R.layout.pill_list_item, pillNames);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.pill_list_item, null);

            ImageView pillImage = (ImageView) rowView.findViewById(R.id.pillImage);
            TextView pillName = (TextView) rowView.findViewById(R.id.pillName);
            TextView pillManufacturer = (TextView) rowView.findViewById(R.id.pillManufacturer);
            TextView pillSymptom = (TextView) rowView.findViewById(R.id.pillSymptom);

            pillImage.setImageResource(pillImages[position]);
            pillName.setText(pillNames[position]);
            pillManufacturer.setText("제조사: " + pillManufacturers[position]);
            pillSymptom.setText(pillSymptoms[position]);

            return rowView;
        }
    }
}