package styr1x.music.lyric;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.media.MediaPlayer;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.color.MaterialColors;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class MlistActivity extends AppCompatActivity {
	
	private String SearchText = "";
	private String ValueIsTitle = "";
	private String ValueIsArtist = "";
	private HashMap<String, Object> item = new HashMap<>();
	private String duration = "";
	private double grabber = 0;
	
	private ArrayList<HashMap<String, Object>> MList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> FilterdMList = new ArrayList<>();
	
	private LinearLayout linear1;
	private LinearLayout search_linear;
	private ListView listview1;
	private EditText edittext1;
	private ImageView imageview1;
	
	private Intent SMusic = new Intent();
	private ProgressDialog HoldOn;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.mlist);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		search_linear = findViewById(R.id.search_linear);
		listview1 = findViewById(R.id.listview1);
		edittext1 = findViewById(R.id.edittext1);
		imageview1 = findViewById(R.id.imageview1);
		
		edittext1.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				FilterdMList.clear();
				SearchText = edittext1.getText().toString().toLowerCase();
				for (HashMap<String, Object> item : MList) {
					ValueIsTitle = item.get("title").toString().toLowerCase();
					ValueIsArtist = item.get("artist").toString().toLowerCase();
					if (ValueIsTitle.contains(SearchText) || ValueIsArtist.contains(SearchText)) {
						FilterdMList.add(item);
					}
				}
				if (_charSeq.equals("")) {
					listview1.setAdapter(new Listview1Adapter(MList));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				} else {
					listview1.setAdapter(new Listview1Adapter(FilterdMList));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				
			}
			
			@Override
			public void afterTextChanged(Editable _param1) {
				
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				FilterdMList.clear();
				SearchText = edittext1.getText().toString().toLowerCase();
				for (HashMap<String, Object> item : MList) {
					ValueIsTitle = item.get("title").toString().toLowerCase();
					ValueIsArtist = item.get("artist").toString().toLowerCase();
					if (ValueIsTitle.contains(SearchText) || ValueIsArtist.contains(SearchText)) {
						FilterdMList.add(item);
					}
				}
				if (edittext1.getText().toString().equals("")) {
					listview1.setAdapter(new Listview1Adapter(MList));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				} else {
					listview1.setAdapter(new Listview1Adapter(FilterdMList));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				}
			}
		});
	}
	
	private void initializeLogic() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
			if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_VIDEO) != android.content.pm.PackageManager.PERMISSION_GRANTED
			|| checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
				
				requestPermissions(new String[]{
					android.Manifest.permission.READ_MEDIA_VIDEO,
					android.Manifest.permission.READ_MEDIA_AUDIO
				}, 100);
			} else {
			}
		} else {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
			} else {
			}
		}
		HoldOn = new ProgressDialog(MlistActivity.this);
		HoldOn.setTitle("Getting music...");
		HoldOn.setMessage("Please wait");
		HoldOn.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		HoldOn.setCanceledOnTouchOutside(false);
		HoldOn.show();
		_more();
	}
	
	public void _more() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				String[] projection = {
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.DURATION
				};
				
				Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
				
				if (cursor != null) {
					while (cursor.moveToNext()) {
						long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
						String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
						String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
						long duration = 0;
						try {
							duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
						} catch (Exception e) { }
						
						String path = null;
						try {
							path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
						} catch (Exception e) { }
						
						if (path == null || path.isEmpty()) {
							Uri contentUri = ContentUris.withAppendedId(uri, id);
							path = getRealPathFromUri(contentUri);
						}
						Bitmap albumArt = null;
						try {
							MediaMetadataRetriever mmr = new MediaMetadataRetriever();
							mmr.setDataSource(path);
							byte[] artBytes = mmr.getEmbeddedPicture();
							if (artBytes != null) {
								albumArt = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
							}
							mmr.release();
						} catch (Exception e) { 
							// ignore, albumArt will remain null
						}
						HashMap<String, Object> song = new HashMap<>();
						song.put("title", title);
						song.put("artist", artist);
						song.put("path", path);
						song.put("duration", duration);
						song.put("image", albumArt);
						
						MList.add(song);
					}
					cursor.close();
				}
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						HoldOn.dismiss();
						Collections.sort(MList, (a, b) -> ((String)a.get("title")).compareToIgnoreCase((String)b.get("title")));
						listview1.setAdapter(new Listview1Adapter(MList));
					}
				});
			}
		}).start();
		
	}
	
	private String getRealPathFromUri(Uri uri) {
		String[] proj = { MediaStore.MediaColumns.DATA };
		Cursor c = getContentResolver().query(uri, proj, null, null, null);
		if (c != null) {
			int index = c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			c.moveToFirst();
			String result = c.getString(index);
			c.close();
			return result;
		}
		return null;
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.list, null);
			}
			
			final androidx.cardview.widget.CardView cardview = _view.findViewById(R.id.cardview);
			final LinearLayout linear = _view.findViewById(R.id.linear);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview3 = _view.findViewById(R.id.textview3);
			final TextView title = _view.findViewById(R.id.title);
			final TextView artist = _view.findViewById(R.id.artist);
			
			if (_data.get((int)_position).containsKey("title")) {
				title.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				title.setMarqueeRepeatLimit(-1);
				title.setSingleLine(true);
				title.setSelected(true);
				if (!_data.get((int)_position).get("title").toString().equals("Unknown")) {
					title.setText(_data.get((int)_position).get("title").toString());
				} else {
					title.setText(Uri.parse(_data.get((int)_position).get("location").toString().substring((int)(0), (int)(_data.get((int)_position).get("location").toString().lastIndexOf(".")))).getLastPathSegment());
				}
			}
			if (_data.get((int)_position).containsKey("artist")) {
				artist.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				artist.setMarqueeRepeatLimit(-1);
				artist.setSingleLine(true);
				artist.setSelected(true);
				artist.setText(_data.get((int)_position).get("artist").toString());
			}
			if (_data.get((int)_position).containsKey("duration")) {
				duration = _data.get((int)_position).get("duration").toString();
				grabber = Double.parseDouble(duration) / 1000;
				int cmm = (int) grabber / 60; 
				
				int css = (int) grabber % 60; 
				String ALLTime = String.format("%02d:%02d", cmm, css);
				textview3.setText(ALLTime);
			}
			Bitmap img = null;
			try {
				img = (Bitmap)  _data.get((int)_position).get("image");
			} catch (Exception e) {
				img = null;
			}
			if (img != null) {
				imageview1.setImageBitmap(img);
			} else {
				imageview1.setImageResource(R.drawable.icon_music_note_round);
			}
			linear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					SMusic.setClass(getApplicationContext(), MainActivity.class);
					SMusic.putExtra("Music", _data.get((int)_position).get("path").toString());
					startActivity(SMusic);
					finish();
				}
			});
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}