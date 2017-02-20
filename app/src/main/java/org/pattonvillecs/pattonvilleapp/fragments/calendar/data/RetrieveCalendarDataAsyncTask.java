package org.pattonvillecs.pattonvilleapp.fragments.calendar.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.HashMultimap;

import net.fortuna.ical4j.model.component.VEvent;

import org.pattonvillecs.pattonvilleapp.DataSource;
import org.pattonvillecs.pattonvilleapp.PattonvilleApplication;
import org.pattonvillecs.pattonvilleapp.fragments.calendar.fix.SerializableCalendarDay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.pattonvillecs.pattonvilleapp.fragments.calendar.data.CalendarDownloadAndParseTask.parseFile;

/**
 * Created by Mitchell Skaggs on 1/25/2017.
 */

public class RetrieveCalendarDataAsyncTask extends AsyncTask<DataSource, Double, HashMultimap<SerializableCalendarDay, VEvent>> {

    private static final String TAG = RetrieveCalendarDataAsyncTask.class.getSimpleName();
    private PattonvilleApplication pattonvilleApplication;
    private Kryo kryo;
    private DataSource dataSource;

    public RetrieveCalendarDataAsyncTask(PattonvilleApplication pattonvilleApplication) {
        this.pattonvilleApplication = pattonvilleApplication;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.kryo = pattonvilleApplication.borrowKryo();
        pattonvilleApplication.getRunningCalendarAsyncTasks().add(this);
        pattonvilleApplication.updateCalendarListeners();
    }

    @Override
    protected HashMultimap<SerializableCalendarDay, VEvent> doInBackground(DataSource... params) {
        this.dataSource = params[0];

        Log.i(TAG, "Getting calendar for " + dataSource.shortName);

        NetworkInfo networkInfo = ((ConnectivityManager) pattonvilleApplication.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean hasInternet = networkInfo != null && networkInfo.isConnected();

        // Begin caching operations.

        File calendarDataCache = new File(this.pattonvilleApplication.getCacheDir(), dataSource.shortName + ".bin");

        boolean cacheExists = calendarDataCache.exists();
        long cacheAge = System.currentTimeMillis() - calendarDataCache.lastModified();
        boolean cacheIsYoung = TimeUnit.HOURS.convert(cacheAge, TimeUnit.MILLISECONDS) < 24 * 7; // One week expiration

        if (cacheExists && (cacheIsYoung || !hasInternet)) {
            //Attempt to load the cache
            boolean isCacheCorrupt;
            HashMultimap<SerializableCalendarDay, VEvent> calendarData = null;

            Input input = null;
            try {
                input = new Input(new FileInputStream(calendarDataCache));

                //noinspection unchecked
                calendarData = this.kryo.readObject(input, HashMultimap.class);
                isCacheCorrupt = false;
            } catch (FileNotFoundException e) {
                Log.wtf(TAG, "This should never happen. The file should already be checked to exist before opening.");
                isCacheCorrupt = true;
            } catch (Exception e) {
                Log.e(TAG, "Other error thrown! Needs investigation!");
                isCacheCorrupt = true;
                e.printStackTrace();
            } finally {
                if (input != null) {
                    //noinspection ThrowFromFinallyBlock
                    input.close();
                }
            }

            if (!isCacheCorrupt) {
                assert calendarData != null;

                Log.i(TAG, "Got calendar for " + dataSource.shortName);
                return calendarData;
            }
        }

        //End caching operations. Resort to redownloading and parsing calendars if connected.

        if (hasInternet) {
            //Add a request
            RequestFuture<String> requestFuture = RequestFuture.newFuture();
            StringRequest request = new StringRequest(dataSource.calendarURL, requestFuture, requestFuture);
            request.setRetryPolicy(new DefaultRetryPolicy(3000, 10, 1.3f));
            pattonvilleApplication.getRequestQueue().add(request);

            //Wait for the request
            String result = null;
            boolean downloadSucceeded;
            try {
                result = requestFuture.get(5, TimeUnit.MINUTES);
                downloadSucceeded = true;
            } catch (InterruptedException e) {
                Log.e(TAG, "Thread interrupted!");
                downloadSucceeded = false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                downloadSucceeded = false;
            } catch (TimeoutException e) {
                Log.e(TAG, "Download timed out!");
                downloadSucceeded = false;
            }

            //Continue if the request succeeded
            if (downloadSucceeded) {
                assert result != null;

                //Apply fix for iCal format
                String processedResult = CalendarDownloadAndParseTask.fixICalStrings(result);

                HashMultimap<SerializableCalendarDay, VEvent> calendarData = parseFile(processedResult);

                Output output = null;
                try {
                    output = new Output(new FileOutputStream(calendarDataCache));

                    this.kryo.writeObject(output, calendarData);

                    if (!calendarDataCache.setLastModified(System.currentTimeMillis()))
                        Log.e(TAG, "Failed to set last modified time!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        //noinspection ThrowFromFinallyBlock
                        output.close();
                    }
                }

                Log.i(TAG, "Got calendar for " + dataSource.shortName);
                return calendarData;
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
    }

    @Override
    protected void onPostExecute(HashMultimap<SerializableCalendarDay, VEvent> result) {
        releaseKryo();
        if (result != null) {
            pattonvilleApplication.getCalendarData().put(dataSource, result);
        }
        Log.i(TAG, "Removing from size: " + pattonvilleApplication.getRunningCalendarAsyncTasks().size());
        pattonvilleApplication.getRunningCalendarAsyncTasks().remove(this);
        Log.i(TAG, "Now size: " + pattonvilleApplication.getRunningCalendarAsyncTasks().size());
        pattonvilleApplication.updateCalendarListeners();
    }

    @Override
    protected void onCancelled(HashMultimap<SerializableCalendarDay, VEvent> result) {
        releaseKryo();
        pattonvilleApplication.getRunningCalendarAsyncTasks().remove(this);
        pattonvilleApplication.updateCalendarListeners();
    }

    private void releaseKryo() {
        pattonvilleApplication.releaseKryo(this.kryo);
    }
}