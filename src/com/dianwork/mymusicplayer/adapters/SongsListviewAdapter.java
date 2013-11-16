package com.dianwork.mymusicplayer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dianwork.mymusicplayer.R;
import com.dianwork.mymusicplayer.activity.ListActivity;
import com.dianwork.mymusicplayer.api.Song;
import com.dianwork.mymusicplayer.api.Song.Msg;

public class SongsListviewAdapter extends BaseAdapter {
	List<Msg> data;
	int layoutId;
	Context context;
	LayoutInflater inflater;
	Song song;

	public SongsListviewAdapter(Context context, List<Msg> data, int layoutId,
			Song song) {
		super();
		this.song = song;
		this.data = data;
		this.layoutId = layoutId;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(ListActivity.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	public List<Msg> getData() {
		return data;
	}

	public void setData(List<Msg> data) {
		this.data = data;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout layout;
		ViewHolder holder;
		if (convertView == null) {
			layout = (LinearLayout) inflater.inflate(layoutId, null);
			holder = new ViewHolder();
			holder.songnameTextView = (TextView) layout
					.findViewById(R.id.textView1);
			holder.artistnametTextView = (TextView) layout
					.findViewById(R.id.textView2);
			layout.setTag(holder);
		} else {
			layout = (LinearLayout) convertView;
			holder = (ViewHolder) layout.getTag();
		}
		holder.songnameTextView.setText(data.get(position).getName());
		holder.artistnametTextView.setText(data.get(position).getArtist());
		final int buttonPosition = position;
		ImageButton addButton = (ImageButton) layout
				.findViewById(R.id.add_to_playlist_button);
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(context,
				// "Clicked " + data.get(buttonPosition).getName(),
				// Toast.LENGTH_SHORT).show();
				String songNameTemp = data.get(buttonPosition).getName();
				String artistNameTemp = data.get(buttonPosition).getArtist();
				int sum = 0;
				for (int i = 0; i < song.Now.getPlaylist().size(); i++) {
					if (song.Now.getPlaylist().get(i).getName()
							.equals(songNameTemp)
							&& song.Now.getPlaylist().get(i).getArtist()
									.equals(artistNameTemp)) {
						sum++;
					}
				}
				if (sum == 0) {
					song.AddToList(data.get(buttonPosition), song.Now);
					ListActivity.playlistListviewAdapter.notifyDataSetChanged();
					Toast.makeText(
							context,
							"向播放列表添加了\"" + data.get(buttonPosition).getName() + "\""
									, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(
							context,
							"\"" + data.get(buttonPosition).getName() + "\""
									+ "已经在播放列表了，快去看看吧", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		return layout;
	}

	class ViewHolder {
		TextView songnameTextView;
		TextView artistnametTextView;
	}

}
