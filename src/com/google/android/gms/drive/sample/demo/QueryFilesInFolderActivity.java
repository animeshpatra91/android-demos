
package com.google.android.gms.drive.sample.demo;

import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveApi.MetadataBufferResult;
import com.google.android.gms.drive.DriveFolder.OnChildrenRetrievedCallback;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import android.os.Bundle;
import android.widget.ListView;

/**
 * An activity that illustrates how to query files in a folder. For an example
 * of pagination and displaying results, please see {@link ListFilesActivity}.
 */
public class QueryFilesInFolderActivity extends BaseDemoActivity implements
        OnChildrenRetrievedCallback {

    private static DriveId sFolderId = DriveId.createFromResourceId("0B2EEtIjPUdX6MERsWlYxN3J6RU0");

    private ListView mResultsListView;
    private ResultsAdapter mResultsAdapter;

    @Override
    public void onConnected(Bundle connectionHint) {
        super.onCreate(connectionHint);
        setContentView(R.layout.activity_listfiles);
        mResultsListView = (ListView) findViewById(R.id.listViewResults);
        mResultsAdapter = new ResultsAdapter(this);
        mResultsListView.setAdapter(mResultsAdapter);
        DriveFolder folder = Drive.DriveApi.getFolder(getGoogleApiClient(), sFolderId);
        Query query = new Query.Builder()
                .addFilter(Filters.eq(SearchableField.MIME_TYPE, "text/plain"))
                .build();
        folder.queryChildren(getGoogleApiClient(), query).addResultCallback(this);
    }

    @Override
    public void onChildrenRetrieved(MetadataBufferResult result) {
        if (!result.getStatus().isSuccess()) {
            showMessage("Problem while retrieving files");
            return;
        }
        mResultsAdapter.clear();
        mResultsAdapter.append(result.getMetadataBuffer());
        showMessage("Successfully listed files.");
    }

}
