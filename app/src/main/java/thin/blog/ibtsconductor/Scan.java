package thin.blog.ibtsconductor;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import datasets.Travel;
import datasets.User;
import network.CustomRequest;
import network.VolleySingleton;

import static android.content.Context.MODE_PRIVATE;
import static thin.blog.ibtsconductor.ApplicationHelper.readFromSharedPreferences;
import static thin.blog.ibtsconductor.ApplicationHelper.writeToSharedPreferences;

/**
 * Main Fragment which takes care of Entering Details for User Travel
 */
public class Scan extends Fragment {
    @Bind(R.id.spinner_bus)
    AppCompatSpinner busSpinner;
    @Bind(R.id.spinner_stop)
    AppCompatSpinner stopSpinner;
    @Bind(R.id.number_of_passengers)
    EditText numberOfPassengers;
    boolean busSelected, stopSelected;
    List<String> busArrayList = new ArrayList<>();
    List<String> stopArrayList = new ArrayList<>();
    ArrayAdapter busListAdapter;
    ArrayAdapter<String> stopListAdapter;
    ProgressDialog busProgressDialog;
    ProgressDialog stopProgressDialog;
    List<Travel> travelLinkedList = new ArrayList<>();
    String currentStop;


    public Scan() {
    }

    public static Scan newInstance() {
        return new Scan();
    }

    @OnItemSelected(R.id.spinner_bus)
    void onBusSpinnerItemSelected(int position) {
        busSelected = true;
        writeToSharedPreferences(Constants.SHARED_PREFS_CURRENT_BUS_NAME, busArrayList.get(position));
        stopSpinner.setVisibility(View.INVISIBLE);

        final RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        Map<String, String> formData = new HashMap<>();
        formData.put("busname", busArrayList.get(position));
        final CustomRequest request = new CustomRequest(Request.Method.POST, Constants.BUS_DETAILS, formData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                stopListJsonParser(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog.dismiss();
                Snackbar.make(busSpinner, "Network Error", Snackbar.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
        stopProgressDialog = new ProgressDialog(getActivity());
        stopProgressDialog.setIndeterminate(true);
        stopProgressDialog.setCancelable(false);
        stopProgressDialog.setTitle("Contacting Server");
        stopProgressDialog.setMessage("Fetching Stop Names List");
        stopProgressDialog.show();

    }

    private void stopListJsonParser(JSONObject response) {
        int serverSuccess;
        try {
            serverSuccess = response.getInt("status");
            if (serverSuccess == 1) {
                stopArrayList.clear();
                if (stopListAdapter != null) {
                    stopListAdapter.notifyDataSetChanged();
                }
                JSONArray stopNameArray = response.getJSONArray("list");
                for (int i = 0; i < stopNameArray.length(); i++) {
                    stopArrayList.add(stopNameArray.getString(i));
                    stopListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stopArrayList);
                    stopListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stopSpinner.setAdapter(stopListAdapter);
                    stopSpinner.setVisibility(View.VISIBLE);
                    stopProgressDialog.dismiss();
                    busSelected = true;
                    serverSuccess = 0;
                }
            } else if (serverSuccess == 0) {
                busSelected = false;
                stopProgressDialog.dismiss();
                Snackbar.make(busSpinner, "Select Some other Bus", Snackbar.LENGTH_SHORT).show();
            } else {
                stopProgressDialog.dismiss();
                stopSelected = false;
                Snackbar.make(busSpinner, "Cannot Fetch Stop details", Snackbar.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnItemSelected(R.id.spinner_stop)
    void onStopSpinnerItemSelected(int position) {
        stopSelected = true;
        writeToSharedPreferences(Constants.SHARED_PREFS_CURRENT_STOP_NAME, stopArrayList.get(position));
        currentStop = stopArrayList.get(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    void doRefresh() {
        busProgressDialog = new ProgressDialog(getActivity());
        busProgressDialog.setIndeterminate(true);
        busProgressDialog.setCancelable(false);
        busProgressDialog.setTitle("Contacting Server");
        busProgressDialog.setMessage("Fetching Bus Names List");

        final RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        final CustomRequest request = new CustomRequest(Request.Method.POST, Constants.BUS_LIST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                busListJsonParser(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                busProgressDialog.dismiss();
                Snackbar.make(busSpinner, "Network Error", Snackbar.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
        busProgressDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        writeToSharedPreferences(Constants.SHARED_PREFS_NUMBER_OF_PASSENGERS, numberOfPassengers.getText().toString());
        writeToSharedPreferences(Constants.SHARED_PREFS_BUS_SELECTION, String.valueOf(busSpinner.getSelectedItemPosition()));
        writeToSharedPreferences(Constants.SHARED_PREFS_STOP_SELECTION, String.valueOf(stopSpinner.getSelectedItemPosition()));
        Gson gson = new Gson();
        Type travelType = new TypeToken<List<Travel>>() {
        }.getType();
        String travelList = gson.toJson(travelLinkedList, travelType);
        writeToSharedPreferences(Constants.SHARED_PREFS_TRAVEL_LIST, travelList);
        Type stringType = new TypeToken<List<String>>() {
        }.getType();
        String busList = gson.toJson(busArrayList, stringType);
        writeToSharedPreferences("BUS_LIST", busList);
        String stopList = gson.toJson(stopArrayList, stringType);
        writeToSharedPreferences("STOP_LIST", stopList);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS_USER_DATA, MODE_PRIVATE);
        String busListJson = sharedPreferences.getString(Constants.SHARED_PREFS_BUS_LIST, null);
        if (busListJson == null || busListJson.contentEquals("null") || busListJson.contentEquals("")) {
            doRefresh();
            return;
        }
        Gson gson = new Gson();
        Type travelType = new TypeToken<List<Travel>>() {
        }.getType();
        String travelJson = readFromSharedPreferences(Constants.SHARED_PREFS_TRAVEL_LIST, "");
        travelLinkedList = gson.fromJson(travelJson, travelType);
        Type stringType = new TypeToken<List<String>>() {
        }.getType();

        busArrayList = gson.fromJson(busListJson, stringType);
        String stopListJson = readFromSharedPreferences(Constants.SHARED_PREFS_STOP_LIST, "");
        stopArrayList = gson.fromJson(stopListJson, stringType);

        busListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busArrayList);
        busListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busSpinner.setAdapter(busListAdapter);

        stopListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, stopArrayList);
        stopListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopSpinner.setAdapter(stopListAdapter);
        stopSpinner.setVisibility(View.VISIBLE);

        numberOfPassengers.setText(readFromSharedPreferences(Constants.SHARED_PREFS_NUMBER_OF_PASSENGERS, "1"));
        busSpinner.setSelection(Integer.parseInt(readFromSharedPreferences(Constants.SHARED_PREFS_BUS_SELECTION, "0")));
        stopSpinner.setSelection(Integer.parseInt(readFromSharedPreferences(Constants.SHARED_PREFS_STOP_SELECTION, "0")));
    }

    private void busListJsonParser(JSONObject response) {
        int serverSuccess;
        try {
            serverSuccess = response.getInt("status");
            if (serverSuccess == 1) {
                stopArrayList.clear();
                busArrayList.clear();
                JSONArray busNameArray = response.getJSONArray("list");
                for (int i = 0; i < busNameArray.length(); i++) {
                    busArrayList.add(busNameArray.getString(i));
                    busListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, busArrayList);
                    busListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    busSpinner.setAdapter(busListAdapter);
                    busProgressDialog.dismiss();
                    busSelected = true;
                    serverSuccess = 0;
                }
            } else {
                busProgressDialog.dismiss();
                Snackbar.make(busSpinner, "Cannot Fetch Bus details", Snackbar.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        String passengers = readFromSharedPreferences(Constants.SHARED_PREFS_NUMBER_OF_PASSENGERS, "1");
        numberOfPassengers.setText(passengers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    void processQRCodeData(String data) {
        int userId = getUserIdFromQRCodeData(data);
        processTravel(userId);

    }

    private void processTravel(int userId) {
        int passengerCount = Integer.parseInt(numberOfPassengers.getText().toString());
        Travel current = new Travel(userId, passengerCount);
        if (travelLinkedList.contains(current)) {
            int index = travelLinkedList.indexOf(current);
            Travel fromList = travelLinkedList.get(index);
            current.setStartingStop(fromList.getStartingStop());
            Toast.makeText(getActivity(), "Contains", Toast.LENGTH_LONG).show();
            current.setEndingStop(currentStop);
            travelLinkedList.remove(index);
            travelLinkedList.add(index, current);
            deductAmount(current, index);
        } else {
            current.setStartingStop(currentStop);
            travelLinkedList.add(current);
        }
    }

    private void deductAmount(Travel current, final int index) {
        String string = readFromSharedPreferences(Constants.USER_DATA_OBJECT, "");
        User user = User.getUserObject(string);
        int counductorId = user.getUserId();
        String busName = readFromSharedPreferences(Constants.SHARED_PREFS_CURRENT_BUS_NAME, "");
        final RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        Map<String, String> formData = new HashMap<>();
        formData.put("user_id", String.valueOf(current.getUserId()));
        formData.put("conductor_id", String.valueOf(counductorId));
        formData.put("busname", busName);
        formData.put("starting_stop", current.getStartingStop());
        formData.put("ending_stop", current.getEndingStop());
        formData.put("number_of_passengers", String.valueOf(current.getNumberOfPassengers()));
        final CustomRequest request = new CustomRequest(Request.Method.POST, Constants.TRAVEL, formData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deductAmountJsonParser(response, index);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopProgressDialog.dismiss();
                Snackbar.make(busSpinner, "Network Error", Snackbar.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        stopProgressDialog = new ProgressDialog(getActivity());
        stopProgressDialog.setIndeterminate(true);
        stopProgressDialog.setCancelable(false);
        stopProgressDialog.setTitle("Contacting Server");
        stopProgressDialog.setMessage("Getting Travel Details");
        stopProgressDialog.show();

    }

    private void deductAmountJsonParser(JSONObject response, int index) {
        int serverSuccess;
        try {
            serverSuccess = response.getInt("status");
            if (serverSuccess == 1) {
                int distance = response.getInt("distance");
                int amount = response.getInt("amount");
                stopProgressDialog.dismiss();
                travelLinkedList.remove(index);
                CustomAlertDialogTravel dialogTravel = new CustomAlertDialogTravel(getActivity(), R.drawable.icon_done, "Transaction Successful\nDistance: " + distance + " km\nAmount: ₹" + amount);
                dialogTravel.show();
            } else if (serverSuccess == 0) {
                String message = response.getString("message");
                stopProgressDialog.dismiss();
                travelLinkedList.remove(index);
                CustomAlertDialogTravel dialogTravel = new CustomAlertDialogTravel(getActivity(), R.drawable.icon_error, message);
                dialogTravel.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private int getUserIdFromQRCodeData(String data) {
        int userId;
        userId = Integer.parseInt(data.substring(0, data.indexOf("/")));
        return userId;
    }
}
