package com.byethost31.mobpro.tugas5pmp;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends ListActivity
{
    // URL to get contacts JSON
    private static String url = "http://dthan.net/json_mhs.php";
    // JSON Node names
    private static final String TAG_STUDENTINFO = "data";
    private static final String TAG_NIM = "nim";
    private static final String TAG_NAMA = "nm_pd";
    private static final String TAG_ANGKATAN = "angkatan";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Calling async task to get json
        new GetStudents().execute();
    } /** * Async task class to get json by making HTTP call */
private class GetStudents extends AsyncTask<Void, Void, Void>
{ // Hashmap for ListView
    ArrayList<HashMap<String, String>> studentList;
    ProgressDialog pDialog;
    @Override protected void onPreExecute()
    {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false); pDialog.show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        Koneksi webreq = new Koneksi();
        // Making a request to url and getting response
        String jsonStr = webreq.makeWebServiceCall(url, Koneksi.GET);
        Log.d("Response: ", "> " + jsonStr);
        studentList = ParseJSON(jsonStr); return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /** * Updating parsed JSON data into ListView * */
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this,
                studentList,
                R.layout.list_item,
                new String[]{TAG_NAMA, TAG_NIM, TAG_ANGKATAN},
                new int[]{R.id.nama, R.id.nim, R.id.angkatan,});
        setListAdapter(adapter);
    }
} private ArrayList<HashMap<String, String>> ParseJSON(String json) {
    if (json != null) { try { // Hashmap for ListView
        ArrayList<HashMap<String, String>>
                studentList = new ArrayList<HashMap<String, String>>();
        JSONObject jsonObj = new JSONObject(json);
// Getting JSON Array node
        JSONArray students = jsonObj.getJSONArray(TAG_STUDENTINFO);
        // looping through All Students
        for (int i = 0; i < students.length(); i++)
        {
            JSONObject c = students.getJSONObject(i);
            String nama = "Nama : " + c.getString(TAG_NAMA);
            String nim = "NIM : " + c.getString(TAG_NIM);
            String angkatan = "Angkatan : " + c.getString(TAG_ANGKATAN);
            // tmp hashmap for single student
            HashMap<String, String> student = new HashMap<String, String>();
            // adding each child node to HashMap key => value
            student.put(TAG_NAMA, nama);
            student.put(TAG_NIM, nim);
            student.put(TAG_ANGKATAN, angkatan);
            // adding student to students list
            studentList.add(student); }
        return studentList;
    } catch (JSONException e)
    { e.printStackTrace();
        return null; }
    } else
    { Log.e("ServiceHandler", "Couldn't get any data from the url");
        return null; } }
}