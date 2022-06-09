package com.example.taxiprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class dataactivity2 extends AppCompatActivity {
    ArrayList<String> address=new ArrayList<String>();
    ArrayList<Double> latitude=new ArrayList<Double>();
    ArrayList<Double> longitude=new ArrayList<Double>();
    AutoCompleteTextView source;
    AutoCompleteTextView destination;
    private Button buttonl;
    private Spinner spinner;
    private String [] specialist;
    private ArrayAdapter<String> arrayAdapter;
    String URL="https://taxi-priceprediction.herokuapp.com/predict";
    TextView price1,price2,price3,price4,price5,price6,price7;

    ArrayAdapter<String> s,d;

    String source_selected="";
    String destination_selected="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataactivity2);
        spinner=findViewById(R.id.spinner);
        buttonl=findViewById(R.id.buttonpredict2);
        specialist=getResources().getStringArray(R.array.suger);
        Dialog d=showdialog();

        price1=(TextView)d.findViewById(R.id.pri1);
        price2=(TextView)d.findViewById(R.id.pri2);
        price3=(TextView)d.findViewById(R.id.pri3);
        price4=(TextView)d.findViewById(R.id.pri4);
        price5=(TextView)d.findViewById(R.id.pri5);
        price6=(TextView)d.findViewById(R.id.pri6);
        source=findViewById(R.id.source1);
        destination=findViewById(R.id.destination2);

        arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,specialist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        load_data();

        s=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,address);
        source.setThreshold(1);
        destination.setThreshold(1);
        source.setAdapter(s);
        destination.setAdapter(s);

        source.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                source_selected= (String)adapterView.getItemAtPosition(i);
            }
        });

        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                destination_selected=(String)adapterView.getItemAtPosition(i);
            }
        });

        buttonl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (source_selected.equals("") || destination_selected.equals("")) {
                    Toast.makeText(getApplicationContext(), "Source or destination can not be null", Toast.LENGTH_LONG).show();

                } else if (source_selected.equals(destination_selected)) {
                    Toast.makeText(getApplicationContext(), "Source or destination can not be equal", Toast.LENGTH_LONG).show();
                } else {

                    int index1=address.indexOf(source_selected);
                    int index2=address.indexOf(destination_selected);

                    String dist =Double.toString( distance(latitude.get(index1), longitude.get(index1), latitude.get(index2), longitude.get(index2)));

                    String surg = spinner.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(), surg, Toast.LENGTH_LONG).show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                if (Double.parseDouble(surg) == 1.00) {
                                    try {
                                        price1.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    price1.setText("Unavailable");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "1.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price2.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "2.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price3.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "3.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price4.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "4.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price5.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "5.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price6.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("dist", dist);
                            params.put("surge", surg);
                            params.put("Lyft_Type", "6.0");
                            params.put("Uber_Type", "0.0");
                            params.put("cab_type_Uber", "0.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity2.this);
                    queue.add(stringRequest);

                    d.show();
                }
            }
        });



    }
    private Dialog showdialog()
    {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomdialoglyft);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialoganimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        return dialog;
    }


    private void load_data()
    {
        String line="";
        boolean fi=false;

        try {
            InputStream is=getResources().openRawResource(R.raw.boston);
            BufferedReader br=new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((line=br.readLine())!=null)
            {

                String []values=line.split(",");
                if (values[1]!=null) {
                    address.add(values[0]);
                    latitude.add(Double.parseDouble(values[1]));
                    longitude.add(Double.parseDouble(values[2]));
                }
            }
            fi=true;
            Toast.makeText(getApplicationContext(),fi+" read"+address.size(),Toast.LENGTH_LONG).show();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),fi+" read",Toast.LENGTH_LONG).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),fi+" read",Toast.LENGTH_LONG).show();
        }

    }

    private double distance(double lat1,double long1,double lat2 ,double long2)
    {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        long1 = Math.toRadians(long1);
        long2 = Math.toRadians(long2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = long2 - long1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        Toast.makeText(getApplicationContext(),String.format(Locale.US,"%2f Miles", (c * r)),Toast.LENGTH_LONG).show();
        return(c * r);


    }
}