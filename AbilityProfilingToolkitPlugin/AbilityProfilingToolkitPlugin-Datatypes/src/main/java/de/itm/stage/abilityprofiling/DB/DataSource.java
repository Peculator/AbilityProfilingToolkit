package de.itm.stage.abilityprofiling.DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DataSource {
	
	public final String TAG = this.getClass().getSimpleName();

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
    private boolean isReady = false;

    private Context c;
	private String[] allAbilityColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ICF_CODE, MySQLiteHelper.COLUMN_TITLE,
			MySQLiteHelper.COLUMN_DESCRIPTION };

	private String[] allProfileColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_ICF_CODE, MySQLiteHelper.COLUMN_APP_NAME,
			MySQLiteHelper.COLUMN_RATING, MySQLiteHelper.COLUMN_TIMESTAMP };

	private String[] allPermissionColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_APP_NAME,
			MySQLiteHelper.COLUMN_PERMISSION_STATE };

	public DataSource(Context context) {

        //context.getApplicationContext().deleteDatabase("abilityprofilingtoolkit.db");
        //Log.i(TAG,"deleted");

		dbHelper = new MySQLiteHelper(context);
        c = context;

	}

	public boolean open() throws SQLException {
		database = dbHelper.getWritableDatabase();

        if (getAllAbilities().size() < 1) {
            Log.i(TAG, "Writing abilities");
            Resources res = c.getResources();

            //InputStream in_s = res.openRawResource(R.raw.combined);

            String file = "raw/combined.txt"; // res/raw/test.txt also work.
            InputStream in_s = this.getClass().getClassLoader().getResourceAsStream(file);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in_s));
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {

                int firstStop = line.indexOf('-');
                int secondStop = line.indexOf(':');
                String title = "";
                String description = "-";

                // Store de.itm.stage.abilityprofiling.ICF code
                String code = line.substring(0, firstStop)
                        .trim();

                if (secondStop > 0) {
                    // Store title
                    title = line.substring(firstStop + 1,
                            secondStop).trim();
                    // Store Description
                    description = line
                            .substring(secondStop + 1).trim();
                } else {
                    title = line.substring(firstStop + 1)
                            .trim();
                }

                //Log.i(TAG, code);
                createAbility(new Ability(code,
                        title, description));

                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "Database filled");
            isReady = true;


            Permission p = new Permission();
            p.setPermissionState(Permission.PermissionStates.ADMIN.toString());
            p.setAppName("AbilityProfilingToolkitManager");
            createPermission(p);

        }else{
            isReady = true;
        }
        return true;
	}

    public boolean isReady(){
        return isReady;
    }

	public void close() {
		dbHelper.close();
	}

	// ------------- Drop all tables -------------

	public void dropAllTables() {
		dropAbilityTable();
		dropProfileTable();
		dropPermissionTable();

	}

    public void resetDataBase() {
        dropProfileTable();
        dropPermissionTable();
        Permission p = new Permission();
        p.setPermissionState(Permission.PermissionStates.ADMIN.toString());
        p.setAppName("AbilityProfilingToolkitManager");
        createPermission(p);

    }

	// ------------- Dropping the tables for abilities, profiles or permissions
	// -------------


	public void dropAbilityTable() {
		//database.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_ABILITY);
		//database.delete(MySQLiteHelper.TABLE_ABILITY, null, null);
        database.delete(MySQLiteHelper.TABLE_ABILITY, null, null);
	}

	public void dropPermissionTable() {
		//database.execSQL("DROP TABLE IF EXISTS "
				//+ MySQLiteHelper.TABLE_PERMISSION);
        database.delete(MySQLiteHelper.TABLE_PERMISSION, null, null);
	}

	public void dropProfileTable() {
		//database.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_PROFILE);
        database.delete(MySQLiteHelper.TABLE_PROFILE, null, null);
	}

	// ------------- Creating abilities, profiles or permissions -------------

	public Profile createProfile(Profile p) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ICF_CODE, p.getICFCode());
		values.put(MySQLiteHelper.COLUMN_APP_NAME, p.getAppName());
		values.put(MySQLiteHelper.COLUMN_RATING, p.getRating());
		values.put(MySQLiteHelper.COLUMN_TIMESTAMP, p.getTimeStamp());

		long insertId = database.insert(MySQLiteHelper.TABLE_PROFILE, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILE,
				allProfileColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);


		cursor.moveToFirst();
		Profile newProfile = cursorToProfile(cursor);
		cursor.close();

		return newProfile;
	}

	public Ability createAbility(Ability a) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_ICF_CODE, a.getICFCode());
		values.put(MySQLiteHelper.COLUMN_TITLE, a.getAbilityTitle());
		values.put(MySQLiteHelper.COLUMN_DESCRIPTION, a.getAbilityDescription());

		long insertId = database.insert(MySQLiteHelper.TABLE_ABILITY, null,
				values);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ABILITY,
				allAbilityColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);

		cursor.moveToFirst();
		Ability newAbility = cursorToAbility(cursor);
		cursor.close();

		return newAbility;
	}

	public Permission createPermission(Permission p) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_APP_NAME, p.getAppName());
		values.put(MySQLiteHelper.COLUMN_PERMISSION_STATE,
				p.getPermissionState().toString());

		long insertId = database.insertWithOnConflict(MySQLiteHelper.TABLE_PERMISSION, null,
                values, SQLiteDatabase.CONFLICT_REPLACE);

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PERMISSION,
				allPermissionColumns, MySQLiteHelper.COLUMN_ID + " = "
						+ insertId, null, null, null, null);

		cursor.moveToFirst();
		Permission newPermission = cursorToPermission(cursor);
		cursor.close();

		return newPermission;
	}

	// ------------- Deleting abilities, profiles or permissions -------------

	public void deleteProfile(Profile p) {
		long id = p.getId();
		System.out.println("Profile deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_PROFILE, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public void deletePermission(Permission p) {
		long id = p.getId();
		System.out.println("Permission deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_PERMISSION,
				MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	public void deleteAbility(Ability a) {
		long id = a.getId();
		System.out.println("Ability deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_ABILITY, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	// ------------- Get all tables, abilities, profiles or permissions -------------
    public List<ResponseType> getAllTables() {
        List<ResponseType> response = new ArrayList<ResponseType>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ABILITY,
                allAbilityColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ability ability = cursorToAbility(cursor);
            response.add(ability);
            cursor.moveToNext();
        }

        cursor = database.query(MySQLiteHelper.TABLE_PROFILE,
                allProfileColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Profile profile = cursorToProfile(cursor);
            response.add(profile);
            cursor.moveToNext();
        }

        cursor = database.query(MySQLiteHelper.TABLE_PERMISSION,
                allPermissionColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Permission permission = cursorToPermission(cursor);
            response.add(permission);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        return response;
    }


	public List<Ability> getAllAbilities() {
		List<Ability> abilities = new ArrayList<Ability>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ABILITY,
				allAbilityColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Ability ability = cursorToAbility(cursor);
			abilities.add(ability);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return abilities;
	}

	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILE,
				allProfileColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Profile profile = cursorToProfile(cursor);
			profiles.add(profile);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return profiles;
	}

	public List<Permission> getAllPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PERMISSION,
				allPermissionColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Permission permission = cursorToPermission(cursor);
			permissions.add(permission);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return permissions;
	}

	// ----- Get a single ability by icfcode, profiles by icfcode or a
	// permission by appname ---------

	public Ability getAbilityByCode(String icfCode) {
		icfCode = icfCode.replace(" ","");
		Ability a = new Ability();
		String[] selectionArgs = { icfCode };

		Cursor cursor = database.query(MySQLiteHelper.TABLE_ABILITY,
				allAbilityColumns, MySQLiteHelper.COLUMN_ICF_CODE+"=?",
				selectionArgs, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Ability ability = cursorToAbility(cursor);
			a = ability;
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return a;
	}

	public Permission getPermissionByAppname(String appname) {
		Permission a = null;
		String[] selectionArgs = { appname };

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PERMISSION,
				allPermissionColumns, MySQLiteHelper.COLUMN_APP_NAME+"=?",
				selectionArgs, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Permission permission = cursorToPermission(cursor);
			a = permission;
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return a;
	}

	public List<Profile> getProfilesByCode(String icfCode) {
		icfCode = icfCode.replace(" ","");
		List<Profile> profiles = new ArrayList<Profile>();

		String[] selectionArgs = { icfCode };

		Cursor cursor = database.query(MySQLiteHelper.TABLE_PROFILE,
				allProfileColumns, MySQLiteHelper.COLUMN_ICF_CODE+"=?",
				selectionArgs, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Profile profile = cursorToProfile(cursor);
			profiles.add(profile);
			cursor.moveToNext();
		}

		// make sure to close the cursor
		cursor.close();
		return profiles;
	}

	// -------------- Advanced Requests --------------
	// -------------- Get Profiles by de.itm.stage.abilityprofiling.ICF and depth (0-4) --------------
	/*
	 * Cases: search for b1 and depth 0 --> you only get b1 search for b1 and
	 * depth 1 --> you get b1 and b10 - b19 search for b1 and depth 2 --> you
	 * get b1 and b10 - b199 search for b1 and depth 3 --> you get b1 and b10 -
	 * b1999 search for b1 and depth 4 --> you get b1 and b10 - b19999
	 *
	 * search for b45 and depth 2 --> you get b45 and b450 - b4599
	 *
	 * search for b458 and depth 2 --> you get b458 and b4580 - b45899
	 */

	public List<Profile> getProfilesByCode(String icfCode, int depthtolerance) {
		List<Profile> result = new ArrayList<Profile>();

		icfCode = icfCode.replace(" ","");

		int icf_length = icfCode.length();

		for (Profile profile : getAllProfiles()) {
			Log.i(TAG, profile.getICFCode().subSequence(0, icf_length) + " - " + icfCode);
			if (profile.getICFCode().length() - depthtolerance <= icf_length
					&& profile.getICFCode().subSequence(0, icf_length).equals(icfCode)) {

				result.add(profile);
			}
		}

		return result;
	}

	// -------------- Get Profiles by de.itm.stage.abilityprofiling.ICF and depth (0-4) and
	// StartTime--------------

	public List<Profile> getProfilesByCode(String icfCode, int depthtolerance,
			long starttime) {
		icfCode = icfCode.replace(" ","");
		List<Profile> result = new ArrayList<Profile>();

		int icf_length = icfCode.length();

		for (Profile profile : getAllProfiles()) {

			if (profile.getICFCode().length() - depthtolerance <= icf_length
					&& profile.getICFCode().subSequence(0, icf_length) == icfCode
					&& starttime < profile.getTimeStamp()) {
				result.add(profile);
			}
		}

		return result;
	}

	// -------------- Get Profiles by de.itm.stage.abilityprofiling.ICF and depth (0-4) and StartTime and
	// Endtime --------------
	public List<Profile> getProfilesByCode(String icfCode, int depthtolerance,
			long starttime, long endtime) {
		icfCode = icfCode.replace(" ","");
		List<Profile> result = new ArrayList<Profile>();

		int icf_length = icfCode.length();

		for (Profile profile : getAllProfiles()) {

			if (profile.getICFCode().length() - depthtolerance <= icf_length
					&& profile.getICFCode().subSequence(0, icf_length) == icfCode
					&& starttime < profile.getTimeStamp()
					&& endtime > profile.getTimeStamp()) {
				result.add(profile);
			}
		}

		return result;
	}

	// -------------- Get related Profiles by de.itm.stage.abilityprofiling.ICF and depth (0-4) in a time-slot
	// --------------
	public List<Profile> getRelatedProfilesByCode(String icfCode,
			int depthtolerance, long refTime, long timeDistance) {
		icfCode = icfCode.replace(" ","");
		List<Profile> result = new ArrayList<Profile>();

		int icf_length = icfCode.length();

		for (Profile profile : getAllProfiles()) {

			if (profile.getICFCode().length() - depthtolerance <= icf_length
					&& profile.getICFCode().subSequence(0, icf_length) == icfCode
					&& refTime - timeDistance / 2 < profile.getTimeStamp()
					&& refTime + timeDistance / 2 > profile.getTimeStamp()) {
				result.add(profile);
			}
		}

		return result;
	}

	// -------------- Get related Profiles by profile and depth (0-4) in a
	// time-slot
	// --------------
	public List<Profile> getRelatedProfilesByProfile(Profile p, long timeDistance) {

		List<Profile> result = new ArrayList<Profile>();

		for (Profile profile : getAllProfiles()) {

			if (profile != p
					&& profile.getTimeStamp() - timeDistance < p.getTimeStamp()
					&& profile.getTimeStamp() + timeDistance > p.getTimeStamp()) {
				result.add(profile);
			}
		}

		return result;
	}

	// ------------- Filling the abilities, profiles or permissions dynamically
	// -------------

	private Profile cursorToProfile(Cursor cursor) {
		Profile profile = new Profile();
		profile.setId(cursor.getLong(0));
		profile.setICFCode(cursor.getString(1));
		profile.setAppName(cursor.getString(2));
		profile.setRating(cursor.getString(3));
		profile.setTimeStamp(cursor.getLong(4));

		return profile;
	}

	private Permission cursorToPermission(Cursor cursor) {
		Permission permission = new Permission();
		permission.setId(cursor.getLong(0));
		permission.setAppName(cursor.getString(1));
		permission.setPermissionState(cursor.getString(2));

		return permission;
	}

	private Ability cursorToAbility(Cursor cursor) {
		Ability ability = new Ability();
		ability.setId(cursor.getLong(0));
		ability.setICFCode(cursor.getString(1));
		ability.setAbilityTitle(cursor.getString(2));
		ability.setAbilityDescription(cursor.getString(3));

		return ability;
	}
}