package com.sanoj.devildeveloper.enoughmovies.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import com.sanoj.devildeveloper.enoughmovies.R;

public final class PermissionUtility {
	public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA = 1;
	public static final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
	public static final int REQUEST_PERMISSION_ACCESS_LOCATION = 3;

	private PermissionUtility() {}

	public static boolean checkPermissionReadExternalStorageAndCamera(final Context fragment) {
		return check(fragment,
				new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
				new int[]{R.string.permission_read_external_storage, R.string.permission_camera},
				REQUEST_PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA);
	}

	public static boolean checkPermissionWriteExternalStorage(final Context fragment) {
		return check(fragment,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				R.string.permission_write_external_storage,
				REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE);
	}

	public static boolean checkPermissionAccessLocation(final Context fragment) {
		return check(fragment,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
				new int[]{R.string.permission_access_location, R.string.permission_access_location},
				REQUEST_PERMISSION_ACCESS_LOCATION);
	}

	private static boolean check(final Context fragment, final String permission, final int explanation, final int requestCode) {
		// check permission
		final int result = ContextCompat.checkSelfPermission(fragment, permission);

		// ask for permission
		if (result != PackageManager.PERMISSION_GRANTED) {
			// check if explanation is needed
			if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) fragment,permission)) {
				// show explanation
				Snackbar
						.make(((Activity) fragment).getWindow().getDecorView(), explanation, Snackbar.LENGTH_INDEFINITE)
						.setAction(android.R.string.ok, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								// try again
								ActivityCompat.requestPermissions((Activity) fragment,new String[]{permission}, requestCode);
							}
						}).show();
			} else {
				// no explanation needed
				ActivityCompat.requestPermissions((Activity) fragment,new String[]{permission}, requestCode);
			}
		}

		// result
		return result == PackageManager.PERMISSION_GRANTED;
	}

	private static boolean check(final Context fragment, final String[] permissions, final int[] explanations, final int requestCode) {
		// check permissions
		final int[] results = new int[permissions.length];
		for (int i = 0; i < permissions.length; i++) {
			results[i] = ContextCompat.checkSelfPermission(fragment, permissions[i]);
		}

		// get denied permissions
		final List<String> deniedPermissions = new ArrayList<>();
		for (int i = 0; i < results.length; i++) {
			if (results[i] != PackageManager.PERMISSION_GRANTED) {
				deniedPermissions.add(permissions[i]);
			}
		}

		// ask for permissions
		if (!deniedPermissions.isEmpty()) {
			final String[] params = deniedPermissions.toArray(new String[deniedPermissions.size()]);

			// check if explanation is needed
			boolean explanationShown = false;
			for (int i = 0; i < permissions.length; i++) {
				if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) fragment,permissions[i])) {
					// show explanation
					Snackbar
							.make(((Activity) fragment).getWindow().getDecorView(), explanations[i], Snackbar.LENGTH_INDEFINITE)
							.setAction(android.R.string.ok, new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// try again
									ActivityCompat.requestPermissions((Activity) fragment,params, requestCode);
								}
							}).show();
					explanationShown = true;
					break;
				}
			}

			// no explanation needed
			if (!explanationShown) {
				ActivityCompat.requestPermissions((Activity) fragment,params, requestCode);
			}
		}

		// result
		return deniedPermissions.isEmpty();
	}
}
