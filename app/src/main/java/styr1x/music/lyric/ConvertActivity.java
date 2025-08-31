package styr1x.music.lyric;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.color.MaterialColors;
import java.io.*;
import java.nio.ByteBuffer;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import org.json.*;
import android.provider.Settings;
import java.nio.ByteBuffer;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;;

public class ConvertActivity extends AppCompatActivity {
	
	private String videoUri = "";
	private String outputPath = "";
	private Uri videoURI;
	
	private LinearLayout mainLinear;
	private LinearLayout linear1;
	private TextView textview1;
	private Button button1;
	private LinearLayout r_u_SureLinear;
	private LinearLayout convertStatus;
	private TextView textview2;
	private FrameLayout frame_layout1;
	private TextView textview3;
	private Button button2;
	private Button button3;
	private VideoView videoview1;
	private TextView textview5;
	private ProgressBar progressbar1;
	private ImageView imageview1;
	private TextView textview4;
	
	private AlertDialog.Builder warn;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.convert);
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
		mainLinear = findViewById(R.id.mainLinear);
		linear1 = findViewById(R.id.linear1);
		textview1 = findViewById(R.id.textview1);
		button1 = findViewById(R.id.button1);
		r_u_SureLinear = findViewById(R.id.r_u_SureLinear);
		convertStatus = findViewById(R.id.convertStatus);
		textview2 = findViewById(R.id.textview2);
		frame_layout1 = findViewById(R.id.frame_layout1);
		textview3 = findViewById(R.id.textview3);
		button2 = findViewById(R.id.button2);
		button3 = findViewById(R.id.button3);
		videoview1 = findViewById(R.id.videoview1);
		MediaController videoview1_controller = new MediaController(this);
		videoview1.setMediaController(videoview1_controller);
		textview5 = findViewById(R.id.textview5);
		progressbar1 = findViewById(R.id.progressbar1);
		imageview1 = findViewById(R.id.imageview1);
		textview4 = findViewById(R.id.textview4);
		warn = new AlertDialog.Builder(this);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.setType("video/*");
				startActivityForResult(intent, 1001);
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
					if (Environment.isExternalStorageManager()) {
						videoview1.setVideoURI(Uri.parse(""));
						videoview1.stopPlayback();
						convertStatus.setVisibility(View.VISIBLE);
						r_u_SureLinear.setVisibility(View.GONE);
						_more();
					} else {
						warn.setTitle("Permission needed! ");
						warn.setIcon(R.drawable.icon_warning_amber_round);
						warn.setMessage("This app need to have the all files access permission to be able to save the converted video to the music folder");
						warn.setPositiveButton("Go To Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
								intent.setData(Uri.parse("package:" + getPackageName()));
								startActivity(intent);
							}
						});
						warn.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						warn.create().show();
					}
				} else {
					videoview1.setVideoURI(Uri.parse(""));
					videoview1.stopPlayback();
					convertStatus.setVisibility(View.VISIBLE);
					r_u_SureLinear.setVisibility(View.GONE);
					_more();
				}
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				recreate();
			}
		});
		
		videoview1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer _mediaPlayer, int _what, int _extra) {
				
				return true;
			}
		});
		
		videoview1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer _mediaPlayer) {
				videoview1.start();
			}
		});
	}
	
	private void initializeLogic() {
		setTitle("Video To Audio | MP4 -> MP3");
		r_u_SureLinear.setVisibility(View.GONE);
		convertStatus.setVisibility(View.GONE);
		videoview1.setEnabled(false);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		if (_requestCode == 1001 && _resultCode == RESULT_OK && _data != null) {
			videoURI = _data.getData();
			videoview1.setVideoURI(videoURI);
			textview1.setVisibility(View.GONE);
			button1.setVisibility(View.GONE);
			r_u_SureLinear.setVisibility(View.VISIBLE);
			
		}
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	public void _more() {
		try {
			String outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
			+ "/converted_audio_" + System.currentTimeMillis() + ".m4a";
			
			MediaExtractor extractor = new MediaExtractor();
			extractor.setDataSource(this, videoURI, null);
			
			int audioTrackIndex = -1;
			MediaFormat format = null;
			
			for (int i = 0; i < extractor.getTrackCount(); i++) {
				format = extractor.getTrackFormat(i);
				String mime = format.getString(MediaFormat.KEY_MIME);
				if (mime.startsWith("audio/")) {
					audioTrackIndex = i;
					break;
				}
			}
			
			if (audioTrackIndex < 0) {
				SketchwareUtil.showMessage(getApplicationContext(), "No audio track found!");
				extractor.release();
				return;
			}
			
			extractor.selectTrack(audioTrackIndex);
			
			
			MediaMuxer muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
			int newTrackIndex = muxer.addTrack(format);
			muxer.start();
			
			ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
			MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
			
			while (true) {
				int sampleSize = extractor.readSampleData(buffer, 0);
				if (sampleSize < 0) break;
				
				info.offset = 0;
				info.size = sampleSize;
				info.presentationTimeUs = extractor.getSampleTime();
				info.flags = extractor.getSampleFlags();
				
				muxer.writeSampleData(newTrackIndex, buffer, info);
				extractor.advance();
			}
			
			muxer.stop();
			muxer.release();
			extractor.release();
			
			progressbar1.setVisibility(View.GONE);
			textview5.setText("Conversion done! Saved in Music directory with name :- ".concat(Uri.parse(outputPath).getLastPathSegment()));
			
		} catch (Exception e) {
			e.printStackTrace();
			progressbar1.setVisibility(View.GONE);
			textview5.setText("Conversion Failed! ");
		}
		/*}
public void convertVideoToAudio(Uri videoUri) {
    try {
        outputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                + "/converted_audio_" + System.currentTimeMillis() + ".m4a";

        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(this, videoUri, null);

        int audioTrackIndex = -1;
        MediaFormat format = null;

        for (int i = 0; i < extractor.getTrackCount(); i++) {
            format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime.startsWith("audio/")) {
                audioTrackIndex = i;
                break;
            }
        }

        if (audioTrackIndex < 0) {
            SketchwareUtil.showMessage(getApplicationContext(), "No audio track found!");
            extractor.release();
            return;
        }

        extractor.selectTrack(audioTrackIndex);

        // Create MediaMuxer to save audio track
        MediaMuxer muxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        int newTrackIndex = muxer.addTrack(format);
        muxer.start();

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();

        while (true) {
            int sampleSize = extractor.readSampleData(buffer, 0);
            if (sampleSize < 0) break;

            info.offset = 0;
            info.size = sampleSize;
            info.presentationTimeUs = extractor.getSampleTime();
            info.flags = extractor.getSampleFlags();

            muxer.writeSampleData(newTrackIndex, buffer, info);
            extractor.advance();
        }

        muxer.stop();
        muxer.release();
        extractor.release();

        SketchwareUtil.showMessage(getApplicationContext(), "Conversion done! Saved in /Music/");
        textview5.setText("Conversion done!");

    } catch (Exception e) {
        e.printStackTrace();
        SketchwareUtil.showMessage(getApplicationContext(), "Conversion failed!");
        progressbar1.setVisibility(View.GONE);
        textview5.setText("Conversion failed!");
    }
  
  
*/
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