package styr1x.music.lyric;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.media.MediaPlayer;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.color.MaterialColors;
import java.io.*;
import java.text.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {
	
	public final int REQ_CD_MPICKER = 101;
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private String MCheck = "";
	private String MTitle = "";
	private String MArtist = "";
	private String MDuration = "";
	private double MLength = 0;
	private boolean MisLoaded = false;
	private HashMap<String, Object> data = new HashMap<>();
	private String MisVideo = "";
	private double clicks = 0;
	private String MBitrate = "";
	private double ss = 0;
	private double mm = 0;
	private double hth = 0;
	private String Saver = "";
	private String readableSize = "";
	private String AllTime = "";
	private double lyricCheck = 0;
	
	private ArrayList<HashMap<String, Object>> lyricData = new ArrayList<>();
	
	private LinearLayout main;
	private LinearLayout MData;
	private LinearLayout line;
	private LinearLayout TextLinear;
	private LinearLayout MusicP;
	private LinearLayout MImg;
	private LinearLayout MediaInfo;
	private ImageView infoM;
	private ImageView img;
	private TextView Title;
	private TextView Artist;
	private EditText edittext1;
	private ImageView add;
	private LinearLayout DLinear;
	private LinearLayout BLinear;
	private SeekBar seekbar1;
	private LinearLayout TLinear;
	private TextView ctime;
	private TextView atime;
	private ImageView rewind;
	private ImageView plps;
	private ImageView forward;
	
	private Intent MPicker = new Intent(Intent.ACTION_GET_CONTENT);
	private MediaPlayer MPlayer;
	private TimerTask ctimer;
	private AlertDialog.Builder MDInfo;
	private AlertDialog.Builder warn;
	private Intent intent = new Intent();
	private MediaPlayer ydr;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		main = findViewById(R.id.main);
		MData = findViewById(R.id.MData);
		line = findViewById(R.id.line);
		TextLinear = findViewById(R.id.TextLinear);
		MusicP = findViewById(R.id.MusicP);
		MImg = findViewById(R.id.MImg);
		MediaInfo = findViewById(R.id.MediaInfo);
		infoM = findViewById(R.id.infoM);
		img = findViewById(R.id.img);
		Title = findViewById(R.id.Title);
		Artist = findViewById(R.id.Artist);
		edittext1 = findViewById(R.id.edittext1);
		add = findViewById(R.id.add);
		DLinear = findViewById(R.id.DLinear);
		BLinear = findViewById(R.id.BLinear);
		seekbar1 = findViewById(R.id.seekbar1);
		TLinear = findViewById(R.id.TLinear);
		ctime = findViewById(R.id.ctime);
		atime = findViewById(R.id.atime);
		rewind = findViewById(R.id.rewind);
		plps = findViewById(R.id.plps);
		forward = findViewById(R.id.forward);
		MPicker.setType("audio/*,video/*");
		MPicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		MDInfo = new AlertDialog.Builder(this);
		warn = new AlertDialog.Builder(this);
		
		infoM.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (MisLoaded) {
					
					// Assume 'dialog' is your existing AlertDialog or Dialog instance
					
					// Create parent layout
					LinearLayout parentLayout = new LinearLayout(MainActivity.this);
					parentLayout.setOrientation(LinearLayout.VERTICAL);
					parentLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));
					parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
					
					// Inner layout
					LinearLayout linear1 = new LinearLayout(MainActivity.this);
					linear1.setOrientation(LinearLayout.VERTICAL);
					linear1.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
					linear1.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
					
					// Create all TextViews
					TextView ttext = createTextView("Title", 14, true, Color.WHITE);
					TextView tinfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView atext = createTextView("Artist", 14, true, Color.WHITE);
					TextView ainfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView fptext = createTextView("File path", 14, true, Color.WHITE);
					TextView fpinfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView ftext = createTextView("File name", 14, true, Color.WHITE);
					TextView finfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView dtext = createTextView("Duration", 14, true, Color.WHITE);
					TextView dinfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView btext = createTextView("Bitrate", 14, true, Color.WHITE);
					TextView binfo = createTextView("TextView", 12, false, Color.WHITE);
					TextView stext = createTextView("Size", 14, true, Color.WHITE);
					TextView sinfo = createTextView("TextView", 12, false, Color.WHITE);
					
					// Add TextViews to inner layout
					linear1.addView(ttext);
					linear1.addView(tinfo);
					linear1.addView(atext);
					linear1.addView(ainfo);
					linear1.addView(fptext);
					linear1.addView(fpinfo);
					linear1.addView(ftext);
					linear1.addView(finfo);
					linear1.addView(dtext);
					linear1.addView(dinfo);
					linear1.addView(btext);
					linear1.addView(binfo);
					linear1.addView(stext);
					linear1.addView(sinfo);
					
					// Add inner layout to parent
					parentLayout.addView(linear1);
					
					// Set the layout to your existing dialog
					MDInfo.setView(parentLayout);
					
					File file = new File(MCheck);
					long sizeBytes = file.length();
					
					String readableSize;
					if (sizeBytes < 1024) {
						readableSize = sizeBytes + " B";
					} else if (sizeBytes < 1024 * 1024) {
						readableSize = String.format("%.2f KB", sizeBytes / 1024.0);
					} else {
						readableSize = String.format("%.2f MB", sizeBytes / (1024.0 * 1024.0));
					}
					
					MLength = MPlayer.getDuration() / 1000;
					int cmm = (int) MLength / 60; 
					
					int css = (int) MLength % 60; 
					String AllTimee = String.format("%02d:%02d", cmm, css);
					
					long bitrate = Long.parseLong(MBitrate); // e.g., 320000
					String convertedBitrate;
					
					if (bitrate >= 1_000_000) {
						convertedBitrate = String.format("%.2fM", bitrate / 1_000_000.0);
					} else {
						convertedBitrate = String.format("%.0fk", bitrate / 1000.0);
					}
					tinfo.setText(MTitle);
					ainfo.setText(MArtist);
					fpinfo.setText(MCheck.substring((int)(0), (int)(MCheck.lastIndexOf("/"))));
					finfo.setText(Uri.parse(MCheck).getLastPathSegment());
					binfo.setText(convertedBitrate);
					dinfo.setText(AllTimee);
					sinfo.setText(readableSize);
					MDInfo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					MDInfo.create().show();
				}
			}
		});
		
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (MisLoaded) {
					if (edittext1.getText().toString().equals("")) {
						ss = (MPlayer.getCurrentPosition() / 1000) % 60;
						mm = (MPlayer.getCurrentPosition() / 1000) / 60;
						hth = (MPlayer.getCurrentPosition() % 1000) / 10;
						edittext1.setText("[".concat(new DecimalFormat("00").format(mm)).concat(":".concat(new DecimalFormat("00").format(ss))).concat(".".concat(new DecimalFormat("00").format(hth)).concat("] ")));
					} else {
						Saver = edittext1.getText().toString();
						ss = (MPlayer.getCurrentPosition() / 1000) % 60;
						mm = (MPlayer.getCurrentPosition() / 1000) / 60;
						hth = (MPlayer.getCurrentPosition() % 1000) / 10;
						edittext1.setText(Saver.concat("\n").concat("[".concat(new DecimalFormat("00").format(mm)).concat(":".concat(new DecimalFormat("00").format(ss))).concat(".".concat(new DecimalFormat("00").format(hth)).concat("] "))));
					}
				}
			}
		});
		
		seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar _param1) {
				if (MisLoaded) {
					if (MPlayer.isPlaying()) {
						ctimer.cancel();
					}
				} else {
					
				}
			}
			
			@Override
			public void onStopTrackingTouch(SeekBar _param2) {
				if (MisLoaded) {
					MPlayer.seekTo((int)(seekbar1.getProgress()));
					_seeking();
				}
			}
		});
		
		rewind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (MisLoaded) {
					if (MPlayer.isPlaying()) {
						ctimer.cancel();
						MPlayer.seekTo((int)(MPlayer.getCurrentPosition() - 2000));
						_seeking();
					} else {
						MPlayer.seekTo((int)(MPlayer.getCurrentPosition() - 2000));
						seekbar1.setProgress((int)MPlayer.getCurrentPosition());
					}
				}
			}
		});
		
		plps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (MisLoaded) {
					if (MPlayer.isPlaying()) {
						plps.setImageResource(R.drawable.icon_play_arrow_round);
						MPlayer.pause();
						ctimer.cancel();
					} else {
						plps.setImageResource(R.drawable.icon_pause_round);
						MPlayer.start();
						_seeking();
					}
				}
			}
		});
		
		forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (MisLoaded) {
					if (MPlayer.isPlaying()) {
						ctimer.cancel();
						MPlayer.seekTo((int)(MPlayer.getCurrentPosition() + 2000));
						_seeking();
					} else {
						MPlayer.seekTo((int)(MPlayer.getCurrentPosition() + 2000));
						seekbar1.setProgress((int)MPlayer.getCurrentPosition());
					}
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
		MDInfo= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		warn= new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			if (Environment.isExternalStorageManager()) {
				
			} else {
				warn.setTitle("All file access");
				warn.setIcon(R.drawable.icon_warning_amber_round);
				warn.setMessage("this app needs that permission to gain access to music and for converting videos to work properly with this app");
				warn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
						intent.setData(Uri.parse("package:" + getPackageName()));
						startActivity(intent);
					}
				});
				warn.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				warn.create().show();
			}
		} else {
			
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_MPICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				MisLoaded = false;
				MCheck = _filePath.get((int)(0));
				
				_PlayHandler(MCheck);
			}
			else {
				SketchwareUtil.showMessage(getApplicationContext(), "No File Picked");
			}
			break;
			default:
			break;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (getIntent().hasExtra("Music")) {
			_PlayHandler(getIntent().getStringExtra("Music"));
			intent.removeExtra("Music");
		} else {
			
		}
	}
	
	public void _Music(final String _fileLocation) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
			if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_VIDEO) != android.content.pm.PackageManager.PERMISSION_GRANTED
			|| checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
				
				requestPermissions(new String[]{
					android.Manifest.permission.READ_MEDIA_VIDEO,
					android.Manifest.permission.READ_MEDIA_AUDIO
				}, 100);
			} else {
				
				MCheck = _fileLocation;
				MediaMetadataRetriever mediametadataretriever = new MediaMetadataRetriever();
				
				mediametadataretriever.setDataSource(_fileLocation);
				Bitmap MImg = ThumbnailUtils.createVideoThumbnail(_fileLocation, android.provider.MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
				MTitle = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
				MArtist = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				MBitrate = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
				MisLoaded = true;
				MPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new java.io.File(_fileLocation)));
				MLength = MPlayer.getDuration() / 1000;
				int cmm = (int) MLength / 60; 
				
				int css = (int) MLength % 60; 
				String AllTime = String.format("a %02d:%02d", cmm, css);
				seekbar1.setMax((int)MPlayer.getDuration());
				seekbar1.setProgress((int)0);
				
				if (!(MImg == null)) {
					img.setImageBitmap(MImg);
				} else {
					img.setImageResource(R.drawable.icon_music_note_round);
				}
				if (!(MTitle == null)) {
					Title.setText(MTitle);
				} else {
					Title.setText(Uri.parse(_fileLocation).getLastPathSegment().substring((int)(0), (int)(Uri.parse(_fileLocation).getLastPathSegment().lastIndexOf("."))));
				}
				if (!(MArtist == null)) {
					Artist.setText(MArtist);
				} else {
					Artist.setText("Unknown");
				}
				atime.setText(AllTime);
				Title.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				Title.setMarqueeRepeatLimit(-1);
				Title.setSingleLine(true);
				Title.setSelected(true);
				Artist.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				Artist.setMarqueeRepeatLimit(-1);
				Artist.setSingleLine(true);
				Artist.setSelected(true);
			}
		} else {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
			} else {
				
				MCheck = _fileLocation;
				MediaMetadataRetriever mediametadataretriever = new MediaMetadataRetriever();
				
				mediametadataretriever.setDataSource(_fileLocation);
				Bitmap MImg = ThumbnailUtils.createVideoThumbnail(_fileLocation, android.provider.MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
				MTitle = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
				MArtist = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
				MBitrate = mediametadataretriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
				MisLoaded = true;
				MPlayer = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new java.io.File(_fileLocation)));
				MLength = MPlayer.getDuration() / 1000;
				int cmm = (int) MLength / 60; 
				
				int css = (int) MLength % 60; 
				String AllTime = String.format("a %02d:%02d", cmm, css);
				seekbar1.setMax((int)MPlayer.getDuration());
				seekbar1.setProgress((int)0);
				
				if (!(MImg == null)) {
					img.setImageBitmap(MImg);
				} else {
					img.setImageResource(R.drawable.icon_music_note_round);
				}
				if (!(MTitle == null)) {
					Title.setText(MTitle);
				} else {
					Title.setText(Uri.parse(_fileLocation).getLastPathSegment().substring((int)(0), (int)(Uri.parse(_fileLocation).getLastPathSegment().lastIndexOf("."))));
				}
				if (!(MArtist == null)) {
					Artist.setText(MArtist);
				} else {
					Artist.setText("Unknown");
				}
				atime.setText(AllTime);
				Title.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				Title.setMarqueeRepeatLimit(-1);
				Title.setSingleLine(true);
				Title.setSelected(true);
				Artist.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE);
				Artist.setMarqueeRepeatLimit(-1);
				Artist.setSingleLine(true);
				Artist.setSelected(true);
			}
		}
	}
	
	
	public void _Video(final String _fileLocation) {
		MisVideo = _fileLocation;
		warn.setTitle("You selected a video! ");
		warn.setIcon(R.drawable.icon_warning_amber_round);
		warn.setMessage("Only MP3s can be used. This video needs to be converted from MP4 → MP3. There is a option to do that in the three dots. Don’t worry, your original video stays safe and available");
		warn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		warn.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		warn.create().show();
	}
	
	
	public void _UITheme() {
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		
		MenuItem Help = menu.add(Menu.NONE, 0, Menu.NONE, "Help" ); Help.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); Help.setIcon(R.drawable.icon_audio_file_round); 
		
		menu.add(Menu.NONE, 1, Menu.NONE, "Converter");
		
		MenuItem Save = menu.add(Menu.NONE, 2, Menu.NONE, "Save" ); Save.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); Save.setIcon(R.drawable.icon_upload_file_round); 
		
		
		return true;
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			
			case 0:
			
			intent.setClass(getApplicationContext(), MlistActivity.class);
			startActivity(intent);
			finish();
			
			return true;
			case 1:
			
			intent.setClass(getApplicationContext(), ConvertActivity.class);
			startActivity(intent);
			finish();
			
			return true;
			case 2:
			
			if (MisLoaded) {
				if (!edittext1.getText().toString().equals("")) {
					FileUtil.writeFile(MCheck.substring((int)(0), (int)(MCheck.lastIndexOf("/"))).concat("/").concat(Uri.parse(MCheck).getLastPathSegment().substring((int)(0), (int)(Uri.parse(MCheck).getLastPathSegment().lastIndexOf("."))).concat(".lrc")), edittext1.getText().toString());
					lyricCheck = 0;
				} else {
					if (edittext1.getText().toString().equals("") && (lyricCheck == 0)) {
						SketchwareUtil.showMessage(getApplicationContext(), "No lyrics typed, please type something to be able to save the lrc file! ");
					}
					if (edittext1.getText().toString().equals("") && (lyricCheck == 1)) {
						SketchwareUtil.showMessage(getApplicationContext(), "You need to type lyrics, cause why need for empty file in you files?");
					}
					if (edittext1.getText().toString().equals("") && (lyricCheck == 2)) {
						SketchwareUtil.showMessage(getApplicationContext(), "Type something... ");
					}
					if (edittext1.getText().toString().equals("") && (lyricCheck == 3)) {
						SketchwareUtil.showMessage(getApplicationContext(), "nope");
						finishAffinity();
					}
					lyricCheck++;
				}
			}
			
			return true;
			
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	
	public void _more() {
	}
	private void onRequestPermissionsResult(int requestCode, int grantResult) {
		boolean granted = grantResult == PackageManager.PERMISSION_GRANTED;
		if (granted) {
			
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "Not Granted");
		}
	}{
	}
	public TextView createTextView(String text, int textSizeSp, boolean bold, int textColor) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSp);
		tv.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
		tv.setTextColor(textColor);
		return tv;
	}
	public int dpToPx(int dp) {
		return (int) TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		dp,
		getResources().getDisplayMetrics()
		);
		
	}
	
	
	public void _seeking() {
		ctimer = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ctime.setText("c ".concat(new DecimalFormat("00").format((MPlayer.getCurrentPosition() / 1000) / 60).concat(":").concat(new DecimalFormat("00").format((MPlayer.getCurrentPosition() / 1000) % 60))));
						seekbar1.setProgress((int)MPlayer.getCurrentPosition());
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(ctimer, (int)(0), (int)(1000));
	}
	
	
	public void _PlayHandler(final String _Uri) {
		if (_Uri != null && !_Uri.isEmpty()) {
			String extension = "";
			int dotIndex = _Uri.lastIndexOf(".");
			if (dotIndex != -1) {
				extension = _Uri.substring(dotIndex + 1).toLowerCase();
			}
			
			String _type = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(_Uri.substring(_Uri.lastIndexOf(".") + 1).toLowerCase());
			
			if (_type != null && _type.startsWith("audio/")) {
				
				_Music(_Uri);
				
			} else {
				if (_type != null && _type.startsWith("video/")) {
					
					_Video(_Uri);
					SketchwareUtil.showMessage(getApplicationContext(), "Video Media Picked");
					
				} else {
					
					SketchwareUtil.showMessage(getApplicationContext(), "the picked file is not a media file");
					
				}
			}
		} else {
			SketchwareUtil.showMessage(getApplicationContext(), "file path is null or empty");
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