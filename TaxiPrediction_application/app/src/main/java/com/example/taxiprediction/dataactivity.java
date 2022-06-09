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

public class dataactivity extends AppCompatActivity {

    ArrayList<String> address=new ArrayList<String>();
    ArrayList<Double> latitude=new ArrayList<Double>();
    ArrayList<Double> longitude=new ArrayList<Double>();
    Button button;
    AutoCompleteTextView source;
    AutoCompleteTextView destination;
    TextView price1,price2,price3,price4,price5,price6,price7,dest;

    ArrayAdapter<String> s,d;

    String source_selected="";
    String destination_selected="";

    double distance_eq=0.0;

    String URL="https://taxi-priceprediction.herokuapp.com/predict";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataactivity);
        button=findViewById(R.id.buttonpredict);
        source=findViewById(R.id.source1);
        destination=findViewById(R.id.destination2);


        load_data();


        Dialog d=showdialog();

        price1=(TextView)d.findViewById(R.id.price1);
        price2=(TextView)d.findViewById(R.id.price2);
        price3=(TextView)d.findViewById(R.id.price3);
        price4=(TextView)d.findViewById(R.id.price4);
        price5=(TextView)d.findViewById(R.id.price5);
        price6=(TextView)d.findViewById(R.id.price6);
        price7=(TextView)d.findViewById(R.id.price7);

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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (source_selected.equals("") || destination_selected.equals("")) {
                    Toast.makeText(getApplicationContext(), "Source or destination can not be null", Toast.LENGTH_LONG).show();

                } else if (source_selected.equals(destination_selected)) {
                    Toast.makeText(getApplicationContext(), "Source or destination can not be equal", Toast.LENGTH_LONG).show();
                } else {


                    int index1=address.indexOf(source_selected);
                    int index2=address.indexOf(destination_selected);

                    distance_eq=distance(latitude.get(index1), longitude.get(index1), latitude.get(index2), longitude.get(index2));


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {


                                    price1.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
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
                            params.put("dist",Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "1.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    RequestQueue queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price2.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();

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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "2.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    //incresse 2 to the actual price
                                    price3.setText(String.format("%.2f$-", Double.parseDouble(data)) + String.format("%.2f$", Double.parseDouble(data) + 4.0));
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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "3.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price4.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();

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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "4.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price5.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "5.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price6.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "6.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
                    queue.add(stringRequest);

                    stringRequest = new StringRequest(Request.Method.POST, URL
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("price");
                                try {
                                    price7.setText(String.format("%.2f$-", Double.parseDouble(data)-2.0) + String.format("%.2f$", Double.parseDouble(data) + 2.0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();

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
                            params.put("dist", Double.toString(distance_eq));
                            params.put("surge", "1.00");
                            params.put("Lyft_Type", "0.0");
                            params.put("Uber_Type", "7.0");
                            params.put("cab_type_Uber", "1.0");
                            return params;
                        }
                    };

                    queue = Volley.newRequestQueue(dataactivity.this);
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
            dialog.setContentView(R.layout.bottomdialog);

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